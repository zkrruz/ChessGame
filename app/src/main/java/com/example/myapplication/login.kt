package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class login : AppCompatActivity() {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etUsername = findViewById(R.id.tvUserName)
        etPassword = findViewById(R.id.tvPassword)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()
    }

    // Handle login button click
    fun onLoginClick(view: View) {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        // Check if the username and password match the stored data in Firebase
        checkCredentials(username, password)
    }

    // Check user credentials against Firebase data
    private fun checkCredentials(username: String, password: String) {
        // Example: Query Firebase to check if the user exists and the password is correct
        database.child("users").orderByChild("userName").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (userSnapshot in snapshot.children) {
                            val user = userSnapshot.getValue(user::class.java)
                            if (user?.password == password) {
                                // Password is correct, login successful
                                // You can navigate to the next activity here
                                startActivity(Intent(this@login, menu::class.java))
                                finish()
                            } else {
                                // Password is incorrect
                                // Display an error message or take appropriate action
                            }
                        }
                    } else {
                        // User with the given username does not exist
                        // Display an error message or take appropriate action
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
    }


    // Handle sign-up text click
    fun signUp(view: View) {
        val intent = Intent(this, register::class.java)
        startActivity(intent)
    }
}
