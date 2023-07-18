package com.example.demo

import java.time.LocalDateTime

data class Post(
    val id: String,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
)
