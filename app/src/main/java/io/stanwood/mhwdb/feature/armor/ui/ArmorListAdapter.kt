package io.stanwood.mhwdb.feature.armor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.stanwood.framework.databinding.recyclerview.BindingViewHolder
import io.stanwood.mhwdb.BR
import io.stanwood.mhwdb.databinding.LayoutArmorItemBinding
import io.stanwood.mhwdb.databinding.LayoutArmorSetItemBinding
import io.stanwood.mhwdb.feature.armor.vm.ArmorItem

class ArmorListAdapter(
    private val inflater: LayoutInflater,
    private val dataBindingComponent: DataBindingComponent,
    private val clickCallback: ((ArmorItem) -> Unit)
) :
    ListAdapter<ArmorItem, BindingViewHolder>(
        object : DiffUtil.ItemCallback<ArmorItem>() {
            override fun areItemsTheSame(oldItem: ArmorItem, newItem: ArmorItem) =
                oldItem.viewType == newItem.viewType

            override fun areContentsTheSame(oldItem: ArmorItem, newItem: ArmorItem): Boolean {
                return oldItem.title == newItem.title &&
                    oldItem.imageUrl == newItem.imageUrl &&
                    oldItem.selected == newItem.selected
            }

            override fun getChangePayload(oldItem: ArmorItem, newItem: ArmorItem) =
                oldItem.imageUrl == newItem.imageUrl
        }) {


    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            ArmorItem.ArmorViewModel.VIEW_TYPE -> DividerViewHolder(LayoutArmorItemBinding.inflate(
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
            else -> BindingViewHolder(LayoutArmorSetItemBinding.inflate(inflater, parent, false, dataBindingComponent)
                .apply {
                    root.setOnClickListener {
                        this.vm?.apply { clickCallback.invoke(this) }
                    }
                })
        }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int, payloads: MutableList<Any>) {
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

    class DividerViewHolder(viewDataBinding: ViewDataBinding) : BindingViewHolder(viewDataBinding)
}