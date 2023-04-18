package com.example.model

data class CourseJsonData(
    val category: String,
    val subCategory: String,
    val courseTitle: String,
    val level: String,
    val author: String,
    val contentLength: Int,
    val rating: Float,
    val numberReviews: Int,
    val students: Int,
    val language: String,
    val headline: String,
    val description: String
)