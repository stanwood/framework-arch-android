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
import androidx.core.content.res.ResourcesCompat
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
    private lateinit var viewModel: ArmorsViewModel
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
                retryCallback = View.OnClickListener { viewModel.retry() }
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                ResourcesCompat.getDrawable(resources, R.drawable.armor_item_divider, null)?.apply {
                    addItemDecoration(ArmorItemDivider(this))
                }
            }
            lifecycleOwner = viewLifecycleOwner
        }
        rcvAdapter = ArmorsAdapter(LayoutInflater.from(context), dataBindingComponent) { viewModel.itemClicked(it) }
        viewModel.apply {
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
            status.subscribeBy(viewLifecycleOwner, onNext = {
                binding?.status = it
            })
            navigator.subscribeBy(
                viewLifecycleOwner,
                onSuccess = { it.navigate(mainNavController) })
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.apply {
            outState.putLongArray(KEY_EXPANDED_SETS, expandedSets.toLongArray())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroy()
    }

    companion object {
        private const val KEY_EXPANDED_SETS = "expanded"
    }
}