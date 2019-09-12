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

package io.stanwood.mhwdb.feature.weapons.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import io.stanwood.framework.arch.core.rx.subscribeBy
import io.stanwood.framework.arch.di.factory.ViewModelFactory
import io.stanwood.mhwdb.R
import io.stanwood.mhwdb.databinding.FragmentWeaponsBinding
import io.stanwood.mhwdb.feature.weapons.vm.WeaponsViewModel
import javax.inject.Inject

class WeaponsFragment : Fragment(), HasAndroidInjector {
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
    private lateinit var viewModel: WeaponsViewModel
    @Inject
    internal lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    internal lateinit var dataBindingComponent: DataBindingComponent
    private var binding: FragmentWeaponsBinding? = null
    private var rcvAdapter: WeaponsAdapter? = null
    override fun androidInjector(): AndroidInjector<Any> = androidInjector

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
        DataBindingUtil.inflate<FragmentWeaponsBinding>(inflater, R.layout.fragment_weapons, container, false, dataBindingComponent)
            .apply {
                binding = this
            }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            rcv.apply {
                setHasFixedSize(true)
                layoutManager = GridLayoutManager(context, 3)
            }
            lifecycleOwner = viewLifecycleOwner
        }
        rcvAdapter = WeaponsAdapter(LayoutInflater.from(context), dataBindingComponent) { viewModel.itemClicked(it) }
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
            navigator.subscribeBy(viewLifecycleOwner, onSuccess = { it.navigate(findNavController()) })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.destroy()
    }
}