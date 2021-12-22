package com.example.myfinalproject_capstone.ui.staff.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfinalproject_capstone.databinding.ActivityDetailLetterBinding
import com.google.firebase.database.*
import android.content.Intent
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import com.example.myfinalproject_capstone.entity.Letter
import com.example.myfinalproject_capstone.ui.staff.LetterAddActivity
import java.text.SimpleDateFormat
import java.util.*


class DetailLetterActivity : AppCompatActivity(){

    private lateinit var binding: ActivityDetailLetterBinding
    private var database: DatabaseReference? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")


    companion object {
        const val EXTRA_LETTER = "extra_price"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Letters")

        binding = ActivityDetailLetterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDetailLetter()

        binding.btnEdit.setOnClickListener { editLetter() }
        binding.btnDelete.setOnClickListener { deleteLetter() }
        binding.btnClose.setOnClickListener { backToHome() }
    }

    private fun backToHome() {
        val intent = Intent(this@DetailLetterActivity, StaffHomeActivity::class.java)
        startActivity(intent)
    }


    private fun deleteLetter() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Letter")
        builder.setMessage("You Want Delete This Letter? ")
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            val letter = database?.child(intent.getStringExtra(EXTRA_LETTER).toString())
            letter?.removeValue()

            Toast.makeText(applicationContext, "Berhasil Dihapus", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@DetailLetterActivity, StaffHomeActivity::class.java)
            startActivity(intent)
        }
        builder.setNegativeButton(
            "No") { dialog, id ->

        }
        builder.show()
    }

    private fun editLetter() {
        val newDescription = binding.edtDescription.text.toString().trim()
        val newTypeLetter: String = binding.edtTypeLeave.text.toString().trim()
        val newStartDate: String = binding.edtStartDatePicker.text.toString().trim()
        val newFinishDate: String = binding.edtEndDatePicker.text.toString().trim()

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Letter")
        builder.setMessage("You Want to Update This Letter? ")
        builder.setPositiveButton(
            "Yes") { dialog, id ->
            if (newDescription.isEmpty()) {
                binding.edtTypeLeave.error = "Field tidak boleh kosong"
            } else {
                val letterDB = database?.child(intent.getStringExtra(EXTRA_LETTER).toString())

                val letterID = intent.getStringExtra(EXTRA_LETTER).toString()

                val letter = Letter(letterID, getCurrentDate(), newTypeLetter, newDescription, getUserID(),
                    getCompanyID(), newStartDate, newFinishDate, "3")

                letterDB?.setValue(letter)

                Toast.makeText(applicationContext, "Berhasil Update", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@DetailLetterActivity, StaffHomeActivity::class.java)
                startActivity(intent)
            }
        }
        builder.setNegativeButton(
            "No") { dialog, id ->

        }
        builder.show()
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

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        )
        return sdf.format(Date())
    }

    private fun getCompanyID(): String? {
        var userCompanyId: String? = null
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getCompanyID().observe(this,
            { userCompanyID: String ->
                userCompanyId = userCompanyID
            }
        )

        return userCompanyId
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

