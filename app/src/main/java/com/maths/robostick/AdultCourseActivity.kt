package com.maths.robostick

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.maths.robostick.databinding.ActivityChildCourseBinding

class AdultCourseActivity : AppCompatActivity() {
    private val binding: ActivityChildCourseBinding by lazy {
        ActivityChildCourseBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var courseRecyclerView: RecyclerView
    private lateinit var courseArrayList: ArrayList<Courses>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()

        courseRecyclerView = binding.recyclarView
        courseRecyclerView.layoutManager = LinearLayoutManager(this)
        courseRecyclerView.setHasFixedSize(true)

        courseArrayList = arrayListOf()
        getCourseData()
    }

    private fun getCourseData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("AdultCourses")

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    courseArrayList.clear() // Clear the list before adding new data

                    val courseKeyMap = mutableMapOf<String, Courses>() // To map keys to Courses

                    for (userSnapshot in snapshot.children) {
                        val course = userSnapshot.getValue(Courses::class.java)
                        val key = userSnapshot.key
                        if (course != null && key != null) {
                            courseKeyMap[key] = course // Add to map
                        }
                    }

                    courseRecyclerView.adapter = AdapterClass(ArrayList(courseKeyMap.values)) { course ->
                        // Handle item click
                        val topicKey = courseKeyMap.entries.find { it.value == course }?.key
                        if (topicKey != null) {
                            val intent = Intent(this@AdultCourseActivity, MediaDisplayActivity2::class.java).apply {
                                putExtra("TOPIC_KEY", topicKey) // Pass the topic key
                            }
                            startActivity(intent)
                        }
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Data is not fetched", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
