package com.example.myfinalproject_capstone.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import com.example.myfinalproject_capstone.ui.manager.home.ManagerHomeActivity
import com.example.myfinalproject_capstone.ui.staff.home.StaffHomeActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")

    private val delay = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            val userPos = getPosition()
            println(userPos)
            if (userPos == "Staff") {
                val intent = Intent(this@SplashScreen, StaffHomeActivity::class.java)
                startActivity(intent)
            } else if (userPos == "Manager") {
                val intent = Intent(this@SplashScreen, ManagerHomeActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this@SplashScreen, FirstActivity::class.java)
                startActivity(intent)
            }
        }, delay.toLong())
    }

    private fun getPosition(): String? {
        var userPos: String? = null
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getPosition().observe(this,
            { position: String ->
                userPos = position
            }
        )
        return userPos
    }

    private fun checkLoggedInState() {
            TODO("Automatically bring the user to the respective Activity (i.e = staff to staffHome, manager to managerHome)")
    }
}