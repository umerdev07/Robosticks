package com.maths.robostick

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maths.robostick.databinding.ActivityMediaDisplay2Binding

class MediaDisplayActivity2 : AppCompatActivity() {


    private val binding: ActivityMediaDisplay2Binding by lazy {
        ActivityMediaDisplay2Binding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mediaArrayList: ArrayList<MediaClass>
    private lateinit var imageSlider: ImageSlider
    private var videoUrl :String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        imageSlider = binding.imageSlider

        val topicKey = intent.getStringExtra("TOPIC_KEY") ?: return

        mediaArrayList = arrayListOf()

        binding.videoBtn.setOnClickListener {
            if (videoUrl != null) {
                // Pass video URL to ChildCourseVideo activity
                val intent = Intent(this@MediaDisplayActivity2, CourseVideo::class.java)
                intent.putExtra("VIDEO_URL", videoUrl)
                intent.putExtra("topicname" , topicKey)
                startActivity(intent)
            } else {
                Toast.makeText(this@MediaDisplayActivity2, "No video available for this topic", Toast.LENGTH_SHORT).show()
            }
        }

        getMediaData(topicKey)
    }

    private fun getMediaData(topicKey: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("Courses2").child(topicKey).child("media")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    mediaArrayList.clear()

                    // List to store SlideModel objects
                    val imageList = ArrayList<SlideModel>()
                    videoUrl = snapshot.child("videoUrl").getValue(String::class.java)
                    for (mediaSnapshot in snapshot.children) {
                        val mediaUrl = mediaSnapshot.child("mediaUrl").getValue(String::class.java)
                        val mediaTile = mediaSnapshot.child("title").getValue(String::class.java)
                        if (mediaUrl != null) {
                            mediaArrayList.add(MediaClass(mediaUrl))
                            imageList.add(SlideModel(mediaUrl , mediaTile , ScaleTypes.FIT))
                        }
                    }

                    // Set the images in the ImageSlider
                    imageSlider.setImageList(imageList)
                } else {
                    Toast.makeText(this@MediaDisplayActivity2, "No data found for the topic", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MediaDisplayActivity2, "Error fetching data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}