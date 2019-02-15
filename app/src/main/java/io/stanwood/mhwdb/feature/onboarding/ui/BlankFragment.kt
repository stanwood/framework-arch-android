package io.stanwood.mhwdb.feature.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.mhwdb.databinding.FragmentBlankBinding
import javax.inject.Inject

class BlankFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var dataBindingComponent: DataBindingComponent
    private var binding: FragmentBlankBinding? = null

    override fun supportFragmentInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) =
        FragmentBlankBinding.inflate(inflater, container, false, dataBindingComponent)
            .apply {
                binding = this
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestApplyInsets()
        binding?.lifecycleOwner = viewLifecycleOwner
    }
}