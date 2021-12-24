package com.example.myfinalproject_capstone.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.databinding.ActivityAccountBinding
import com.example.myfinalproject_capstone.databinding.ActivityFirstBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory

class AccountActivity : AppCompatActivity() {

    private var binding: ActivityAccountBinding? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        getUser()

        binding!!.btnSignOut.setOnClickListener {
            Toast.makeText(applicationContext, "Logging Out...", Toast.LENGTH_SHORT).show()

            val pref = SettingPreferences.getInstance(dataStore)
            val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
                MainViewModel::class.java
            )
            val clear = ""
            mainViewModel.saveUserSetting(clear, clear, clear, clear, clear, clear)
            val intent = Intent(this@AccountActivity, FirstActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // clears current and previous activity stack
            startActivity(intent)
            finish()
        }
    }

    private fun getUser() {
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getNameStaff().observe(this,
            { userID: String ->
                val nameUser = userID
                binding?.tvName?.setText(nameUser)
            }
        )

        mainViewModel.getCompanyID().observe(this,
            { userCompanyID: String ->
                val companyCodeUser = userCompanyID
                binding?.tvCompanyCode?.setText(companyCodeUser)
            }
        )

        mainViewModel.getEmail().observe(this,
            { userCompanyID: String ->
                val emailUser = userCompanyID
                binding?.tvEmail?.setText(emailUser)
            }
        )

        mainViewModel.getPosition().observe(this,
            { userCompanyID: String ->
                val positionUser = userCompanyID
                binding?.tvPosition?.setText(positionUser)
            }
        )
    }
}