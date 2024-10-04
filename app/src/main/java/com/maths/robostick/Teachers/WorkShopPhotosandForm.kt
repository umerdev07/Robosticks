package com.maths.robostick.Teachers

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.maths.robostick.databinding.ActivityWorkShopPhotosandFormBinding

class WorkShopPhotosandForm : AppCompatActivity() {
    private val binding :ActivityWorkShopPhotosandFormBinding by lazy {
        ActivityWorkShopPhotosandFormBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val schoolName = intent.getStringExtra("TOPIC_KEY")


    }
}