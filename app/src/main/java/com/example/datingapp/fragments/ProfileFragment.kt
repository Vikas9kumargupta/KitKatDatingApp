package com.example.datingapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.datingapp.R
import com.example.datingapp.activity.EditProfile
import com.example.datingapp.auth.LoginActivity
import com.example.datingapp.databinding.FragmentProfileBinding
import com.example.datingapp.model.UserModel
import com.example.datingapp.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var dialog: AlertDialog
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        dialog = AlertDialog.Builder(requireContext()).setView(R.layout.loading_layout)
//            .setCancelable(false)
//            .create()

        Config.showDialog(requireContext())
        binding = FragmentProfileBinding.inflate(layoutInflater)

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!).get()
            .addOnSuccessListener {
                if(it.exists()){
                    val data = it.getValue(UserModel::class.java)
                    binding.name.setText(data!!.name.toString())
                    binding.number.setText(data.number.toString())
                    binding.city.setText(data.city.toString())
                    binding.email.setText(data.email.toString())
                    binding.birthday.setText(data.dateOfBirth.toString())

                    Glide.with(requireContext()).load(data.image).placeholder(R.drawable.profile).into(binding.userImg)

                    Config.hideDialog()
                }
            }

        binding.logOut.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }

        binding.edtProfile.setOnClickListener{
            startActivity(Intent(requireContext(), EditProfile::class.java))
        }
        return binding.root
    }


}