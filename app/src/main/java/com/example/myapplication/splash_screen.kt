package com.example.myapplication

import android.content.SharedPreferences
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class splash_screen : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private val SPLASH_TIME_OUT: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        // Получаем значение флага isFirstRun из SharedPreferences
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)

        Handler().postDelayed({
            // This method will be executed once the timer is over
            // Start your app's main activity based on isFirstRun
            val intent = if (isFirstRun) {
                // Если это первый запуск, открываем окно регистрации и входа
                sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
                Intent(this, login::class.java)
            } else {
                // Если не первый запуск, открываем основное меню
                Intent(this, menu::class.java)
            }
            startActivity(intent)
            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}


