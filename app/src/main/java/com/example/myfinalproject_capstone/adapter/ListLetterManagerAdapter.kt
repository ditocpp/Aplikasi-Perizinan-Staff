package com.example.myfinalproject_capstone.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.databinding.ItemLetterBinding
import com.example.myfinalproject_capstone.entity.Letter
import com.example.myfinalproject_capstone.ui.manager.home.DetailLetterManagerActivity

class ListLetterManagerAdapter(private val listSurat: ArrayList<Letter>) : RecyclerView.Adapter<ListLetterManagerAdapter.ListViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemLetterBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val letter = listSurat[position]
        holder.bin(letter)
    }

    override fun getItemCount(): Int = listSurat.size

    inner class ListViewHolder(var binding: ItemLetterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bin(letter: Letter) {
            with(binding) {
                tvItemDate.text = letter.inputDate.toString()
                tvPermit.text = letter.title
                tvItemDescription.text = letter.description
                tvPermitDurationStart.text = letter.durationStart
                tvPermitDurationFinish.text = letter.durationFinish
                when {
                    letter.status.equals("0") -> {
                        tvPermitStatus.setText(R.string.approved)
                        imgStatus.setColorFilter(R.color.green)
                        imgStatus.setImageResource(R.drawable.ic_baseline_check_24)
                    }
                    letter.status.equals("1") -> {
                        tvPermitStatus.setText(R.string.rejected)
                        imgStatus.setColorFilter(R.color.red)
                        imgStatus.setImageResource(R.drawable.ic_baseline_close_24)
                    }
                    else -> {
                        tvPermitStatus.setText(R.string.pending)
                        imgStatus.setColorFilter(R.color.purple_200)
                        imgStatus.setImageResource(R.drawable.ic_baseline_access_time_24)
                    }
                }
                itemView.setOnClickListener{
                    val intent = Intent(itemView.context, DetailLetterManagerActivity::class.java)
                    intent.putExtra(DetailLetterManagerActivity.EXTRA_LETTER, letter.letterID)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}