package com.example.ddtapp.di

import com.example.ddtapp.ui.menu.MenuActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMenuActivity(): MenuActivity

}