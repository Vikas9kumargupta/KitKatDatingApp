package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.datingapp.auth.LoginActivity
import com.example.datingapp.databinding.ActivityMainBinding
 import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() , OnNavigationItemSelectedListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    var actionBarDrawerToggle : ActionBarDrawerToggle ? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarDrawerToggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()

        binding.navigationView.setNavigationItemSelectedListener(this)

        val navController = findNavController(R.id.fragment)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.rateUs -> {
                Toast.makeText(this,"Rate Us", Toast.LENGTH_SHORT).show()
            }
            R.id.shareApp -> {
                Toast.makeText(this,"Share App", Toast.LENGTH_SHORT).show()
            }
            R.id.favourite -> {
                Toast.makeText(this,"Favourite", Toast.LENGTH_SHORT).show()
            }
            R.id.terms -> {
                Toast.makeText(this,"Terms and Conditions", Toast.LENGTH_SHORT).show()
            }
            R.id.privacy -> {
                Toast.makeText(this,"Privacy", Toast.LENGTH_SHORT).show()
            }
            R.id.aboutUs -> {
                Toast.makeText(this,"About Us", Toast.LENGTH_SHORT).show()
            }
            R.id.signOut -> {
                auth.signOut()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
    }else
        super.onOptionsItemSelected(item)
        }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.close()
        }
        else
            super.onBackPressed()
    }
}