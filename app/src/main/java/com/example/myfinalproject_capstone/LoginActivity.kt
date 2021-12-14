package com.example.myfinalproject_capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myfinalproject_capstone.databinding.ActivityLoginBinding
import com.google.firebase.database.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivityLoginBinding? = null
    private var database: DatabaseReference? = null

    companion object {
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")

        binding!!.btnLogin.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        val msg_email: String = binding?.etEmail?.text.toString().trim()
        val msg_password: String = binding?.etPassword?.text.toString().trim()

        if (msg_email.isEmpty()) {
            binding?.etEmail?.error = FIELD_REQUIRED
            return
        }
        if (msg_password.isEmpty()){
            binding?.etPassword?.error = FIELD_REQUIRED
            return
        }

        if (isValidEmail(msg_email)) {
            try {
                checkEmail(msg_email, msg_password)
            } catch (ex: Exception) {
                Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
            }
        } else {
            binding?.etEmail?.error = FIELD_IS_NOT_VALID
            return
        }
    }
    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkEmail(msg_email: String, msg_password: String) {
        database?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for (ds in snapshot.children) {
                        if (msg_email.equals(ds.child("email").value)
                            && msg_password.equals(ds.child("password").value)
                            && ds.child("position").value == "Staff") {
                            val moveIntent = Intent(this@LoginActivity, StaffHomeActivity::class.java)
                            startActivity(moveIntent)
                        } else if (msg_email.equals(ds.child("email").value)
                            && msg_password.equals(ds.child("password").value)
                            && ds.child("position").value == "Manager"){
                            val moveIntent = Intent(this@LoginActivity, ManagerHomeActivity::class.java)
                            startActivity(moveIntent)
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Email do Not Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        database = null
    }
}