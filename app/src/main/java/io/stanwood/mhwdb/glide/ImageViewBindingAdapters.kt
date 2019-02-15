package io.stanwood.mhwdb.glide

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImageViewBindingAdapters(private val glideAppFactory: GlideAppFactory) {

    @BindingAdapter(
        value = ["android:src", "placeholder", "circleCrop", "skipCache"],
        requireAll = false
    )
    fun loadImageFromString(
        imageView: ImageView,
        url: String?,
        placeholder: Drawable? = null,
        circleCrop: Boolean = false,
        skipCache: Boolean = false
    ) {
        url?.apply {
            glideAppFactory.get()
                .load(this)
                .fitCenter()
                .apply {
                    if (circleCrop) {
                        circleCrop()
                    }
                    if (skipCache) {
                        diskCacheStrategy(DiskCacheStrategy.NONE)
                        skipMemoryCache(true)
                    }
                    placeholder?.let { placeholder(it) }
                }
                .into(imageView)
        }
            ?: glideAppFactory.get().apply {
                clear(imageView)
            }
    }
}