package com.example.myfinalproject_capstone.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.example.myfinalproject_capstone.R
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private val delay = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        checkLoggedInState()
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this@SplashScreen, FirstActivity::class.java)
            startActivity(intent)
        }, delay.toLong())
    }

    private fun checkLoggedInState() {
        if(FirebaseAuth.getInstance().currentUser == null) {
            Toast.makeText(applicationContext, "You are not Logged In", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "Logging Out...", Toast.LENGTH_LONG).show()
            FirebaseAuth.getInstance().signOut()
            finish()
            overridePendingTransition(0, 0)
            startActivity(getIntent())
            overridePendingTransition(0, 0)
            TODO("Automatically bring the user to the respective Activity (i.e = staff to staffActivity)")
        }
    }
}