package com.example.todoapp3.data.network.retrofit.model

/**
 * Class for passing error code and text inherited from an Exception
 */
data class ExceptionWithErrorCode(override val message: String, val code: Int): Exception(message)
