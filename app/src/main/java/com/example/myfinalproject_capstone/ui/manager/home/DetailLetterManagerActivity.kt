package com.example.myfinalproject_capstone.ui.manager.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import android.content.Intent
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.databinding.ActivityDetailLetterManagerBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


class DetailLetterManagerActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailLetterManagerBinding
    private var database: DatabaseReference? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")


    companion object {
        const val EXTRA_LETTER = "extra_price"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Letters")

        binding = ActivityDetailLetterManagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDetailLetter()

        binding.btnClose.setOnClickListener { backToHome() }
    }

    private fun backToHome() {
        val intent = Intent(this@DetailLetterManagerActivity, ManagerHomeActivity::class.java)
        startActivity(intent)
    }

    private fun showDetailLetter() {
        database?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        if (intent.getStringExtra(EXTRA_LETTER) == ds.child("letterID").value as String){
                            binding.edtTypeLeave.setText(ds.child("title").value as String)
                            binding.edtDescription.setText(ds.child("description").value as String)
                            binding.edtStartDatePicker.setText(ds.child("durationStart").value as String)
                            binding.edtEndDatePicker.setText(ds.child("durationFinish").value as String)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}

