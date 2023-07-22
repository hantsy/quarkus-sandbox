package com.example.demo

import java.util.*


class PostNotFoundException(uuid: UUID) : RuntimeException("Post: $uuid was not found.")

