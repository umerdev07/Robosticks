package com.maths.robostick

import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.maths.robostick.databinding.ActivityCourseVideoBinding

class CourseVideo : AppCompatActivity() {
    private val binding: ActivityCourseVideoBinding by lazy {
        ActivityCourseVideoBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("VIDEO_URL")
        val videoTopic = intent.getStringExtra("topicname")

        binding.videoTitle.text = videoTopic

        if (videoUrl != null) {
            binding.videoView.setVideoURI(Uri.parse(videoUrl))

            val mediaController = android.widget.MediaController(this)
            binding.videoView.setMediaController(mediaController)
            mediaController.setMediaPlayer(binding.videoView)

            binding.videoView.start()
        } else {
            Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
        }
    }
}
