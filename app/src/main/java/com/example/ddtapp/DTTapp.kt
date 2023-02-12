package com.example.ddtapp

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.example.ddtapp.di.AppComponent
import com.example.ddtapp.di.Injectable
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class DTTapp: Application(), HasAndroidInjector, Application.ActivityLifecycleCallbacks {

    @Inject
    internal lateinit var dispatchingAndroidInjectorAny: DispatchingAndroidInjector<Any>

    private val appComponent by lazy {
        AppComponent.create(this)
    }

    override fun onCreate() {
        super.onCreate()
        appComponent.inject(this)
        registerActivityLifecycleCallbacks(this)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        if (p0 is HasAndroidInjector) {
            AndroidInjection.inject(p0)
        }
        if (p0 is FragmentActivity) {
            p0.supportFragmentManager
                .registerFragmentLifecycleCallbacks(
                    object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(
                            fm: FragmentManager,
                            f: Fragment,
                            savedInstanceState: Bundle?
                        ) {
                            if (f is Injectable) {
                                AndroidSupportInjection.inject(f)
                            }
                        }
                    }, true
                )
        }
    }

    override fun onActivityStarted(p0: Activity) {}

    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjectorAny;
}