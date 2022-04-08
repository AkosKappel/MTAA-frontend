package com.example.mtaa.utilities

import android.widget.EditText

object Validator {

    fun validateEmail(etEmail: EditText): Boolean {
        val email = etEmail.text.toString().trim()
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            etEmail.requestFocus()
            return false
        }
        if (!email.matches(Settings.emailPattern)) {
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
        if (password.length < Settings.minPasswordLength) {
            etPassword.error =
                "Password must be at least ${Settings.minPasswordLength} characters long"
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
        if (!time.matches(Settings.timePattern)) {
            etTime.error = "Time is not valid"
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
        if (duration.toIntOrNull() == null) {
            etDuration.error = "Duration must be a number"
            etDuration.requestFocus()
            return false
        }
        if (duration.toInt() <= 0) {
            etDuration.error = "Duration must be a positive number"
            etDuration.requestFocus()
            return false
        }
        return true
    }

    fun validateID(etId: EditText): Boolean {
        val id = etId.text.toString().trim()
        if (id.isEmpty()) {
            etId.error = "ID is required"
            etId.requestFocus()
            return false
        }
        if (id.toIntOrNull() == null) {
            etId.error = "ID must be a number"
            etId.requestFocus()
            return false
        }
        if (id.toInt() <= 0) {
            etId.error = "ID must be a positive number"
            etId.requestFocus()
            return false
        }
        return true
    }

    fun validateNotOwner(etId: EditText, ownerId: Int): Boolean {
        val id = etId.text.toString().trim().toInt()
        if (id == ownerId) {
            etId.error = "Cannot add / remove owner"
            etId.requestFocus()
            return false
        }
        return true
    }

}