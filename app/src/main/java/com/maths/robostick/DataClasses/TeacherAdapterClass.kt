package com.maths.robostick.DataClasses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.maths.robostick.R

class TeacherAdapterClass(private val workshopList :ArrayList<SchoolsTeacherClass> , val onItemClick : (SchoolsTeacherClass) -> Unit) : RecyclerView.Adapter<TeacherAdapterClass.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TeacherAdapterClass.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.courseslist , parent , false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherAdapterClass.ViewHolder, position: Int) {
        val workshop = workshopList[position]
        holder.topic.text = workshop.title
        holder.desp.text = workshop.description

        holder.itemView.setOnClickListener {
            onItemClick(workshop)
        }
    }

    override fun getItemCount(): Int {
        return workshopList.size
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val topic: TextView = itemView.findViewById(R.id.title)
        val desp: TextView = itemView.findViewById(R.id.description)
    }
}


