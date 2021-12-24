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
import com.example.myfinalproject_capstone.databinding.ActivityLoginBinding
import com.example.myfinalproject_capstone.datastore.MainViewModel
import com.example.myfinalproject_capstone.datastore.SettingPreferences
import com.example.myfinalproject_capstone.datastore.ViewModelFactory
import com.example.myfinalproject_capstone.ui.manager.home.ManagerHomeActivity
import com.example.myfinalproject_capstone.ui.staff.home.StaffHomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class
LoginActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivityLoginBinding? = null
    private var database: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataUser")

    companion object {
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_WRONG_EMAIL_PASS = "E-mail/Password salah atau tidak terdaftar"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.btnLogin.setOnClickListener(this)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Users")

        auth = FirebaseAuth.getInstance()
    }

    override fun onClick(v: View?) {
        //login()
        val msg_email: String = binding?.etEmail?.text.toString().trim().lowercase()
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

                                datastore(ds.child("idUser").value as String, ds.child("name").value as String, ds.child("email").value as String,
                                    ds.child("password").value as String, ds.child("codeCompany").value as String,
                                    ds.child("position").value as String)

                            val moveIntent = Intent(this@LoginActivity, StaffHomeActivity::class.java)
                            moveIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // clears current and previous activity stack
                            startActivity(moveIntent)
                        } else if (msg_email.equals(ds.child("email").value)
                            && msg_password.equals(ds.child("password").value)
                            && ds.child("position").value == "Manager"){

                            datastore(ds.child("idUser").value as String, ds.child("name").value as String, ds.child("email").value as String,
                                ds.child("password").value as String, ds.child("codeCompany").value as String,
                                ds.child("position").value as String)

                            val moveIntent = Intent(this@LoginActivity, ManagerHomeActivity::class.java)
                            moveIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // clears current and previous activity stack
                            startActivity(moveIntent)
                        } else {
                            binding?.etEmail?.error = FIELD_WRONG_EMAIL_PASS
                            binding?.etPassword?.error = FIELD_WRONG_EMAIL_PASS
                        }
                    }
                } else {
                    Toast.makeText(applicationContext, "Email does Not Exist", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Connection Failed", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun datastore(
        id: String,
        name:String,
        email: String,
        password: String,
        codeCompany: String,
        position: String
    ) {
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.saveUserSetting(id, name, email, password, codeCompany, position)
    }
/*
    private fun login() {
        val email : String = binding?.etEmail?.text.toString().trim().lowercase()
        val password : String = binding?.etPassword?.text.toString().trim()
        if(email.isNotEmpty() && password.isNotEmpty()) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    auth.signInWithEmailAndPassword(email, password).await()
                    withContext(Dispatchers.Main) {
                        checkLoggedInState()
                    }
                } catch(e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(applicationContext, "Error: User Not Found!", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun checkLoggedInState() {
        if(auth.currentUser == null) {
            Toast.makeText(applicationContext, "You Are Not Logged In", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(applicationContext, "You Are Logged In", Toast.LENGTH_LONG).show()
        }
    }
*/
    override fun onDestroy() {
        super.onDestroy()
        database = null
    }
}