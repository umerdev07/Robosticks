package com.maths.robostick

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.maths.robostick.databinding.ActivityStudentBinding

class StudentActivity : AppCompatActivity() {
private val binding : ActivityStudentBinding by lazy {
    ActivityStudentBinding.inflate(layoutInflater)
}
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        // Initialize the binding object
        setContentView(binding.root)

        binding.kidage.setOnClickListener {
            startActivity(Intent(applicationContext ,ChildCourseActivity::class.java))
        }

        binding.age11Plus.setOnClickListener {
            startActivity(Intent(applicationContext , AdultCourseActivity::class.java))
        }

        binding.facebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://web.facebook.com/RoboSticks/")
            startActivity(intent)
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
}
