package com.example.myapplication

// Определение данных класса, представляющего шахматную фигуру
data class ChessPiece(
    val col: Int,          // Колонка, в которой находится фигура
    val row: Int,          // Ряд, в котором находится фигура
    val player: ChessPlayer,  // Игрок, которому принадлежит фигура (белые или черные)
    val rank: ChessRank,    // Ранг фигуры (пешка, ладья, конь и т. д.)
    val resId: Int          // Идентификатор ресурса изображения для фигуры
)

