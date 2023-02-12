package com.example.ddtapp.ui.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ddtapp.R
import com.example.ddtapp.ui.info.InfoFragment
import com.example.ddtapp.ui.main.MainFragment
import com.example.ddtapp.utils.Screen
import com.example.ddtapp.utils.changeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MenuFragment : Fragment() {

    companion object {
        fun newInstance(data: Bundle? = null): MenuFragment =
            MenuFragment().apply {
                arguments = data
            }
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.mainPage -> {
                changeFragment(
                    R.id.navHostContainer,
                    MainFragment.newInstance(arguments),
                    Screen.MAIN_PAGE.name
                )
                return@OnNavigationItemSelectedListener true
            }
            R.id.infoPage -> {
                changeFragment(
                    R.id.navHostContainer,
                    InfoFragment.newInstance(arguments),
                    Screen.INFO_PAGE.name
                )
                return@OnNavigationItemSelectedListener true
            }
            else -> return@OnNavigationItemSelectedListener false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        if (savedInstanceState == null) {
            changeFragment(
                R.id.navHostContainer,
                MainFragment.newInstance(arguments),
                Screen.MAIN_PAGE.name
            )
        }
    }

    private fun bindViews(view: View) = with(view) {
        bottomNavigationView = findViewById(R.id.bottomNav)
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        bottomNavigationView.setOnNavigationItemReselectedListener {}
    }
}