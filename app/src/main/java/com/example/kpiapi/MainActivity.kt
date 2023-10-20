package com.example.kpiapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var fetchBut = findViewById<Button>(R.id.fetchBut)
        var inputName = findViewById<EditText>(R.id.editGroup)

        var classes = mutableListOf(
            Class("Бжд", "Не пам'ятаю"),
            Class("Англ", "Гаєва"),
            Class("Матан", "Зойка"),
            Class("Основи науки про дані", "Новотарський"),
            Class("Штучний інтелект", "Шимкович"),
            Class("Комп віжн", "Писарчук"),
            Class("Комп системи", "Луцький")
            )

        val dataDict = mutableMapOf<String, String>()
        val myRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val myAdapter = ClassesAdapter(classes)
        myRecyclerView.adapter = myAdapter
        myRecyclerView.layoutManager = LinearLayoutManager(this)


        val scrollView = findViewById<ScrollView>(R.id.scroll)

        val editGroup: EditText = findViewById(R.id.enterGroup)

        editGroup.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollView.viewTreeObserver.addOnPreDrawListener(
                    object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            scrollView.scrollTo(0, editGroup.top)
                            scrollView.viewTreeObserver.removeOnPreDrawListener(this)
                            return true
                        }
                    }
                )
            }
        }

        // Assuming fetchBut and inputName are views in your activity.
        fetchBut.setOnClickListener {
            Log.e("Group", inputName.text.toString())
            dataDict.forEach { (key, value) ->
                Log.e("Data elems", "$key: $value")

            }

            RequestManager(this).makeTimeRequest(
                {timeDict ->
                    timeDict.forEach { (key, value) ->
                        dataDict[key] = value
                    }

                    Log.e("TAG", "get time response")
            },{
                    // Error callback
                    Log.e("TAG", "Failed to get time response")
                    Toast.makeText(this, "Fail to get time response", Toast.LENGTH_SHORT).show()
            })


            RequestManager(this).makeGroupRequest( inputName.text.toString(),
                { id, name ->
                    dataDict["id"] = id
                    dataDict["name"] = name
                    Log.e("TAG", "get id response")
                    // Success callback
                    Toast.makeText(this, "$id , $name", Toast.LENGTH_LONG).show()
                },
                {
                    // Error callback
                    Log.e("TAG", "Failed to get id response")
                    Toast.makeText(this, "Fail to get id response", Toast.LENGTH_SHORT).show()
                }
            )


            }

}
}