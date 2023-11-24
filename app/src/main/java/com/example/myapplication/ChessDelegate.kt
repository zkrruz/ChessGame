package com.example.myapplication

import com.example.myapplication.ChessPiece
import com.example.myapplication.Square

// Интерфейс, предоставляющий методы для взаимодействия с шахматной доской
interface ChessDelegate {
    // Метод для получения фигуры (если она есть) на указанной клетке
    fun pieceAt(square: Square): ChessPiece?

    // Метод для перемещения фигуры с одной клетки на другую
    fun movePiece(from: Square, to: Square)
}