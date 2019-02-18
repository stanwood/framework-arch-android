package io.stanwood.mhwdb.feature.weapons.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.stanwood.framework.databinding.recyclerview.BindingViewHolder
import io.stanwood.mhwdb.BR
import io.stanwood.mhwdb.databinding.LayoutWeaponsItemBinding
import io.stanwood.mhwdb.feature.weapons.vm.WeaponsItemViewModel

class WeaponsAdapter(
    private val inflater: LayoutInflater,
    private val dataBindingComponent: DataBindingComponent,
    private val clickCallback: ((WeaponsItemViewModel) -> Unit)
) :
    ListAdapter<WeaponsItemViewModel, BindingViewHolder>(
        object : DiffUtil.ItemCallback<WeaponsItemViewModel>() {
            override fun areItemsTheSame(
                oldItem: WeaponsItemViewModel,
                newItem: WeaponsItemViewModel
            ) =
                oldItem.title == newItem.title

            override fun areContentsTheSame(
                oldItem: WeaponsItemViewModel,
                newItem: WeaponsItemViewModel
            ) =
                oldItem.title == newItem.title && oldItem.subtitle == newItem.subtitle && oldItem.imageUrl == newItem.imageUrl

            override fun getChangePayload(
                oldItem: WeaponsItemViewModel,
                newItem: WeaponsItemViewModel
            ) =
                oldItem.imageUrl == newItem.imageUrl
        }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        BindingViewHolder(LayoutWeaponsItemBinding.inflate(
            inflater,
            parent,
            false,
            dataBindingComponent
        )
            .apply {
                root.setOnClickListener {
                    this.vm?.apply { clickCallback.invoke(this) }
                }
            })

    override fun onBindViewHolder(
        holder: BindingViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty() || payloads.any { it == true }) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.binding.apply {
                setVariable(BR.imageUrl, getItem(position).imageUrl)
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {
        holder.binding.apply {
            getItem(position).also { item ->
                setVariable(BR.vm, item)
                setVariable(BR.imageUrl, item.imageUrl)
            }
            executePendingBindings()
        }
    }
}