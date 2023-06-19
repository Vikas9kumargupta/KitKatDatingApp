package com.example.datingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.model.MessageModel
import com.example.datingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageAdapter(val context : Context,val list : List<MessageModel>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val MSG_TYPE_RIGHT = 0
    private val MSG_TYPE_LEFT = 0
    class MessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val text: TextView = itemView.findViewById<TextView>(R.id.messageText)
        val image: ImageView = itemView.findViewById<ImageView>(R.id.senderImage)
    }

    override fun getItemViewType(position: Int): Int {
        return if(list[position].senderId == FirebaseAuth.getInstance().currentUser!!.phoneNumber){
            MSG_TYPE_RIGHT
        } else MSG_TYPE_LEFT
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if(viewType == MSG_TYPE_RIGHT){
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_receiver_message,parent,false))
        }else{
            MessageViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_sender_message,parent,false))
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {

        holder.text.text = list[position].message

        FirebaseDatabase.getInstance().getReference("users").child(list[position].senderId!!)
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        val data = snapshot.getValue(UserModel::class.java)
                        Glide.with(context).load(data!!.image).placeholder(R.drawable.sonam).into(holder.image)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,error.message, Toast.LENGTH_SHORT).show()
                }

            })
    }
}