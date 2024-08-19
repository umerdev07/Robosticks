package com.maths.robostick

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.maths.robostick.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var storage: FirebaseStorage
    private lateinit var userEmail: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)

        storage = FirebaseStorage.getInstance()

        userEmail = intent.getStringExtra("email") ?: ""

        binding.profileEmail.text = userEmail

        auth = FirebaseAuth.getInstance()
        binding.logout.setOnClickListener {
            showAlertDialog("Do you want to logout?")

        }

        binding.students.setOnClickListener {
            Log.d("MainActivity", "Students button clicked")
            startActivity(Intent(this, StudentActivity::class.java))
        }


        binding.facebook.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://web.facebook.com/RoboSticks/"))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show()
            }
        }

        binding.instagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.instagram.com/robosticksofficial/")
            startActivity(intent)
        }
        binding.linkdin.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.linkedin.com/company/robosticks/")
            startActivity(intent)
        }
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setTitle("Logout")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->

                // Redirect to LoginActivity after logout
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }


}
