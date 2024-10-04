package com.maths.robostick.OtherActivities

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
import com.maths.robostick.Students.StudentActivity
import com.maths.robostick.Teachers.TeacherActivity
import com.maths.robostick.UserManagment.LoginActivity
import com.maths.robostick.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var storage: FirebaseStorage
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
        auth = FirebaseAuth.getInstance()

        // Handle logout
        binding.logout.setOnClickListener {
            showAlertDialog("Do you want to logout?")
        }

        // Other buttons setup
        binding.web.setOnClickListener {
            openUrl("https://robosticks.com/")
        }

        binding.students.setOnClickListener {
            Log.d("MainActivity", "Students button clicked")
            startActivity(Intent(this, StudentActivity::class.java))
        }

        binding.educator.setOnClickListener {
            Log.d("MainActivity", "Teacher button Clicked")
            startActivity(Intent(this ,TeacherActivity::class.java))
        }

        binding.facebook.setOnClickListener {
            openUrl("https://web.facebook.com/RoboSticks/")
        }

        binding.instagram.setOnClickListener {
            openUrl("https://www.instagram.com/robosticksofficial/")
        }

        binding.linkdin.setOnClickListener {
            openUrl("https://www.linkedin.com/company/robosticks/")
        }
    }

    private fun showAlertDialog(message: String) {
        AlertDialog.Builder(this)
            .setMessage(message)
            .setTitle("Logout")
            .setCancelable(false)
            .setPositiveButton("Yes") { dialog, _ ->
                auth.signOut() // Sign out from FirebaseAuth
                navigateToLogin() // Navigate to LoginActivity
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss() // Dismiss the dialog
            }
            .create()
            .show()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish() // Finish current activity to prevent back navigation
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open link", Toast.LENGTH_SHORT).show()
        }
    }
}
