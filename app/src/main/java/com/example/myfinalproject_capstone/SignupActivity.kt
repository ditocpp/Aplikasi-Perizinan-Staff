package com.example.myfinalproject_capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myfinalproject_capstone.entity.DataUsers
import com.example.myfinalproject_capstone.databinding.ActivitySignupBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var database: DatabaseReference
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val msg_email: String = binding.etEmail.text.toString()
        val msg_password: String = binding.etPassword.text.toString()
        val msg_code: String = binding.etCode.text.toString()

        if (msg_email.trim().isEmpty()) {
            binding.etEmail.error = "Required"
            Toast.makeText(applicationContext, "User Name Required", Toast.LENGTH_SHORT).show()
        }
        if (msg_password.trim().isEmpty()) {
            binding.etPassword.error = "Required"
            Toast.makeText(applicationContext, "Password Required", Toast.LENGTH_SHORT).show()
        }
        if (msg_code.trim().isEmpty()) {
            binding.etCode.error = "Required"
            Toast.makeText(applicationContext, "Code Required", Toast.LENGTH_SHORT).show()
        }

        if (msg_email.matches(emailPattern.toRegex())) {
            connectDatabase(msg_email, msg_password, msg_code)
        } else {
            binding.etEmail.error = "@gmail.com"
            Toast.makeText(applicationContext, "email not valid", Toast.LENGTH_SHORT).show()
        }
    }

    private fun connectDatabase(msg_email: String, msg_password: String, msg_code: String) {
        try {
            database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users")

            val idUsers = database.push().key
            val position = "Staff"

            val User = DataUsers(idUsers, msg_email, msg_password, msg_code, position)
            if (idUsers != null) {
                database.child(idUsers).setValue(User).addOnCompleteListener {
                    val moveIntent = Intent(this@SignupActivity, StaffHomeActivity::class.java)
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
}

