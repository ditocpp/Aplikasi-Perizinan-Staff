package com.example.myfinalproject_capstone.ui.staff.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myfinalproject_capstone.databinding.ActivityDetailLetterDialogBinding
import com.google.firebase.database.*

class DetailLetterDialog : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLetterDialogBinding
    private var database: DatabaseReference? = null

    companion object {
        const val EXTRA_LETTER = "extra_price"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = FirebaseDatabase.getInstance("https://capstone-dicoding-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("Letters")

        binding = ActivityDetailLetterDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showDetailLetter()

        database?.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        if (intent.getStringExtra(EXTRA_LETTER) == ds.child("letterID").value as String){
                            binding.edtTypeLeave.setText(ds.child("title").value as String)
                            binding.edtDescription.setText(ds.child("description").value as String)
                            binding.edtStartDatePicker.setText(ds.child("durationStart").value as String)
                            binding.edtEndDatePicker.setText(ds.child("durationFinish").value as String)
                            Toast.makeText(this@DetailLetterDialog, "Kamu memilih " + ds.child("title").value as String, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun showDetailLetter() {

    }
}

