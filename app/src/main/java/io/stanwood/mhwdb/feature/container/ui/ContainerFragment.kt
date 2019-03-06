package io.stanwood.mhwdb.feature.container.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.framework.ui.extensions.setApplyWindowInsetsToChild
import io.stanwood.mhwdb.R
import io.stanwood.mhwdb.databinding.FragmentContainerBinding
import io.stanwood.mhwdb.feature.container.vm.ContainerViewModel
import javax.inject.Inject

class ContainerFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<ContainerViewModel>
    private lateinit var viewModel: ContainerViewModel
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>

    private var binding: FragmentContainerBinding? = null

    override fun supportFragmentInjector() = androidInjector

    private val childNavController
        get() = childFragmentManager.findFragmentById(R.id.nav_host_child_fragment)?.findNavController()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(ContainerViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentContainerBinding.inflate(inflater, container, false)
            .apply {
                binding = this
                root.setApplyWindowInsetsToChild()
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.lifecycleOwner = viewLifecycleOwner
        binding?.setNavSelectedListener { menuItem ->
            binding?.bottomNav?.selectedItemId?.let {
                viewModel.selectMenuItem(menuItem.itemId, it)
            } ?: false
        }
        viewModel.apply {
            navigator.subscribeBy(viewLifecycleOwner, onNext = {
                childNavController?.navigate(it.navDirections, it.navOptions)
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroy()
    }
}