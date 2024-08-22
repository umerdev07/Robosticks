package com.maths.robostick

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdapterClass(private val coursesList: ArrayList<Courses> , val onItemClick : (Courses) -> Unit) : RecyclerView.Adapter<AdapterClass.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.courseslist, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentCourse = coursesList[position]

        // Bind data to your views here
        holder.topic.text = currentCourse.title
        holder.desp.text = currentCourse.description

        holder.itemView.setOnClickListener {
            onItemClick(currentCourse)
        }
    }

    override fun getItemCount(): Int {
        return coursesList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Initialize your views here
        val topic: TextView = itemView.findViewById(R.id.title)
        val desp: TextView = itemView.findViewById(R.id.description)
    }
}
