package com.example.todoapp3.data.network.retrofit.model

import com.google.gson.annotations.SerializedName

/**
 *  Model of the task list response from the server
 */
data class TaskListResponse(
    @SerializedName("status") val status: String,
    @SerializedName("list") val list: List<TaskResponse>,
    @SerializedName("revision") val revision: Int
)