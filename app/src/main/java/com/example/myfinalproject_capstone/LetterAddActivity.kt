package com.example.myfinalproject_capstone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.myfinalproject_capstone.databinding.ActivityLetterAddBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class LetterAddActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLetterAddBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLetterAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val typeLeaves = resources.getStringArray(R.array.item_dropdown)
        val arrayAdapter = ArrayAdapter(this, R.layout.item_drop_down, typeLeaves)
        binding.edtTypeLeave.setAdapter(arrayAdapter)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnShowDateRangePicker.setOnClickListener{
            showDateRangePicker()
        }
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
                binding.edtStartDatePicker.text = convertLongToDate(startDate)
                binding.edtEndDatePicker.text = convertLongToDate(endDate)
            }
        }
    }

    private fun convertLongToDate(time: Long): String? {
        val date = Date(time)
        val format = SimpleDateFormat (
            "dd-MM-yyyy",
            Locale.getDefault()
        )
        return format.format(date)
    }
}

