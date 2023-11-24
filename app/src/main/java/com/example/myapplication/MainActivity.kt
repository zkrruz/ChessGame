package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ChessModel
import com.example.myapplication.ChessPiece
import com.example.myapplication.Square
import com.example.myapplication.databinding.ActivityMainBinding

const val TAG="MainActivity"

class MainActivity : AppCompatActivity(),ChessDelegate {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.chessview.chessDelegate=this
        binding.btnReset.setOnClickListener {
            ChessModel.reset()
            binding.chessview.invalidate()
        }

        Log.d(TAG,"$ChessModel")
    }

    override fun pieceAt(square: Square): ChessPiece? {
        return ChessModel.pieceAt(square)
    }

    override fun movePiece(from:Square,to:Square) {
        ChessModel.movePiece(from,to)
        binding.chessview.invalidate()
    }



    // Обработка нажатия кнопки "Back"
    override fun onBackPressed() {
        // Показываем диалоговое окно с подтверждением
        AlertDialog.Builder(this)
            .setMessage("Вы уверены, что хотите закрыть приложение?")
            .setPositiveButton("Да") { _, _ ->
                // Если пользователь выбрал "Да", закрываем приложение
                finishAffinity()
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    fun Back(view: View) {
        AlertDialog.Builder(this)
            .setMessage("Вы уверены, что хотите покинуть игру?")
            .setPositiveButton("Да") { _, _ ->
                // Если пользователь выбрал "Да", закрываем текущую активность
                finish()

                // Запускаем новую активность (menu::class.java)
                val intent = Intent(this, menu::class.java)
                startActivity(intent)
            }
            .setNegativeButton("Нет", null)
            .show()
    }

}