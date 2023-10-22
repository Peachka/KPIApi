package com.example.kpiapi

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class RequestManager(context: Context) {

    val currentTimeUrl = "https://api.campus.kpi.ua/time/current"
    val groupUrl = "https://api.campus.kpi.ua/schedule/groups"


    val singletonInstance = Singletone.getInstance(context)

    fun GroupListRequest(successListener: (List<String>) -> Unit, errorListener: () -> Unit){
        val request = JsonObjectRequest(Request.Method.GET, groupUrl, null,
            { response ->
                val dataArray = response.getJSONArray("data")
                var listOfGroups = mutableListOf<String>()
                for (i in 0 until dataArray.length()) {
                    val item = dataArray.getJSONObject(i)
//                    Log.e("name", item.getString("name"))
                    listOfGroups.add(item.getString("name"))
                }
                successListener(listOfGroups)
            },
            {
                errorListener()
            }
        )
        singletonInstance.addToRequestQueue(request)

    }


    fun makeGroupRequest( inputName: String,  successListener: (String, String) -> Unit, errorListener: () -> Unit) {
        val request = JsonObjectRequest(Request.Method.GET, groupUrl, null,
            { response ->
                val dataArray = response.getJSONArray("data")
                Log.e("input name", inputName)
                for (i in 0 until dataArray.length()) {
                    val item = dataArray.getJSONObject(i)
                    val name = item.getString("name")
                    if (name == inputName) {
                        val id = item.getString("id")
                        val faculty = item.getString("faculty")
                        successListener(id, name)
                    }
                }
            },
            {
                errorListener()
            }
        )
        singletonInstance.addToRequestQueue(request)
    }

    fun makeTimeRequest(successListener: (Map<String, String>) -> Unit, errorListener: () -> Unit){
        val request = JsonObjectRequest(Request.Method.GET, currentTimeUrl, null, {response ->
            val dataObject = response.getJSONObject("data")
            val timeDict = mutableMapOf<String, String>()

            val keys = dataObject.keys()
            while (keys.hasNext()) {
                val key = keys.next()
                val value = dataObject.getInt(key)
                timeDict[key] = value.toString()
            }
            successListener(timeDict)
        },
            {
                errorListener()
            }
        )
        singletonInstance.addToRequestQueue(request)
    }
}
