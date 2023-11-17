package com.example.kpiapi

import Semaphore
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.util.Log
import android.view.ViewTreeObserver
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val context = this
        val dataContainer = DataContainer(context)


        var fetchBut = findViewById<Button>(R.id.fetchBut)
        var inputName = findViewById<EditText>(R.id.editGroup)
        val semaphore = Semaphore(2)

        var classes = mutableListOf(
            Class("Бжд", "Не пам'ятаю"),
            Class("Англ", "Гаєва"),
            Class("Матан", "Зойка"),
            Class("Основи науки про дані", "Новотарський"),
            Class("Штучний інтелект", "Шимкович"),
            Class("Комп віжн", "Писарчук"),
            Class("Комп системи", "Луцький")
            )


        var suggestions: List<String> = listOf()
        val dataDict = mutableMapOf<String, String>()
        dataDict["Check"] = "Present"
        val myRecyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val myAdapter = ClassesAdapter(classes)
        myRecyclerView.adapter = myAdapter
        myRecyclerView.layoutManager = LinearLayoutManager(this)

        fun printDict(){
            dataDict.forEach { (key, value) ->
                Log.e("Data elems", "$key: $value")
                Toast.makeText(context, "$key : $value \n", Toast.LENGTH_SHORT).show()

                Log.e("Check", "printed dict is above")
            }
        }

        var job = GlobalScope.launch(Dispatchers.IO) {
            Log.e("GroupName", "launch starts")
            suggestions = dataContainer.requestAllGroups()
            Log.e("GroupName", "launch done")

            // Now you can use the suggestions
            suggestions.forEach { groupName ->
                Log.d("GroupName", groupName)
            }
        }


        runBlocking {
            job.join()
        }

        val adapterText = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, suggestions)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)
        autoCompleteTextView.setAdapter(adapterText)

        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            val selectedText = parent.getItemAtPosition(position) as String
            dataDict["Name"] = selectedText

            val timeRequest = GlobalScope.launch(Dispatchers.IO) {
                RequestManager(context).makeTimeRequest(
                    {timeDict ->
                        timeDict.forEach { (key, value) ->
                            dataDict[key] = value
                        }

                        Log.e("TAG", "get time response")

                    },{
                        // Error callback
                        Log.e("TAG", "Failed to get time response")
                        Toast.makeText(context, "Fail to get time response", Toast.LENGTH_SHORT).show()
                    })
                semaphore.release()
            }

//            val groupRequest = GlobalScope.launch(Dispatchers.IO) {
//                RequestManager(context).makeGroupRequest( selectedText,
//                { id, name ->
//                    dataDict["id"] = id
//                    Log.e("TAG", "get id response")
//                    // Success callback
//                    Toast.makeText(context, "$id , $name", Toast.LENGTH_LONG).show()
//
//                },
//                {
//                    // Error callback
//                    Log.e("TAG", "Failed to get id response")
//                    Toast.makeText(context, "Fail to get id response", Toast.LENGTH_SHORT).show()
//                }
//            )
//
//                semaphore.release()
//            }


            GlobalScope.launch(Dispatchers.Main) {
                val result = RequestManager(context).makeGroupRequest(selectedText)
                if (result != null) {
                    Log.e("My ID request", "$result")
                } else {
                    Log.e("My error ID request", "$result")
                }
            }

            runBlocking {
                Log.e("$context", "entered runBlock")

                semaphore.acquire()

                Log.e("Asynk", "print data")
                printDict()
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

            printDict()

            }

}
}
