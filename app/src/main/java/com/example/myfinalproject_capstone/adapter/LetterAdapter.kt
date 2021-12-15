package com.example.myfinalproject_capstone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinalproject_capstone.R
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
        val (inputDate, title, description, staffName, durationStart, durationFinish, status) = listSurat[position]

        holder.binding.apply {
            tvItemDate.text = inputDate.toString()
            tvPermit.text = title
            tvItemDescription.text = description
            tvNameStaff.text = staffName
            tvPermitDurationStart.text = durationStart.toString()
            tvPermitDurationFinish.text = durationFinish.toString()
            when {
                status == 0 -> {
                    TODO("APPROVED")
                    tvPermitStatus.setText(R.string.approved)
                    imgStatus.setImageResource(R.drawable.ic_baseline_check_24)
                }
                status == 1 -> {
                    TODO("REJECTED")
                    tvPermitStatus.setText(R.string.rejected)
                    imgStatus.setImageResource(R.drawable.ic_baseline_close_24)
                }
                else -> {
                    TODO("PENDING")
                    tvPermitStatus.setText(R.string.pending)
                    imgStatus.setImageResource(R.drawable.ic_baseline_access_time_24)
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