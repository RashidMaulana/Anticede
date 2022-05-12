package com.bangkit.anticede.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.anticede.databinding.ItemRowMemberBinding
import com.bangkit.anticede.model.TeamMember
import com.bumptech.glide.Glide


class AboutAdapter(private val listMember: ArrayList<TeamMember>): RecyclerView.Adapter<AboutAdapter.MemberViewHolder>() {
    inner class MemberViewHolder(private val binding: ItemRowMemberBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(teamMember: TeamMember){
            binding.apply {
                Glide.with(itemView)
                    .load(teamMember.photo)
                    .circleCrop()
                    .into(imgItemPhoto)
                tvItemName.text = teamMember.name
                tvItemId.text = teamMember.id
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AboutAdapter.MemberViewHolder {
        val view = ItemRowMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberViewHolder((view))
    }

    override fun onBindViewHolder(holder: AboutAdapter.MemberViewHolder, position: Int) {
        holder.bind(listMember[position])
    }

    override fun getItemCount(): Int = listMember.size
}