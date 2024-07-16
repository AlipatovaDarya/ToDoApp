package com.example.todoapp3.presentation.model

import com.google.gson.annotations.SerializedName


/**
 * Enum class for Importance of tasks
 */
enum class Importance {
    @SerializedName("low")
    LOW {
        override val text = "Низкая"
    },
    @SerializedName("important")
    IMPORTANT {
        override val text = "Высокая"
    },
    @SerializedName("basic")
    BASIC {
        override val text = "Нет"
    };

    abstract val text: String

}