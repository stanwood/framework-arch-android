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

package io.stanwood.mhwdb.feature.armors.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.AndroidSupportInjection
import dagger.android.support.HasSupportFragmentInjector
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.framework.arch.nav.addNavigateUp
import io.stanwood.mhwdb.databinding.FragmentArmorDetailsBinding
import io.stanwood.mhwdb.feature.armors.vm.ArmorDetailsViewModel
import javax.inject.Inject

class ArmorDetailsFragment : Fragment(), HasSupportFragmentInjector {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<ArmorDetailsViewModel>
    private var viewModel: ArmorDetailsViewModel? = null
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    internal lateinit var dataBindingComponent: DataBindingComponent
    private var binding: FragmentArmorDetailsBinding? = null
    private var rcvAdapter: ArmorDetailsAdapter? = null

    override fun supportFragmentInjector() = androidInjector

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = viewModelFactory.create(ArmorDetailsViewModel::class.java)
            .apply {
                init(ArmorDetailsFragmentArgs.fromBundle(arguments!!).armorId)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FragmentArmorDetailsBinding.inflate(inflater, container, false, dataBindingComponent)
            .apply {
                binding = this
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.requestApplyInsets()
        binding?.apply {
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
            }
            toolbar.addNavigateUp(requireFragmentManager())
            lifecycleOwner = viewLifecycleOwner
        }
        rcvAdapter = ArmorDetailsAdapter(LayoutInflater.from(context), dataBindingComponent)
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

            data.subscribeBy(viewLifecycleOwner, onNext = {
                binding?.armor = it
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel?.destroy()
    }
}