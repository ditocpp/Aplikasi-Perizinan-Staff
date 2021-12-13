package com.example.myfinalproject_capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myfinalproject_capstone.entity.DataUsers
import com.example.myfinalproject_capstone.databinding.ActivitySignupCompanyBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupCompanyActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivitySignupCompanyBinding? = null
    private var database: DatabaseReference? = null
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCompanyBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")

        binding!!.btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val msg_email: String = binding?.etEmail?.text.toString()
        val msg_password: String = binding?.etPassword?.text.toString()

        if (msg_email.trim().isEmpty()) {
            binding?.etEmail?.error = "Required"
            Toast.makeText(applicationContext, "User Name Required ", Toast.LENGTH_SHORT).show()
        }
        if (msg_password.trim().isEmpty()){
            binding?.etPassword?.error = "Required"
            Toast.makeText(applicationContext, "Password Required ", Toast.LENGTH_SHORT).show()
        }
        if (msg_email.matches(emailPattern.toRegex())) {
            connectDatabase(msg_email, msg_password)
        } else {
            binding?.etEmail?.error = "@gmail.com"
            Toast.makeText(applicationContext, "email not valid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectDatabase(msg_email: String, msg_password: String) {
        try {
            database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users")

            val idUsers = database!!.push().key
            val position = "Manager"
            val msg_code = "00001"


            val User = DataUsers(idUsers, msg_email, msg_password, msg_code, position)
            if (idUsers != null) {
                database!!.child(idUsers).setValue(User).addOnCompleteListener {
                    val moveIntent = Intent(this@SignupCompanyActivity, StaffHomeActivity::class.java)
                    startActivity(moveIntent)
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Failed Saved", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Failed in idUsers", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: Exception) {
            Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        database = null
    }
}