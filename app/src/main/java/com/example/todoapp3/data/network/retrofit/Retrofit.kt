package com.example.todoapp3.data.network.retrofit

import com.example.todoapp3.data.network.OkHttpHelper
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASEURL = "https://hive.mrdekk.ru/todo/"

/**
 *  This object is an singleton instance of a Retrofit
 */
object Retrofit {

    val api: TodoApi by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASEURL)
            .client(OkHttpHelper.okHttpClient)
            .build()
            .create(TodoApi::class.java)
    }
}