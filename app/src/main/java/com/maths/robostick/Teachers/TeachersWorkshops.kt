package com.maths.robostick.Teachers

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.maths.robostick.DataClasses.SchoolsTeacherClass
import com.maths.robostick.DataClasses.TeacherAdapterClass
import com.maths.robostick.OtherActivities.MainActivity
import com.maths.robostick.databinding.ActivityTeachersWorkshopsBinding

class TeachersWorkshops : AppCompatActivity() {
    private val binding: ActivityTeachersWorkshopsBinding by lazy {
        ActivityTeachersWorkshopsBinding.inflate(layoutInflater)
    }
    private lateinit var databaseReference: DatabaseReference
    private lateinit var workshopsRecyclerView: RecyclerView
    private lateinit var workshops: ArrayList<SchoolsTeacherClass>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar?.hide()

        // Initialize spinner view and set it as hidden by default
        val spinKitView = binding.spinKit
        spinKitView.visibility = View.GONE

        workshopsRecyclerView = binding.recyclarView
        workshopsRecyclerView.layoutManager = LinearLayoutManager(this)
        workshopsRecyclerView.setHasFixedSize(true)

        workshops = arrayListOf()

        // Fetch workshops data from Firebase
        getWorkshopsData()
    }

    private fun getWorkshopsData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("TeachersWorkshops")
        binding.spinKit.visibility = View.VISIBLE // Show spinner while loading data

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    workshops.clear()

                    val workshopKeyMap = mutableMapOf<String, SchoolsTeacherClass>()

                    for (userSnapshot in snapshot.children) {
                        val workshop = userSnapshot.getValue(SchoolsTeacherClass::class.java)
                        val key = userSnapshot.key
                        if (workshop != null && key != null) {
                            workshopKeyMap[key] = workshop
                        }
                    }

                    workshopsRecyclerView.adapter = TeacherAdapterClass(ArrayList(workshopKeyMap.values)) { course ->
                        // Handle item click
                        val topicKey = workshopKeyMap.entries.find { it.value == course }?.key
                        if (topicKey != null) {
                            val intent = Intent(this@TeachersWorkshops, MainActivity::class.java).apply {
                                putExtra("TOPIC_KEY", topicKey)
                            }
                            startActivity(intent)
                        }
                    }
                    binding.spinKit.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Error handling
                Toast.makeText(applicationContext, "Data is not fetched: ${error.message}", Toast.LENGTH_SHORT).show()
                binding.spinKit.visibility = View.GONE
            }
        })
    }
}
