package com.example.datingapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datingapp.MainActivity
import com.example.datingapp.R
import com.example.datingapp.databinding.ActivityEditProfileBinding
import com.example.datingapp.model.UserModel
import com.example.datingapp.utils.Config
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class EditProfile : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    private var imageUri: Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.userImage.setImageURI(imageUri)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.editProfile.setOnClickListener {
            validateData()
        }

    }


    private fun validateData() {
        if(binding.userName.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || binding.userLocation.text.toString().isEmpty()
            ||binding.userBirthDate.text.toString().isEmpty()
            || imageUri == null){
            Toast.makeText(this,"Please! Enter all fields", Toast.LENGTH_SHORT).show()
        } else {
            uploadImage()
        }
    }

    private fun uploadImage() {
        Config.showDialog(this)

        val storageRef = FirebaseStorage.getInstance().getReference("profile")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("profile.jpg")
        storageRef.putFile(imageUri!!)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener {
                    updateProfile(it)
                }.addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Config.hideDialog()
                Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
            }

    }
    private fun updateProfile(imageUrl: Uri?) {
        val data = UserModel(
            name = binding.userName.text.toString(),
            image = imageUrl.toString(),
            email = binding.userEmail.text.toString(),
            city = binding.userLocation.text.toString(),
            dateOfBirth = binding.userBirthDate.text.toString()
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener {
                Config.hideDialog()
                if(it.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    Toast.makeText(this,"User register successful", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,it.exception!!.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

}