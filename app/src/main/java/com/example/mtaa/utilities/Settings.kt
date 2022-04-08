package com.example.mtaa.utilities

import android.content.res.AssetManager

object Settings {

    // constants
    var env: Map<String, String>? = null
    val emailPattern: Regex = "^[.\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
    const val minPasswordLength: Int = 7

    fun initEnv(assets: AssetManager) {
        assets.open("env").bufferedReader().use {
            val env = mutableMapOf<String, String>()
            it.forEachLine { line ->
                val (key, value) = line.split("=")
                env[key] = value
            }
            Settings.env = env
        }
    }

}