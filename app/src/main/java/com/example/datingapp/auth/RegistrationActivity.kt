package com.example.datingapp.auth

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.datingapp.MainActivity
import com.example.datingapp.databinding.ActivityRegistrationBinding
import com.example.datingapp.model.UserModel
import com.example.datingapp.utils.Config
import com.example.datingapp.utils.Config.hideDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private var imageUri: Uri? = null

    private val selectImage = registerForActivityResult(ActivityResultContracts.GetContent()){
        imageUri = it
        binding.userImage.setImageURI(imageUri)
    }

//    val calenderBox = Calendar.getInstance()
//    val dateBox = DatePickerDialog.OnDateSetListener{ view: DatePicker?, year: Int, month: Int, day ->
//        calenderBox.set(Calendar.YEAR, year)
//        calenderBox.set(Calendar.MONTH,month)
//        calenderBox.set(Calendar.DAY_OF_MONTH,day)
//    }
//
//    private fun updateDate(calendar: Calendar){
//        val dateFormat = "dd-MM-yyyy"
//        val simple = SimpleDateFormat(dateFormat, Locale.UK)
//        binding.userBirthDate.setText(simple.format(calendar.time))
//    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userImage.setOnClickListener {
            selectImage.launch("image/*")
        }

        binding.saveData.setOnClickListener {
            validateData()
        }
    }

    private fun validateData() {
        if(binding.userName.text.toString().isEmpty()
            || binding.userEmail.text.toString().isEmpty()
            || binding.userCity.text.toString().isEmpty()
            ||binding.userBirthDate.text.toString().isEmpty()
            || imageUri == null){
            Toast.makeText(this,"Please! Enter all fields", Toast.LENGTH_SHORT).show()
        }else if (!binding.terms.isChecked){
            Toast.makeText(this,"Please Accept terms and conditions", Toast.LENGTH_SHORT).show()
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
                    storeData(it)
                }.addOnFailureListener {
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                    hideDialog()
                    Toast.makeText(this,it.message,Toast.LENGTH_SHORT).show()
                }

    }

    private fun storeData(imageUrl: Uri?) {

        val data = UserModel(
            name = binding.userName.text.toString(),
            image = imageUrl.toString(),
            email = binding.userEmail.text.toString(),
            city = binding.userCity.text.toString(),
            dateOfBirth = binding.userBirthDate.text.toString()
        )

        FirebaseDatabase.getInstance().getReference("users")
            .child(FirebaseAuth.getInstance().currentUser!!.phoneNumber!!)
            .setValue(data).addOnCompleteListener {
                hideDialog()
                if(it.isSuccessful){
                    startActivity(Intent(this,MainActivity::class.java))
                    Toast.makeText(this,"User register successful",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this,it.exception!!.message,Toast.LENGTH_SHORT).show()
                }
            }
    }
}