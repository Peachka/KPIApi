package com.example.kpiapi

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ClassesAdapter (var classes: List<Class>) : RecyclerView.Adapter<ClassesAdapter.ClassViewHolder>(){

    inner class ClassViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var subject = itemView.findViewById<TextView>(R.id.subject)
        var teacher = itemView.findViewById<TextView>(R.id.teacher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.class_view, parent, false)
        return ClassViewHolder(view)
    }

    override fun getItemCount(): Int {
        return classes.size
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.subject.text = classes[position].subject
        holder.teacher.text = classes[position].teacher
    }
}