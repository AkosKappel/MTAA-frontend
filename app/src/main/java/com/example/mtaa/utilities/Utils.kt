package com.example.mtaa.utilities

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import com.example.mtaa.RegistrationActivity
import com.example.mtaa.models.UserResponse
import retrofit2.Response
import java.util.Calendar
import java.util.Date

object Utils {

    // constants
    var env: Map<String, String>? = null
    private val emailPattern: Regex = "^[.\\w-]+@([\\w-]+\\.)+[\\w-]{2,4}$".toRegex()
    private const val minPasswordLength: Int = 7

    fun initEnv(assets: AssetManager) {
        assets.open("env").bufferedReader().use {
            val env = mutableMapOf<String, String>()
            it.forEachLine { line ->
                val (key, value) = line.split("=")
                env[key] = value
            }
            Utils.env = env
        }
    }

    fun dateToCalendar(date: Date): Calendar {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal
    }

    fun validateEmail(etEmail: EditText): Boolean {
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }
        if (!email.matches(emailPattern)) {
            etEmail.error = "Email is not valid"
            etEmail.requestFocus()
            return false
        }
        return true
    }

    fun validatePassword(etPassword: EditText, etConfirmPassword: EditText? = null): Boolean {
        val password = etPassword.text.toString().trim()
        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            etPassword.requestFocus()
            return false
        }
        if (password.length < minPasswordLength) {
            etPassword.error = "Password must be at least $minPasswordLength characters long"
            etPassword.requestFocus()
            return false
        }
        if (etConfirmPassword != null) {
            val confirmPassword = etConfirmPassword.text.toString().trim()
            if (confirmPassword.isEmpty()) {
                etConfirmPassword.error = "Confirm password is required"
                etConfirmPassword.requestFocus()
                return false
            }
            if (confirmPassword != password) {
                etConfirmPassword.error = "Passwords do not match"
                etConfirmPassword.requestFocus()
                return false
            }
        }
        return true
    }

    fun validateTitle(etTitle: EditText): Boolean {
        val title = etTitle.text.toString().trim()
        if (title.isEmpty()) {
            etTitle.error = "Title is required"
            etTitle.requestFocus()
            return false
        }
        return true
    }

    fun validateTime(etTime: EditText): Boolean {
        val time = etTime.text.toString().trim()
        if (time.isEmpty()) {
            etTime.error = "Time is required"
            etTime.requestFocus()
            return false
        }
        return true
    }

    fun validateDuration(etDuration: EditText): Boolean {
        val duration = etDuration.text.toString().trim()
        if (duration.isEmpty()) {
            etDuration.error = "Duration is required"
            etDuration.requestFocus()
            return false
        }
        return true
    }
}