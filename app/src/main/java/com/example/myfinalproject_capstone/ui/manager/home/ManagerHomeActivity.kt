package com.example.myfinalproject_capstone.ui.manager.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myfinalproject_capstone.ui.AccountActivity
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.adapter.ListLetterManagerAdapter
import com.example.myfinalproject_capstone.databinding.ActivityManagerHomeBinding
import com.example.myfinalproject_capstone.entity.Letter
import com.example.myfinalproject_capstone.ui.manager.MyDialogHelp
import com.google.firebase.database.*

class ManagerHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityManagerHomeBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")
    private var list: ArrayList<Letter> = arrayListOf()
    private var database: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityManagerHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Letters")

        binding.rvNotesManager.setHasFixedSize(true)

        binding.fabHelp.setOnClickListener { view ->
            MyDialogHelp().show(supportFragmentManager, "mydialog")
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
        val intent = Intent(this@ManagerHomeActivity, AccountActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    private fun getCompanyID(): String {
        val userCompanyId = "123"

//        Nunggu Bima Edit login dulu soalnya belum kesimpen di data store untuk data akunnya

//        val pref = SettingPreferences.getInstance(dataStore)
//        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
//            MainViewModel::class.java
//        )
//
//        mainViewModel.getCompanyID().observe(this,
//            { userCompanyID: String ->
//                userCompanyId = userCompanyID
//            }
//        )

        return userCompanyId
    }

    private fun dataLetter() {
        database?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (ds in snapshot.children) {
                        if(getCompanyID().equals(ds.child("companyID").value) == true) {
                            val user = ds.getValue(Letter::class.java)
                            list.add(user!!)
                        }
                    }
                    binding.rvNotesManager.adapter = ListLetterManagerAdapter(list)
                } else {
                    Toast.makeText(applicationContext, "Data Not Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun showRecyclerList() {
        binding.rvNotesManager.layoutManager = LinearLayoutManager(this)
        val listLetterManagerAdapter = ListLetterManagerAdapter(list)
        listLetterManagerAdapter.notifyDataSetChanged() // Masih Belum Bisa
        binding.rvNotesManager.adapter = listLetterManagerAdapter
    }
}