package com.example.kpiapi

import android.content.Context;
import android.util.Log
import android.widget.Toast


public class DataContainer(var contextMain: Context) {

    var requestManager: RequestManager = RequestManager(contextMain)
    var suggestions: MutableList<String> = mutableListOf()


    suspend fun requestAllGroups(): List<String> {
        return try {
            requestManager.groupListRequest()
        } catch (e: Exception) {
            Log.e("TAG", "Failed to get names response", e)
            Toast.makeText(contextMain, "Fail to get names response", Toast.LENGTH_SHORT).show()
            emptyList()
        }
    }

}

//    fun requestAllGroups(suggestions: MutableList<String>): MutableList<String>{
//        RequestManager(contextMain).GroupListRequest(
//            { names ->
//                Log.e("names size", names.size.toString())
//                for(i in 0..names.size-1){
////                    Log.e("TAG", names[i])
//                    suggestions.add(names[i])
//                }
//            },
//            {
//                Log.e("TAG", "Failed to get names response")
//                Toast.makeText(contextMain, "Fail to get names response", Toast.LENGTH_SHORT).show()
//            }
//        )
//        return suggestions
//    }
