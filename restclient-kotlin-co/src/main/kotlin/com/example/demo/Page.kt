package com.example.demo

data class Page<out T>(val content: List<T>, val count: Long)