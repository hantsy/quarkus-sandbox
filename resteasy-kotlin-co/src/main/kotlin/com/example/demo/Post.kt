package com.example.demo

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime
import java.util.*


@Entity
@Table(name = "posts")
data class Post(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,
    var title: String? = null,
    var content: String? = null,
    @field:CreationTimestamp
    @field:Column(name = "created_at")
    var createdAt: LocalDateTime? = null
)
