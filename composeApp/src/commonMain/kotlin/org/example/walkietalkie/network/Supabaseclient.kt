package org.example.walkietalkie.network



import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.postgrest.Postgrest

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = "https://gruxtpkscpcusiezbkeo.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImdydXh0cGtzY3BjdXNpZXpia2VvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzM3NzY2NDksImV4cCI6MjA4OTM1MjY0OX0.c6ymv9obgjSDG3e9FTE2N_m1gZL7xv7xLhMWqVHLc0A"
    ) {
        install(Postgrest)
        install(Realtime)
    }
}