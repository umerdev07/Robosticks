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
            // Show the ProgressBar while loading the video
            binding.videoLoading.visibility = android.view.View.VISIBLE

            binding.videoView.setVideoURI(Uri.parse(videoUrl))

            val mediaController = android.widget.MediaController(this)
            binding.videoView.setMediaController(mediaController)
            mediaController.setMediaPlayer(binding.videoView)

            binding.videoView.setOnPreparedListener {
                binding.videoLoading.visibility = android.view.View.GONE
                binding.videoView.start()
            }

            binding.videoView.setOnInfoListener { _, what, _ ->
                when (what) {
                    android.media.MediaPlayer.MEDIA_INFO_BUFFERING_START -> {

                        binding.videoLoading.visibility = android.view.View.VISIBLE
                    }
                    android.media.MediaPlayer.MEDIA_INFO_BUFFERING_END -> {

                        binding.videoLoading.visibility = android.view.View.GONE
                    }
                }
                false
            }

            // Handle errors during video playback
            binding.videoView.setOnErrorListener { _, what, extra ->
                binding.videoLoading.visibility = android.view.View.GONE
                Toast.makeText(this, "Video playback error: $what", Toast.LENGTH_LONG).show()
                true
            }
        } else {
            Toast.makeText(applicationContext, "Error Occurred", Toast.LENGTH_SHORT).show()
        }
    }
}
