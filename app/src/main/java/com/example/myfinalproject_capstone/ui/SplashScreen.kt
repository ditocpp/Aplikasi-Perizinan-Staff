package com.example.myfinalproject_capstone.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.myfinalproject_capstone.R

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val delay = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreen, FirstActivity::class.java)
            startActivity(intent)
        }, delay.toLong())
    }
}