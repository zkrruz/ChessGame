package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class register : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        etName = findViewById(R.id.etName)
        etLastName = findViewById(R.id.etLastName)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference
    }

    // Your save function will use the 'database' reference to interact with Firebase.
    // Implement the onClickSave function accordingly.
    public fun onClickSave(view: View) {
        // Example: Save data to Firebase
        val name = etName.text.toString()
        val lastName = etLastName.text.toString()
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password = etPassword.text.toString()

        // Example: Save data to a 'users' node
        val user = user(name, lastName, username, email, password)
        val userReference = database.child("users").push()
        userReference.setValue(user)

        // Start the next activity
        val intent = Intent(this, menu::class.java)
        startActivity(intent)
        finish() // Optional: Close the current activity if needed
    }
}
