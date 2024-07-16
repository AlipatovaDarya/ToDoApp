package com.example.todoapp3.data.network.retrofit

import com.example.todoapp3.data.network.retrofit.model.TaskListResponse
import com.example.todoapp3.data.network.retrofit.model.TaskResponse
import com.example.todoapp3.data.network.retrofit.model.Request
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


/**
 * Interface containing methods to interact with the Todo API.
 */
interface TodoApi {

    @GET("/todo/list")
    fun getTodoList(): Call<TaskListResponse>


    @POST("/todo/list")
    fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: Request<TaskResponse>,
    ): Call<Request<TaskListResponse>>


    @PATCH("/todo/list")
    fun updateTodoList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: Response<TaskListResponse>,
    ): Call<TaskListResponse>


    @GET("/todo/list/{id}")
    fun getTodoItem(
        @Path("id") id: String,
    ): Call<TaskResponse>


    @PUT("/todo/list/{id}")
    fun updateTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body request: Request<TaskResponse>,
    ): Call<TaskResponse>


    @DELETE("/todo/list/{id}")
    fun deleteTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
    ): Call<TaskResponse>

}