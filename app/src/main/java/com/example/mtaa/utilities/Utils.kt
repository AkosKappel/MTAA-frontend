package com.example.mtaa.utilities

import java.io.File

object Utils {

    // TODO; toto este nefunguje
    val env = readEnvFile("../../../../assets/env")

    // function to read .env file
    private fun readEnvFile(fileName: String): Map<String, String> {
        println(File(".").absolutePath)
        val env = mutableMapOf<String, String>()
        env["BASE_URL"] = "http://127.0.0.1:8080"
        try {
            val file = File(fileName)
            file.forEachLine {
                val (key, value) = it.split("=")
                env[key] = value
            }
        } catch (e: Exception) {
            println("Error reading .env file")
        }
        return env
    }

}