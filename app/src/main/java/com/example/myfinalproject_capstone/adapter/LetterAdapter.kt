package com.example.myfinalproject_capstone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinalproject_capstone.databinding.ItemLetterBinding
import com.example.myfinalproject_capstone.entity.Letter
import kotlin.collections.ArrayList

class LetterAdapter(private val listSurat: ArrayList<Letter>) : RecyclerView.Adapter<LetterAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ListViewHolder(var binding: ItemLetterBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemLetterBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (inputDate, title, description, staffName, duration, status, statusImage ) = listSurat[position]

        holder.binding.apply {
            tvItemDate.text = inputDate.toString()
            tvPermit.text = title
            tvItemDescription.text = description
            tvNameStaff.text = staffName
            tvPermitDuration.text = duration.toString()
            when {
                status===1 -> {
                    TODO("APPROVED")
                }
                status===2 -> {
                    TODO("REJECTED")
                }
                else -> {
                    TODO("PENDING")
                }
            }
            TODO("implement callback function")
        }
    }

    override fun getItemCount(): Int = listSurat.size

    interface OnItemClickCallback {
        fun OnItemClicked(data: Letter)
    }
}