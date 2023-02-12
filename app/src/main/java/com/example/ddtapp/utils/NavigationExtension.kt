package com.example.ddtapp.utils

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.changeActFragment(
    layoutId: Int,
    fragment: Fragment,
    tagFragmentName: String,
    addToStack: Boolean = false
) {
    val fragmentManager = supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    if (addToStack) {
        fragmentTransaction.addToBackStack(tagFragmentName)
    }
    val currentFragment = fragmentManager.primaryNavigationFragment
    if (currentFragment != null) {
        fragmentTransaction.hide(currentFragment)
    }
    var fragmentTemp = fragmentManager.findFragmentByTag(tagFragmentName)
    if (fragmentTemp == null) {
        fragmentTemp = fragment
        fragmentTransaction.add(layoutId, fragmentTemp, tagFragmentName)
    } else {
        fragmentTransaction.show(fragmentTemp)
    }
    if (!isFinishing) {
        fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp)
        fragmentTransaction.setReorderingAllowed(true)
        fragmentTransaction.commitAllowingStateLoss()
    }
}

fun Fragment.changeFragment(
    layoutId: Int,
    fragment: Fragment,
    tagFragmentName: String,
    addToStack: Boolean = false
) {
    val fragmentManager = childFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()
    if (addToStack) {
        fragmentTransaction.addToBackStack(tagFragmentName)
    }
    val currentFragment = fragmentManager.primaryNavigationFragment
    if (currentFragment != null) {
        fragmentTransaction.hide(currentFragment)
    }
    var fragmentTemp = fragmentManager.findFragmentByTag(tagFragmentName)
    if (fragmentTemp == null) {
        fragmentTemp = fragment
        fragmentTransaction.add(layoutId, fragmentTemp, tagFragmentName)
    } else {
        fragmentTransaction.show(fragmentTemp)
    }
    fragmentTransaction.setPrimaryNavigationFragment(fragmentTemp)
    fragmentTransaction.setReorderingAllowed(true)
    fragmentTransaction.commitAllowingStateLoss()
}