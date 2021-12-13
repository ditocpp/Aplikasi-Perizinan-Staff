package com.example.myfinalproject_capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfinalproject_capstone.databinding.ActivityStaffHomeBinding
import com.google.android.material.snackbar.Snackbar

class StaffHomeActivity : AppCompatActivity() {

    private var binding: ActivityStaffHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStaffHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.fabAdd.setOnClickListener { view ->
            val intent = Intent(this@StaffHomeActivity, LetterAddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}