package io.stanwood.mhwdb.feature.weapons.ui

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import io.stanwood.mhwdb.repository.mhw.WeaponType


class WeaponsPagerAdapter(fm: FragmentManager, val context: Context, var items: List<WeaponType>) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int) = WeaponsFragment.createInstance(items[position].id)

    override fun getCount() = items.size

    override fun getPageTitle(position: Int) = items[position].name
}