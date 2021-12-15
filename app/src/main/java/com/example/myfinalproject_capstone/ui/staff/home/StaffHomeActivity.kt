package com.example.myfinalproject_capstone.ui.staff.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.myfinalproject_capstone.AccountActivity
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.databinding.ActivityStaffHomeBinding
import com.example.myfinalproject_capstone.entity.Letter
import com.example.myfinalproject_capstone.ui.manager.home.ManagerHomeActivity
import com.example.myfinalproject_capstone.ui.staff.LetterAddActivity
import com.google.firebase.database.*

class StaffHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStaffHomeBinding
    private var list: ArrayList<Letter> = arrayListOf()
    private var database: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStaffHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")

        binding.rvNotes.setHasFixedSize(true)

        binding.fabAddLetter.setOnClickListener { view ->
            val intent = Intent(this@StaffHomeActivity, LetterAddActivity::class.java)
            startActivity(intent)
        }
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

                    }
                } else {
//                    Toast.makeText(applicationContext, "Email do Not Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }
}