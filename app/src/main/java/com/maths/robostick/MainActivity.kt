package com.maths.robostick

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.storage.FirebaseStorage
import com.maths.robostick.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var storage: FirebaseStorage
    private lateinit var userEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)

        // Initialize Firebase Storage
        storage = FirebaseStorage.getInstance()

        // Get email from Intent
        userEmail = intent.getStringExtra("email") ?: ""

        // Set email to TextView
        binding.profileEmail.text = userEmail

        // Retrieve and display the profile image
//        retrieveProfileImage()

        binding.logout.setOnClickListener {
            showAlertDialog("Do you want to logout?")
        }

        binding.students.setOnClickListener {
            Log.d("MainActivity", "Students button clicked")
            startActivity(Intent(this, StudentActivity::class.java))
        }


        binding.facebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.instagram.com/robosticksofficial/")
            startActivity(intent)
        }
        binding.instagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://web.facebook.com/RoboSticks/")
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
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    /*
        private fun retrieveProfileImage() {
            // Replace dots with underscores to match the file path in Firebase Storage
            val formattedEmail = userEmail.replace(".", "_")
            val filePath = "Profile/$formattedEmail.jpg"
            val storageReference = storage.reference.child(filePath)

            // Log the file path for debugging
            Log.d("MainActivity", "Attempting to retrieve image from path: $filePath")

            val localFile = File.createTempFile("tempImage", "jpg")

            storageReference.getFile(localFile).addOnSuccessListener {
                // Successfully retrieved the file
                Log.d("MainActivity", "Image successfully retrieved from path: $filePath")
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                binding.profileImage.setImageBitmap(bitmap)
            }.addOnFailureListener { exception ->
                // Handle any errors
                Log.e("MainActivity", "Failed to retrieve image from path: $filePath", exception)
                Toast.makeText(this, "Image retrieval failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                binding.profileImage.setImageResource(R.drawable.profile) // Set a default image or handle error
            }
        }
    */

}
