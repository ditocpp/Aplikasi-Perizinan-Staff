package com.example.myfinalproject_capstone.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myfinalproject_capstone.R
import com.example.myfinalproject_capstone.databinding.ItemLetterStaffBinding
import com.example.myfinalproject_capstone.entity.Letter

class ListLetterStaffAdapter(private val listSurat: ArrayList<Letter>) : RecyclerView.Adapter<ListLetterStaffAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemLetterStaffBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val letter = listSurat[position]
        holder.bin(letter)
    }

    override fun getItemCount(): Int = listSurat.size

    inner class ListViewHolder(var binding: ItemLetterStaffBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bin(letter: Letter) {
            with(binding) {
                tvItemDate.text = letter.inputDate.toString()
                tvPermit.text = letter.title
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
                        imgStatus.setColorFilter(R.color.yellow)
                        imgStatus.setImageResource(R.drawable.ic_baseline_access_time_24)
                        }
                    }
                itemView.setOnClickListener{
                    //Deprecated
                    onItemClickCallback.onItemClicked(listSurat[adapterPosition])
                }
            }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Letter)
    }
}