package com.example.myfinalproject_capstone.ui.staff

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.databinding.ActivityLetterAddBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import com.example.myfinalproject_capstone.entity.Letter
import com.example.myfinalproject_capstone.ui.staff.home.StaffHomeActivity
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class LetterAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterAddBinding
    private var database: DatabaseReference? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLetterAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeLeaves = resources.getStringArray(R.array.item_dropdown)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_drop_down, typeLeaves)
        binding.edtTypeLeave.setAdapter(arrayAdapter)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnShowDateRangePicker.setOnClickListener{ showDateRangePicker() }
        binding.btnSubmit.setOnClickListener{ addDataToDB() }
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder
            .dateRangePicker()
            .setTitleText("Select date")
            .build()
        dateRangePicker.show(
            supportFragmentManager,
            "date_range_picker"
        )
        dateRangePicker.addOnPositiveButtonClickListener { datePicked ->
            val startDate = datePicked.first
            val endDate = datePicked.second

            if (startDate != null && endDate != null) {
                binding.edtStartDatePicker.setText(convertLongToDate(startDate))
                binding.edtEndDatePicker.setText(convertLongToDate(endDate))
            }
        }
    }

    private fun convertLongToDate(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat (
            "dd-MM-yyyy",
            Locale.getDefault()
        )
        return format.format(date)
    }

    private fun addDataToDB() {

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Letters")

        if(!isEmptyField()) {
            val letterID = database!!.push().key
            val inputDate = getCurrentDate()
            val title = binding.edtTypeLeave.text.toString().trim()
            val description = binding.edtDescription.text.toString().trim()
            val staffID = getUserID()
            val companyID = getCompanyID()
            val durationStart = binding.edtStartDatePicker.text.toString().trim()
            val durationFinish = binding.edtEndDatePicker.text.toString().trim()
            val status = "3"

            val letter = Letter(letterID, inputDate, title, description, staffID, companyID, durationStart, durationFinish, "", status)
            try {
                if (letterID != null) {
                    database!!.child(letterID).setValue(letter).addOnCompleteListener {
                        val intent = Intent(this@LetterAddActivity, StaffHomeActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(applicationContext, "Berhasil Disimpan", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(applicationContext, "Failed Saved", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Failed in idUsers", Toast.LENGTH_SHORT).show()
                }
            } catch (ex : Exception) {
                Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun isEmptyField(): Boolean {

        var status = false

        val msg_TypeLetter: String = binding.edtTypeLeave.text.toString().trim()
        val msg_Description: String = binding.edtDescription.text.toString().trim()
        val msg_StartDate: String = binding.edtStartDatePicker.text.toString().trim()
        val msg_FinishDate: String = binding.edtEndDatePicker.text.toString().trim()

        if (msg_TypeLetter.isEmpty()) {
            binding.edtTypeLeave.error = FIELD_REQUIRED
            status = true
        } else {
            binding.edtTypeLeave.error = null
        }
        if (msg_Description.isEmpty()){
            binding.edtDescription.error = FIELD_REQUIRED
            status = true
        } else {
            binding.edtTypeLeave.error = null
        }
        if (msg_StartDate.isEmpty()){
            binding.edtStartDatePicker.error = FIELD_REQUIRED
            status = true
        } else {
            binding.edtTypeLeave.error = null
        }
        if (msg_FinishDate.isEmpty()){
            binding.edtEndDatePicker.error = FIELD_REQUIRED
            status = true
        } else {
            binding.edtTypeLeave.error = null
        }
        return status
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

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat(
            "dd-MM-yyyy",
            Locale.getDefault()
        )
        return sdf.format(Date())
    }

    companion object {
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
    }
}

