package com.example.myapplication

data class user(
    var name: String = "",
    var lastname: String = "",
    var username: String = "",
    var email: String = "",
    var password: String = ""
) {
    // If you want to enforce non-empty values for certain properties, you can add validation logic here.

    // For example:
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        // Add similar checks for other properties if needed
    }
}
