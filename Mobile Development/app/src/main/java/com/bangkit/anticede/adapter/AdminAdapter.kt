package com.bangkit.anticede.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.anticede.R
import com.bangkit.anticede.model.User

class AdminAdapter(private val userList: ArrayList<User>): RecyclerView.Adapter<AdminAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var username: TextView = itemView.findViewById(R.id.tv_item_name)
        var age: TextView = itemView.findViewById(R.id.tv_item_age)
    }

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ) = ListViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false))

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (username, age) = userList[position]
        holder.username.text = username
        holder.age.text = age

        holder.itemView.setOnClickListener { onItemClickCallback.onItemClicked(userList[holder.adapterPosition]) }
    }

    override fun getItemCount(): Int = userList.size

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: User)
    }
}