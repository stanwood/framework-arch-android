/*
 * Copyright (c) 2019 stanwood GmbH
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.stanwood.mhwdb.feature.onboarding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.framework.ui.extensions.setApplyWindowInsetsToChild
import io.stanwood.mhwdb.R
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
        DataBindingUtil.inflate<FragmentOnboardingBinding>(inflater, R.layout.fragment_onboarding, container, false, dataBindingComponent)
            .apply {
                binding = this
                root.setApplyWindowInsetsToChild()
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.apply {
            binding?.also { bind ->
                bind.lifecycleOwner = viewLifecycleOwner
                bind.btnContinue.setOnClickListener { viewModel.nextPage() }
                navigator.subscribeBy(viewLifecycleOwner, onNext = { it.navigate(findNavController()) })
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroy()
    }
}