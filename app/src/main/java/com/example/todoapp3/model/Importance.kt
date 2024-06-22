package com.example.todoapp3.model

enum class Importance {
    LOW { override val text = "Низкая" },
    HIGH { override val text = "Высокая" },
    MEDIUM { override val text = "Нет" };

    abstract val text: String

}