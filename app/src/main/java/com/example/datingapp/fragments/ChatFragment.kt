package com.example.datingapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.datingapp.R
import com.example.datingapp.adapter.MessageUserAdapter
import com.example.datingapp.databinding.FragmentChatBinding
import com.example.datingapp.fragments.DatingFragment.Companion.list

class ChatFragment : Fragment() {

    private lateinit var binding : FragmentChatBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(layoutInflater)

        binding.recyclerView.adapter = MessageUserAdapter(requireContext(), list!!)
        return binding.root
    }

}