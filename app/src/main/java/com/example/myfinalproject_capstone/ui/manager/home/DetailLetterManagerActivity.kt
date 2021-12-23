package com.example.myfinalproject_capstone.ui.manager.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.*
import android.content.Intent
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.databinding.ActivityDetailLetterManagerBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import com.example.myfinalproject_capstone.entity.Letter
import com.example.myfinalproject_capstone.ui.manager.MyDialogHelp
import com.example.myfinalproject_capstone.ui.staff.home.DetailLetterActivity
import com.example.myfinalproject_capstone.ui.staff.home.StaffHomeActivity
import java.text.SimpleDateFormat
import java.util.*


class DetailLetterManagerActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailLetterManagerBinding
    private var database: DatabaseReference? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")
    private lateinit var newDescription: String
    private lateinit var newTypeLetter: String
    private lateinit var newStartDate: String
    private lateinit var newFinishDate: String
    private lateinit var noted: String

    private lateinit var staffID: String


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

        binding.btnApproved.setOnClickListener { noteAppproved() }
        binding.btnRejected.setOnClickListener { noteRejected() }
        binding.btnClose.setOnClickListener { backToHome() }
    }

    private fun noteRejected() {
        newDescription = binding.edtDescription.text.toString().trim()
        newTypeLetter = binding.edtTypeLeave.text.toString().trim()
        newStartDate = binding.edtStartDatePicker.text.toString().trim()
        newFinishDate = binding.edtEndDatePicker.text.toString().trim()
        noted = binding.noted.text.toString().trim()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Letter")
        builder.setMessage("You Want to Reject This Letter? ")
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            if (noted.isEmpty()) {
                binding.noted.error = "Field tidak boleh kosong"
            } else {
                val letterDB = database?.child(intent.getStringExtra(DetailLetterActivity.EXTRA_LETTER).toString())

                val letterID = intent.getStringExtra(DetailLetterActivity.EXTRA_LETTER).toString()

                val letter = Letter(letterID, getCurrentDate(), newTypeLetter, newDescription, staffID,
                    getCompanyID(), newStartDate, newFinishDate, noted, "1")

                letterDB?.setValue(letter)

                Toast.makeText(applicationContext, "Berhasil Update", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@DetailLetterManagerActivity, ManagerHomeActivity::class.java)
                startActivity(intent)
            }
        }
        builder.setNegativeButton(
            "No") { dialog, id ->

        }
        builder.show()
    }

    private fun noteAppproved() {
        newDescription = binding.edtDescription.text.toString().trim()
        newTypeLetter = binding.edtTypeLeave.text.toString().trim()
        newStartDate = binding.edtStartDatePicker.text.toString().trim()
        newFinishDate = binding.edtEndDatePicker.text.toString().trim()
        noted = binding.noted.text.toString().trim()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Letter")
        builder.setMessage("You Want to Update This Letter? ")
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            if (noted.isEmpty()) {
                binding.noted.error = "Field tidak boleh kosong"
            } else {
                val letterDB = database?.child(intent.getStringExtra(DetailLetterActivity.EXTRA_LETTER).toString())

                val letterID = intent.getStringExtra(DetailLetterActivity.EXTRA_LETTER).toString()

                val letter = Letter(letterID, getCurrentDate(), newTypeLetter, newDescription, staffID,
                    getCompanyID(), newStartDate, newFinishDate, noted, "0")

                letterDB?.setValue(letter)

                Toast.makeText(applicationContext, "Berhasil Update", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@DetailLetterManagerActivity, ManagerHomeActivity::class.java)
                startActivity(intent)
            }
        }
        builder.setNegativeButton(
            "No") { dialog, id ->

        }
        builder.show()
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
                            staffID = ds.child("staffID").value as String
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
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

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        )
        return sdf.format(Date())
    }
}

