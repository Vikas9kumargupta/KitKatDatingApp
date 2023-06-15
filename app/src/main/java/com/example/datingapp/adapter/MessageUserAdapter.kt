package com.example.datingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.datingapp.databinding.FragmentProfileBinding
import com.example.datingapp.databinding.UserItemLayoutBinding
import com.example.datingapp.model.UserModel

class MessageUserAdapter(val context : Context, private val list : ArrayList<UserModel>)
    : RecyclerView.Adapter<MessageUserAdapter.MessageUserViewHolder>() {
    inner class MessageUserViewHolder(val binding: UserItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageUserViewHolder {
        return MessageUserViewHolder(UserItemLayoutBinding.inflate(LayoutInflater.from(context),parent, false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageUserViewHolder, position: Int) {
        Glide.with(context).load(list[position].image).into(holder.binding.userImage1)

        holder.binding.userName.text = list[position].name
    }
}