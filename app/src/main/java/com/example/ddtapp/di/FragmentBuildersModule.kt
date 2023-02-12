package com.example.ddtapp.di

import com.example.ddtapp.ui.info.InfoFragment
import com.example.ddtapp.ui.main.MainDetailFragment
import com.example.ddtapp.ui.main.MainFragment
import com.example.ddtapp.ui.menu.MenuFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMenuFragment(): MenuFragment

    @ContributesAndroidInjector
    abstract fun contributeMainFragment(): MainFragment

    @ContributesAndroidInjector
    abstract fun contributeMainDetailFragment(): MainDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeInfoFragment(): InfoFragment
}