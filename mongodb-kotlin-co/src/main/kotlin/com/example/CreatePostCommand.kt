package com.example

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreatePostCommand(
    @field:NotBlank
    @field:Size(max = 50)
    val title: String,

    @field:NotBlank
    val body:String
)
