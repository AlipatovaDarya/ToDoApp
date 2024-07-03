package com.example.todoapp3.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun formatDate(date: LocalDate): String {
    return DateTimeFormatter
        .ofPattern("MMM dd yyyy")
        .format(date)
}

fun formatYear(date: LocalDate): String {
    return DateTimeFormatter
        .ofPattern("  yyyy")
        .format(date)
}