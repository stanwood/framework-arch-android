package io.stanwood.mhwdb.feature.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.framework.ui.extensions.setApplyWindowInsetsToChild
import io.stanwood.mhwdb.databinding.FragmentOnboardingBinding
import io.stanwood.mhwdb.feature.onboarding.vm.OnboardingViewModel
import javax.inject.Inject

class OnboardingFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<OnboardingViewModel>
    private lateinit var viewModel: OnboardingViewModel
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var dataBindingComponent: DataBindingComponent
    private var binding: FragmentOnboardingBinding? = null

    override fun supportFragmentInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(OnboardingViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentOnboardingBinding.inflate(inflater, container, false, dataBindingComponent)
            .apply {
                binding = this
                root.setApplyWindowInsetsToChild()
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.apply {
            binding?.also { bind ->
                bind.lifecycleOwner = viewLifecycleOwner
                bind.btnContinue.setOnClickListener { viewModel.nextPage() }
                navigator.subscribeBy(viewLifecycleOwner, onNext = { findNavController().navigate(it.navDirections, it.navOptions) })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroy()
    }
}