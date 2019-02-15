package io.stanwood.mhwdb.feature.armors.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.mhwdb.R
import io.stanwood.mhwdb.databinding.FragmentArmorBinding
import io.stanwood.mhwdb.feature.armors.vm.ArmorsViewModel
import javax.inject.Inject

class ArmorsFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<ArmorsViewModel>
    private var viewModel: ArmorsViewModel? = null
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var dataBindingComponent: DataBindingComponent
    @Inject
    internal lateinit var mainNavController: NavController
    private var binding: FragmentArmorBinding? = null
    private var rcvAdapter: ArmorsAdapter? = null

    override fun supportFragmentInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(ArmorsViewModel::class.java)
            .apply {
                savedInstanceState?.getLongArray(KEY_EXPANDED_SETS)?.apply {
                    expandedSets.addAll(this.asList())
                }
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentArmorBinding.inflate(inflater, container, false, dataBindingComponent)
            .apply {
                binding = this
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestApplyInsets()
        binding?.apply {
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                addItemDecoration(ArmorItemDivider(resources.getDrawable(R.drawable.armor_item_divider)))
            }
            lifecycleOwner = viewLifecycleOwner
        }
        rcvAdapter = ArmorsAdapter(LayoutInflater.from(context), dataBindingComponent) { viewModel?.itemClicked(it) }
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
            navigator.subscribeBy(
                viewLifecycleOwner,
                onSuccess = { mainNavController.navigate(it.navDirections, it.navOptions) })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel?.apply {
            outState.putLongArray(KEY_EXPANDED_SETS, expandedSets.toLongArray())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.destroy()
    }

    companion object {
        private const val KEY_EXPANDED_SETS = "expanded"
    }
}