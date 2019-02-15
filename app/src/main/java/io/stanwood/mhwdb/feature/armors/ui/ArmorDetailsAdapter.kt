package io.stanwood.mhwdb.feature.armors.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.stanwood.framework.databinding.recyclerview.BindingViewHolder
import io.stanwood.mhwdb.BR
import io.stanwood.mhwdb.databinding.LayoutArmorDetailsItemBinding
import io.stanwood.mhwdb.feature.armors.vm.ArmorDetailsItemViewModel

class ArmorDetailsAdapter(
    private val inflater: LayoutInflater,
    private val dataBindingComponent: DataBindingComponent
) :
    ListAdapter<ArmorDetailsItemViewModel, BindingViewHolder>(
        object : DiffUtil.ItemCallback<ArmorDetailsItemViewModel>() {
            override fun areItemsTheSame(
                oldItem: ArmorDetailsItemViewModel,
                newItem: ArmorDetailsItemViewModel
            ) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: ArmorDetailsItemViewModel,
                newItem: ArmorDetailsItemViewModel
            ) =
                false
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BindingViewHolder(
            LayoutArmorDetailsItemBinding.inflate(
                inflater,
                parent,
                false,
                dataBindingComponent
            )
        )

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.binding.apply {
            setVariable(BR.vm, getItem(position))
            executePendingBindings()
        }
    }
}