package com.example.todoapp3.presentation.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Function for format date according of pattern "MMM dd yyyy"
 */
fun formatDate(date: LocalDate): String {
    return DateTimeFormatter
        .ofPattern("MMM dd yyyy")
        .format(date)
}

/**
 * Function for format year
 */
fun formatYear(date: LocalDate): String {
    return DateTimeFormatter
        .ofPattern("  yyyy")
        .format(date)
}