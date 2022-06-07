package com.bangkit.anticede.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.anticede.api.response.GetAllUserResponseItem
import com.bangkit.anticede.databinding.ItemRowUserBinding

class AdminAdapter(private val listUser: ArrayList<GetAllUserResponseItem>) :
    RecyclerView.Adapter<AdminAdapter.UserViewHolder>() {
    private var onItemClickCallback: AdminAdapter.OnItemClickCallback? = null

    inner class UserViewHolder(private val binding: ItemRowUserBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(getAllUserResponseItem: GetAllUserResponseItem) {
            binding.root.setOnClickListener {
                onItemClickCallback?.onItemClicked(getAllUserResponseItem)
            }

            binding.apply {
                tvItemName.text = getAllUserResponseItem.username
                tvItemAge.text = getAllUserResponseItem.age.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdminAdapter.UserViewHolder {
        val view = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder((view))
    }

    override fun onBindViewHolder(holder: AdminAdapter.UserViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount() = listUser.size

    interface OnItemClickCallback {
        fun onItemClicked(data: GetAllUserResponseItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}