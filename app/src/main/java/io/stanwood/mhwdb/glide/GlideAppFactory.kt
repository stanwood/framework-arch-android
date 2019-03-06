package io.stanwood.mhwdb.glide

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

interface GlideAppFactory {
    fun get(): GlideRequests
}

class GlideAppFragmentFactory(private val fragment: Fragment) : GlideAppFactory {
    override fun get() = GlideApp.with(fragment)
}

class GlideAppActivityFactory(private val activity: AppCompatActivity) : GlideAppFactory {
    override fun get() = GlideApp.with(activity)
}