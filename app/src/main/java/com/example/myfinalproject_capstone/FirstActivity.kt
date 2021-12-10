package com.example.myfinalproject_capstone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.myfinalproject_capstone.databinding.ActivityFirstBinding

class FirstActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityFirstBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(this)
        binding.btnSignupCompany.setOnClickListener(this)
        binding.btnSignupUser.setOnClickListener(this)
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
}
