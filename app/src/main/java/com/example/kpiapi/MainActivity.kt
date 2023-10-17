package com.example.kpiapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fetchBut = findViewById<Button>(R.id.fetchBut)

        var classes = mutableListOf(
            Class("Бжд", "Не пам'ятаю"),
            Class("Англ", "Гаєва"),
            Class("Матан", "Зойка"),
            Class("Основи науки про дані", "Новотарський"),
            Class("Штучний інтелект", "Шимкович"),
            Class("Комп віжн", "Писарчук"),
            Class("Комп системи", "Луцький")
            )

        val myRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val myAdapter = ClassesAdapter(classes)
        myRecyclerView.adapter = myAdapter
        myRecyclerView.layoutManager = LinearLayoutManager(this)


    }
}