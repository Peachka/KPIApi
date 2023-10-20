package com.example.kpiapi

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

class Singletone constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: Singletone? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Singletone(context).also {
                    INSTANCE = it
                }
            }
    }

    val requestQueue: RequestQueue by lazy {
        // applicationContext is key, it keeps you from leaking the
        // Activity or BroadcastReceiver if someone passes one in.
        Volley.newRequestQueue(context.applicationContext)
    }

    fun <T> addToRequestQueue(request: Request<T>) {
        requestQueue.add(request)
    }
}
