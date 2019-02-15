package io.stanwood.mhwdb.feature.armors.ui

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ArmorItemDivider(val divider: Drawable) : RecyclerView.ItemDecoration() {
    private val bounds = Rect()
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.layoutManager == null) {
            return
        }
        c.save()
        val left: Int
        val right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            c.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            parent.getChildAt(i).apply {
                if (parent.getChildViewHolder(this) is ArmorsAdapter.DividerViewHolder) {
                    parent.getDecoratedBoundsWithMargins(this, bounds)
                    val bottom = bounds.top + divider.intrinsicHeight + Math.round(this.translationY)
                    val top = bottom - divider.intrinsicHeight
                    divider.setBounds(left, top, right, bottom)
                    divider.draw(c)
                }
            }
        }
        c.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildViewHolder(view) is ArmorsAdapter.DividerViewHolder) {
            outRect.set(0, 0, 0, divider.intrinsicHeight)
        }

    }
}