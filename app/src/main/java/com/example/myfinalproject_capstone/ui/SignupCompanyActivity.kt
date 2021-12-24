package com.example.myfinalproject_capstone.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.myfinalproject_capstone.entity.DataUsers
import com.example.myfinalproject_capstone.databinding.ActivitySignupCompanyBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import com.example.myfinalproject_capstone.ui.manager.home.ManagerHomeActivity
import com.google.firebase.database.*

class SignupCompanyActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivitySignupCompanyBinding? = null
    private var database: DatabaseReference? = null
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")

    companion object {
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_WEAK_PASS = "Password harus lebih dari 6 karakter"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupCompanyBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")

        binding!!.btnSignUp.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val msg_name: String = binding?.etNameUser?.text.toString().trim()
        val msg_email: String = binding?.etEmail?.text.toString().trim()
        val msg_password: String = binding?.etPassword?.text.toString().trim()

        if (msg_name.isEmpty()) {
            binding?.etNameUser?.error = FIELD_REQUIRED
            return
        }

        if (msg_email.isEmpty()) {
            binding?.etEmail?.error = FIELD_REQUIRED
            return
        }
        if (msg_password.isEmpty()){
            binding?.etPassword?.error = FIELD_REQUIRED
            return
        }
        if (msg_password.length < 6){
            binding?.etPassword?.error = FIELD_WEAK_PASS
            return
        }

        if (isValidEmail(msg_email)) {
            try {
                connectDatabase(msg_name, msg_email, msg_password)
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

    private fun connectDatabase(msg_name: String, msg_email: String, msg_password: String) {
        try {
            database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("Users")

            val idUsers = database!!.push().key
            val position = "Manager"
            var randomNumber = (1..99999).random()
            var msg_code = String.format("%05d", randomNumber)

            database!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()) {
                            for(ds in snapshot.children) {
                                if (msg_code.equals(ds.child("codeCompany").value)) {
                                    randomNumber = (1..99999).random()
                                    msg_code = String . format ("%05d", randomNumber)
                                    break
                                }
                            }
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "Database Error!", Toast.LENGTH_LONG).show()
                }
            })

            val User = DataUsers(idUsers, msg_name, msg_email, msg_password, msg_code, position)
            if (idUsers != null) {
                database!!.child(idUsers).setValue(User).addOnCompleteListener {
                    datastore(idUsers, msg_name, msg_email, msg_password, msg_code, position)
                    val moveIntent = Intent(this@SignupCompanyActivity, ManagerHomeActivity::class.java)
                    moveIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // clears current and previous activity stack
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

    private fun datastore(id: String, name: String, email: String, password: String, codeCompany: String, position: String) {
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.saveUserSetting(id, name, email, password, codeCompany, position)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        database = null
    }
}