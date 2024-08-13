package com.example.filkomsoul.ui.profile.archive

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.filkomsoul.data.local.ArchiveEntity
import com.example.filkomsoul.databinding.ItemArchiveBinding
import kotlin.math.roundToInt

class ArchiveAdapter: ListAdapter<ArchiveEntity, ArchiveAdapter.ListViewHolder>(DIFF_CALLBACK) {

    class ListViewHolder (var binding: ItemArchiveBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ArchiveEntity) {
            binding.progress.text = user.total.toString()
            binding.progressbar.progress = (user.total*100/150f).roundToInt()
            binding.date.text = user.date
            binding.summary.text = user.result
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ItemArchiveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    companion object {
        val DIFF_CALLBACK: DiffUtil.ItemCallback<ArchiveEntity> =
            object : DiffUtil.ItemCallback<ArchiveEntity>() {
                override fun areItemsTheSame(oldUser: ArchiveEntity, newUser: ArchiveEntity): Boolean {
                    return oldUser.outputId == newUser.outputId
                }

                @SuppressLint("DiffUtilEquals")
                override fun areContentsTheSame(oldUser: ArchiveEntity, newUser: ArchiveEntity): Boolean {
                    return oldUser == newUser
                } }
    }
}