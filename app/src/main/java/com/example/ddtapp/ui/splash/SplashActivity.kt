package com.example.ddtapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.ddtapp.ui.menu.MenuActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed(Runnable {
            val i = Intent(this, MenuActivity::class.java)
            startActivity(i)
            finish()
        }, 2000)
    }
}