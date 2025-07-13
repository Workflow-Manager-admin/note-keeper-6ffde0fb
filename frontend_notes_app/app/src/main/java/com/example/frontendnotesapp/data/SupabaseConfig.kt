package com.example.frontendnotesapp.data

import android.content.Context
import java.util.Properties

// PUBLIC_INTERFACE
object SupabaseConfig {
    fun load(context: Context): Pair<String, String> {
        val props = Properties()
        val envIn = context.assets.open(".env")
        props.load(envIn)
        val url = props.getProperty("SUPABASE_URL") ?: throw RuntimeException("No SUPABASE_URL")
        val key = props.getProperty("SUPABASE_KEY") ?: throw RuntimeException("No SUPABASE_KEY")
        return Pair(url, key)
    }
}
