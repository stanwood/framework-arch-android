package io.stanwood.mhwdb.feature.weapons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.mhwdb.databinding.FragmentWeaponsBinding
import io.stanwood.mhwdb.feature.weapons.vm.WeaponsViewModel
import javax.inject.Inject

class WeaponsFragment : Fragment(), HasSupportFragmentInjector {
    companion object {
        private const val KEY_WEAPON_TYPE_ID = "weapontype"
        fun createInstance(weaponTypeId: String) =
            WeaponsFragment().apply {
                arguments = Bundle().apply {
                    putString(KEY_WEAPON_TYPE_ID, weaponTypeId)
                }
            }
    }

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<WeaponsViewModel>
    private var viewModel: WeaponsViewModel? = null
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var dataBindingComponent: DataBindingComponent
    private var binding: FragmentWeaponsBinding? = null
    private var rcvAdapter: WeaponsAdapter? = null
    override fun supportFragmentInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(WeaponsViewModel::class.java)
            .apply {
                arguments?.getString(KEY_WEAPON_TYPE_ID)?.apply {
                    init(this)
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentWeaponsBinding.inflate(inflater, container, false, dataBindingComponent)
            .apply {
                binding = this
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestApplyInsets()
        binding?.apply {
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 3)
            }
            lifecycleOwner = viewLifecycleOwner
        }
        rcvAdapter = WeaponsAdapter(LayoutInflater.from(context), dataBindingComponent) { viewModel?.itemClicked(it) }
        viewModel?.apply {
            items.subscribeBy(viewLifecycleOwner, onNext = {
                binding?.rcv?.apply {
                    rcvAdapter?.apply {
                        if (adapter == null) {
                            adapter = this
                        }
                        submitList(it)
                    }
                }
            })
            navigator.subscribeBy(viewLifecycleOwner, onSuccess = { findNavController().navigate(it.navDirections, it.navOptions) })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.destroy()
    }
}