package io.stanwood.mhwdb.feature.container.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import io.stanwood.framework.arch.di.module.FragmentModule
import io.stanwood.framework.arch.di.scope.ChildFragmentScope
import io.stanwood.mhwdb.feature.armor.di.ArmorFragmentModule
import io.stanwood.mhwdb.feature.armor.ui.ArmorFragment
import io.stanwood.mhwdb.feature.container.ui.ContainerFragment
import io.stanwood.mhwdb.feature.weapons.di.WeaponsFragmentModule
import io.stanwood.mhwdb.feature.weapons.di.WeaponsPagerFragmentModule
import io.stanwood.mhwdb.feature.weapons.ui.WeaponsFragment
import io.stanwood.mhwdb.feature.weapons.ui.WeaponsPagerFragment

@Module(includes = [ContainerFragmentSubModule::class])
abstract class ContainerFragmentModule : FragmentModule<ContainerFragment>() {

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = [WeaponsFragmentModule::class])
    internal abstract fun contributeWeaponsFragment(): WeaponsFragment

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = [ArmorFragmentModule::class])
    internal abstract fun contributeArmorFragment(): ArmorFragment

    @ChildFragmentScope
    @ContributesAndroidInjector(modules = [WeaponsPagerFragmentModule::class])
    internal abstract fun contributeWeaponsPagerFragment(): WeaponsPagerFragment
}