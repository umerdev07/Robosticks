package com.maths.robostick

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class ChildCourseVideo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_child_course_video)

        // Get video URL and topic from Intent extras
        val videoUrl = intent.getStringExtra("VIDEO_URL")
//        val videoTopic = intent.getStringExtra("videoTopic")

        // Find views by ID

        val videoView = findViewById<VideoView>(R.id.videoView)

        if (videoUrl != null) {
            videoView.setVideoURI(Uri.parse(videoUrl))

            val mediaController = android.widget.MediaController(this)
            videoView.setMediaController(mediaController)
            mediaController.setMediaPlayer(videoView)

            videoView.start()
        } else {
            Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
        }
    }
}
