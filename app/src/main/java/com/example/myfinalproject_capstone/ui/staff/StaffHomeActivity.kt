package com.example.myfinalproject_capstone.ui.staff

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.myfinalproject_capstone.AccountActivity
import com.example.myfinalproject_capstone.LetterAddActivity
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.databinding.ActivityStaffHomeBinding

class StaffHomeActivity : AppCompatActivity() {

    private var binding: ActivityStaffHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStaffHomeBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.fabAddLetter.setOnClickListener { view ->
            val intent = Intent(this@StaffHomeActivity, LetterAddActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
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
}