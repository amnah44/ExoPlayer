package com.examble.youtube.data.network

import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request

open class Client {
    private val okHttpClient = OkHttpClient()
    private val baseUrl = "https://raw.githubusercontent.com/Bareq-altaamah/mock/main/classic.json"

    fun getResponse(callBackResponse: Callback) {
        val request = Request.Builder().url(baseUrl).build()
        val response = okHttpClient
        response.newCall(request).enqueue(callBackResponse)
    }
}