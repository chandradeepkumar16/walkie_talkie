package org.example.walkietalkie.model

import kotlinx.serialization.Serializable

@Serializable
data class Signal(
    val sender: String,
    val room: String,
    val type: String,
    val data: String
)