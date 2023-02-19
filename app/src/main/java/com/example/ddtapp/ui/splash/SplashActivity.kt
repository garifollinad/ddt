package com.example.ddtapp.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.ddtapp.ui.menu.MenuActivity
import com.example.ddtapp.utils.Constants

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler().postDelayed({
            val i = Intent(this, MenuActivity::class.java)
            startActivity(i)
            finish()
        }, Constants.DELAY_MILLIS)
    }
}