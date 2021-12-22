package com.example.myfinalproject_capstone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myfinalproject_capstone.entity.DataUsers
import com.example.myfinalproject_capstone.databinding.ActivitySignupBinding
import com.example.myfinalproject_capstone.ui.staff.home.StaffHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivitySignupBinding? = null
    private var database: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.btnSignUp.setOnClickListener(this)

        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        if(register()) {
            val msg_email: String = binding?.etEmail?.text.toString().trim()
            val msg_password: String = binding?.etPassword?.text.toString().trim()
            val msg_code: String = binding?.etCode?.text.toString().trim()

            if (msg_email.isEmpty()) {
                binding?.etEmail?.error = FIELD_REQUIRED
                return
            }
            if (msg_password.isEmpty()){
                binding?.etPassword?.error = FIELD_REQUIRED
                return
            }
            if (msg_code.isEmpty()) {
                binding?.etCode?.error = FIELD_REQUIRED
                return
            }

            if (isValidEmail(msg_email)) {
                try {
                    connectDatabase(msg_email, msg_password, msg_code)
                } catch (ex: Exception) {
                    Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding?.etEmail?.error = FIELD_IS_NOT_VALID
                return
            }
        }
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun connectDatabase(msg_email: String, msg_password: String, msg_code: String) {
        try {
            database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users")

            val idUsers = database!!.push().key
            val position = "Staff"

            val User = DataUsers(idUsers, msg_email, msg_password, msg_code, position)
            if (idUsers != null) {
                database!!.child(idUsers).setValue(User).addOnCompleteListener {
                    val moveIntent = Intent(this@SignupActivity, StaffHomeActivity::class.java)
                    moveIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // clears current and previous activity stack
                    startActivity(moveIntent)
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Failed to Save", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(applicationContext, "Failed in idUsers", Toast.LENGTH_SHORT).show()
            }
        } catch (ex: Exception) {
            Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun register(): Boolean {
        val email : String = binding?.etEmail?.text.toString().trim().lowercase()
        val password : String = binding?.etPassword?.text.toString().trim()
        var checkEmail = false
        if(email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.createUserWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                        true.also { checkEmail = it }
                    }
                } catch(e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "ERROR: Account Already Exists!", Toast.LENGTH_LONG).show()
                        false.also { checkEmail = it }
                    }
                }
            }
        }
        return checkEmail
    }

    private fun checkLoggedInState() {
        if(auth.currentUser == null) {
            Toast.makeText(applicationContext, "You Are Not Logged In", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "You Are Logged In", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        database = null
    }
}