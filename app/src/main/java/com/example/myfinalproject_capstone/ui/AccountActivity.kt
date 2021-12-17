package com.example.myfinalproject_capstone.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.databinding.ActivityAccountBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


    private fun getUser() {
        // Mendapatkan semua data dari data store
        var userId: String? = null
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getID().observe(this,
            { userID: String ->
                userId = userID
            }
        )
    }
}