package com.example.todoapp3.data.network.retrofit.model

import com.google.gson.annotations.SerializedName

/**
 *  Model of the task response from the server
 */
data class TaskResponse(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("files") val files: List<String>? = null,
    @SerializedName("importance") val importance: String,
    @SerializedName("deadline") val deadline: Int,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String,
    @SerializedName("created_at") val created_at: Int,
    @SerializedName("changed_at") val changed_at: Int,
    @SerializedName("last_updated_by") val last_updated_by: String,
)