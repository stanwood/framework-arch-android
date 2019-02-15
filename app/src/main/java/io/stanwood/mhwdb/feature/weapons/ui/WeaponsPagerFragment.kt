package io.stanwood.mhwdb.feature.weapons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.mhwdb.databinding.FragmentWeaponsPagerBinding
import io.stanwood.mhwdb.feature.weapons.vm.WeaponsPagerViewModel
import javax.inject.Inject

class WeaponsPagerFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<WeaponsPagerViewModel>
    private var viewModel: WeaponsPagerViewModel? = null
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    private var binding: FragmentWeaponsPagerBinding? = null

    override fun supportFragmentInjector() = androidInjector

    private var pagerAdapter: WeaponsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(WeaponsPagerViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentWeaponsPagerBinding.inflate(inflater, container, false)
            .apply {
                binding = this
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestApplyInsets()
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
        }
        viewModel?.items?.subscribeBy(viewLifecycleOwner, onNext = {
            it?.also { items ->
                binding?.apply {
                    pagerAdapter = WeaponsPagerAdapter(childFragmentManager, requireContext(), items)
                    pager.adapter = pagerAdapter
                    tabLayout.setupWithViewPager(pager)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.destroy()
    }
}