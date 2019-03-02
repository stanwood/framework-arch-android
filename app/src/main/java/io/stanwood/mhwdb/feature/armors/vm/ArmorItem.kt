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