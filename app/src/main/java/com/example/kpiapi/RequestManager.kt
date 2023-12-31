package com.example.kpiapi

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.*
import org.json.JSONArray
import kotlin.coroutines.*
class RequestManager(context: Context) {

    val currentTimeUrl = "https://api.campus.kpi.ua/time/current"
    val groupUrl = "https://api.campus.kpi.ua/schedule/groups"
    val classUrl = "https://api.campus.kpi.ua/schedule/lessons?groupId="

    val singletonInstance = Singletone.getInstance(context)

    fun ClassesRequest(groupId: String, week: Int, successListener: (String) -> Unit, errorListener: () -> Unit){
        val request = JsonObjectRequest(Request.Method.GET, classUrl+groupId, null,
            { response ->
                val dataArray = response.getJSONObject("data")
                lateinit var weekSchedule: JSONArray
                when (week) {
                    1 -> weekSchedule = dataArray.getJSONArray("scheduleFirstWeek")
                    0 -> weekSchedule = dataArray.getJSONArray("scheduleSecondWeek")
                    else -> { // Note the block
                        Log.e("Week error" ,"$week is not appropriate")
                    }}

                successListener(weekSchedule.toString())
            },
            {
                errorListener()
            }
        )
        singletonInstance.addToRequestQueue(request)

    }
    suspend fun groupListRequest(): List<String> = suspendCoroutine { continuation ->
        val request = JsonObjectRequest(Request.Method.GET, groupUrl, null,
            { response ->
                val dataArray = response.getJSONArray("data")
                Log.e("data", "got data Json")
                var listOfGroups = mutableListOf<String>()
                for (i in 0 until dataArray.length()) {
                    val item = dataArray.getJSONObject(i)
                    listOfGroups.add(item.getString("name"))
                }
                continuation.resume(listOfGroups)
            },
            {
                continuation.resumeWithException(it)
            }
        )
        singletonInstance.addToRequestQueue(request)
    }


    suspend fun makeGroupRequest(inputName: String): Pair<String, String>? = withContext(Dispatchers.IO) {
        return@withContext suspendCancellableCoroutine<Pair<String, String>?> { continuation ->
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
                            continuation.resume(Pair(id, name))
                        }
                    }
                },
                {
                    continuation.resume(null)
                }
            )
            singletonInstance.addToRequestQueue(request)
        }
    }

//    fun makeGroupRequest( inputName: String,  successListener: (String, String) -> Unit, errorListener: () -> Unit) {
//        val request = JsonObjectRequest(Request.Method.GET, groupUrl, null,
//            { response ->
//                val dataArray = response.getJSONArray("data")
//                Log.e("input name", inputName)
//                for (i in 0 until dataArray.length()) {
//                    val item = dataArray.getJSONObject(i)
//                    val name = item.getString("name")
//                    if (name == inputName) {
//                        val id = item.getString("id")
//                        val faculty = item.getString("faculty")
//                        successListener(id, name)
//                    }
//                }
//            },
//            {
//                errorListener()
//            }
//        )
//        singletonInstance.addToRequestQueue(request)
//    }

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
