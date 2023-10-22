package com.example.kpiapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.*
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

        var suggestions: MutableList<String> = mutableListOf()


        // Assuming this is inside a function or an appropriate context
        RequestManager(this).GroupListRequest(
            { names ->
                Log.e("names size", names.size.toString())
                for(i in 0..names.size-1){
                    Log.e("TAG", names[i])
                    suggestions.add(names[i])
            }
            },
            {
                Log.e("TAG", "Failed to get names response")
                Toast.makeText(this, "Fail to get names response", Toast.LENGTH_SHORT).show()
            }
        )


        val adapterText = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapterText)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedText = parent.getItemAtPosition(position) as String
            dataDict["name"] = selectedText
            Log.e("TAG", selectedText)
            classes.add(Class("паралельне програмування", "Корка 2"))

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


            RequestManager(this).makeGroupRequest( selectedText,
                { id, name ->
                    dataDict["id"] = id
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


            dataDict.forEach { (key, value) ->
                Log.e("Data elems", "$key: $value")
            }


        }

        val scrollView = findViewById<ScrollView>(R.id.scroll)

        autoCompleteTextView.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                scrollView.viewTreeObserver.addOnPreDrawListener(
                    object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            scrollView.scrollTo(0, autoCompleteTextView.top)
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