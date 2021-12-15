package com.example.myfinalproject_capstone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.databinding.ItemLetterStaffBinding
import com.example.myfinalproject_capstone.entity.Letter

class ListLetterStaffAdapter(private val listSurat: ArrayList<Letter>) : RecyclerView.Adapter<ListLetterStaffAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: LetterAdapter.OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: LetterAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemLetterStaffBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (letterID, inputDate, title, description, staffID, companyID, durationStart, durationFinish, status) = listSurat[position]

        holder.binding.apply {
            tvItemDate.text = inputDate.toString()
            tvPermit.text = title
            when {
                status === 0 -> {
                    TODO("APPROVED")
                    tvPermitStatus.setText(R.string.approved)
                    imgStatus.setImageResource(R.drawable.ic_baseline_check_24)
                }
                status === 1 -> {
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

    inner class ListViewHolder(var binding: ItemLetterStaffBinding) : RecyclerView.ViewHolder(binding.root)
}