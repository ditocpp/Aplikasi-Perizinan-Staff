package com.example.myfinalproject_capstone.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.databinding.ActivityFirstBinding
import com.google.firebase.auth.FirebaseAuth

class FirstActivity : AppCompatActivity(), View.OnClickListener {

    private var binding: ActivityFirstBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        //checkLoggedInState()

        binding!!.btnLogin.setOnClickListener(this)
        binding!!.btnSignupCompany.setOnClickListener(this)
        binding!!.btnSignupUser.setOnClickListener(this)
    }



    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.btn_Login -> {
                val moveIntent = Intent(this@FirstActivity, LoginActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.btn_SignupCompany -> {
                val moveIntent = Intent(this@FirstActivity, SignupCompanyActivity::class.java)
                startActivity(moveIntent)
            }
            R.id.btn_SignupUser -> {
                val moveIntent = Intent(this@FirstActivity, SignupActivity::class.java)
                startActivity(moveIntent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
