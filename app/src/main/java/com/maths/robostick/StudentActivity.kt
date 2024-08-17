package com.maths.robostick

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class StudentActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding object
        setContentView(R.layout.activity_student)

        val kids = findViewById<Button>(R.id.kidage)
        val young = findViewById<Button>(R.id.age11_plus)
        val facebook = findViewById<ImageView>(R.id.facebook)
        val linkdin = findViewById<ImageView>(R.id.linkdin)
        val instagram = findViewById<ImageView>(R.id.instagram)

        kids.setOnClickListener {
            startActivity(Intent(applicationContext ,ChildCourseActivity::class.java))
        }

        young.setOnClickListener {
            startActivity(Intent(applicationContext , AdultCourseActivity::class.java))
        }

        facebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://web.facebook.com/RoboSticks/")
            startActivity(intent)
        }
        instagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.instagram.com/robosticksofficial/")
            startActivity(intent)
        }
        linkdin.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://www.linkedin.com/company/robosticks/")
            startActivity(intent)
        }
    }
}
