package com.maths.robostick.Teachers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.maths.robostick.databinding.ActivityTeacherBinding

class TeacherActivity : AppCompatActivity() {
    private val binding :ActivityTeacherBinding by lazy { 
        ActivityTeacherBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        binding.workshop.setOnClickListener {
           startActivity(Intent(this , TeachersWorkshops::class.java))
        }


        binding.web.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://robosticks.com/")
            startActivity(intent)
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