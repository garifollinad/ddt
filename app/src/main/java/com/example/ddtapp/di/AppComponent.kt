package com.example.ddtapp.di

import android.app.Application
import android.content.Context
import com.example.ddtapp.DTTapp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules =[
        AndroidInjectionModule::class,
        ActivityModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        StorageModule::class,
        ViewModelModule::class
    ]
)
interface AppComponent : AndroidInjector<DTTapp> {

    companion object {
        fun create(application: Application): AppComponent {
            return DaggerAppComponent.builder()
                .applicationContext(application.applicationContext)
                .storageModule(StorageModule(application.applicationContext))
                .repositoryModule(RepositoryModule())
                .networkModule(NetworkModule())
                .build()
        }
    }

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun applicationContext(context: Context): Builder

        fun storageModule(storageModule: StorageModule):Builder

        fun repositoryModule(repositoryModule: RepositoryModule): Builder

        fun networkModule(networkModule: NetworkModule): Builder

        fun build(): AppComponent
    }

}