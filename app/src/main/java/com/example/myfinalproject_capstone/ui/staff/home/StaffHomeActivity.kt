package com.example.myfinalproject_capstone.ui.staff.home

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinalproject_capstone.ui.AccountActivity
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.adapter.ListLetterStaffAdapter
import com.example.myfinalproject_capstone.databinding.ActivityStaffHomeBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import com.example.myfinalproject_capstone.entity.Letter
import com.example.myfinalproject_capstone.ui.staff.LetterAddActivity
import com.google.firebase.database.*

class StaffHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffHomeBinding
    private var list: ArrayList<Letter> = arrayListOf()
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")
    private var database: DatabaseReference? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStaffHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Letters")

        binding.rvNotes.setHasFixedSize(true)

        binding.fabAddLetter.setOnClickListener { view ->
            val intent = Intent(this@StaffHomeActivity, LetterAddActivity::class.java)
            startActivity(intent)
        }

        dataLetter()
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.topbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this@StaffHomeActivity, AccountActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    private fun dataLetter() {
        database?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (ds in snapshot.children) {
                        if(getUserID()?.equals(ds.child("staffID").value) == true) {
                            val user = ds.getValue(Letter::class.java)
                            list.add(user!!)
                        }
                    }
                    binding.rvNotes.adapter = ListLetterStaffAdapter(list)
                } else {

                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showRecyclerList() {
        binding.rvNotes.layoutManager = LinearLayoutManager(this)
        val listLetterAdapter = ListLetterStaffAdapter(list)
        binding.rvNotes.adapter = listLetterAdapter
    }

    private fun getUserID(): String? {
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

        return userId
    }
}