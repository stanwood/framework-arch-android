package io.stanwood.mhwdb.feature.armors.vm

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

sealed class ArmorItem(val id: Long, val title: String, val viewType: Int, val imageUrl: String? = null, _selcted: Boolean = false) :
    BaseObservable() {
    var selected: Boolean = false
        @Bindable get
        set(value) {
            if (field != value) {
                field = value
                notifyChange()
            }
        }

    class ArmorViewModel(id: Long, title: String, imageUrl: String? = null, val subtitle: String) :
        ArmorItem(id, title, VIEW_TYPE, imageUrl) {
        companion object {
            const val VIEW_TYPE = 1
        }
    }

    class SetViewModel(id: Long, title: String) : ArmorItem(id, title, VIEW_TYPE) {
        companion object {
            const val VIEW_TYPE = 2
        }
    }
}