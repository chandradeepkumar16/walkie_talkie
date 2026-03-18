package org.example.walkietalkie

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform