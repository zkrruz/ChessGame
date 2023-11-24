package com.example.myapplication

import com.example.myapplication.R
import kotlin.math.abs

// Объект, представляющий модель для шахматной доски
object ChessModel {
    private var pieceBox = mutableSetOf<ChessPiece>()

    // Инициализация, сбрасывающая состояние доски
    init {
        reset()
    }

    // Сброс шахматной доски к начальному состоянию
    fun reset() {
        pieceBox.clear()
        for (i in 0..1) {
            pieceBox.add(ChessPiece(0 + i * 7, 0, ChessPlayer.WHITE, ChessRank.ROOK, R.drawable.rook_white))
            pieceBox.add(ChessPiece(0 + i * 7, 7, ChessPlayer.BLACK, ChessRank.ROOK, R.drawable.rook_black))

            pieceBox.add(ChessPiece(1 + i * 5, 0, ChessPlayer.WHITE, ChessRank.KNIGHT, R.drawable.knight_white))
            pieceBox.add(ChessPiece(1 + i * 5, 7, ChessPlayer.BLACK, ChessRank.KNIGHT, R.drawable.knight_black))

            pieceBox.add(ChessPiece(2 + i * 3, 0, ChessPlayer.WHITE, ChessRank.BISHOP, R.drawable.bishop_white))
            pieceBox.add(ChessPiece(2 + i * 3, 7, ChessPlayer.BLACK, ChessRank.BISHOP, R.drawable.bishop_black))
        }
        for (i in 0..7) {
            pieceBox.add(ChessPiece(i, 1, ChessPlayer.WHITE, ChessRank.PAWN, R.drawable.pawn_white))
            pieceBox.add(ChessPiece(i, 6, ChessPlayer.BLACK, ChessRank.PAWN, R.drawable.pawn_black))
        }
        pieceBox.add(ChessPiece(3, 0, ChessPlayer.WHITE, ChessRank.QUEEN, R.drawable.queen_white))
        pieceBox.add(ChessPiece(3, 7, ChessPlayer.BLACK, ChessRank.QUEEN, R.drawable.queen_black))
        pieceBox.add(ChessPiece(4, 0, ChessPlayer.WHITE, ChessRank.KING, R.drawable.king_white))
        pieceBox.add(ChessPiece(4, 7, ChessPlayer.BLACK, ChessRank.KING, R.drawable.king_black))
    }

    // Получение фигуры на указанной клетке
    fun pieceAt(square: Square): ChessPiece? {
        return pieceAt(square.col, square.row)
    }

    // Получение фигуры на указанных координатах
    private fun pieceAt(col: Int, row: Int): ChessPiece? {
        for (piece in pieceBox) {
            if (col == piece.col && row == piece.row) {
                return piece
            }
        }
        return null
    }

    // Проверка возможности перемещения фигуры с одной клетки на другую
    fun canMove(from: Square, to: Square): Boolean {
        if (from.col == to.col && from.row == to.row) {
            return false
        }
        val movingPiece = pieceAt(from) ?: return false
        return when (movingPiece.rank) {
            ChessRank.KNIGHT -> canKnightMove(from, to)
            ChessRank.ROOK -> canRookMove(from, to)
            ChessRank.BISHOP -> canBishopMove(from, to)
            ChessRank.QUEEN -> canQueenMove(from, to)
            ChessRank.KING -> canKingMove(from, to)
            ChessRank.PAWN -> canPawnMove(from, to)
        }
    }

    // Перемещение фигуры с одной клетки на другую
    fun movePiece(from: Square, to: Square) {
        if (canMove(from, to)) {
            movePiece(from.col, from.row, to.col, to.row)
        }
    }

    // Фактическое перемещение фигуры
    private fun movePiece(fromCol: Int, fromRow: Int, toCol: Int, toRow: Int) {
        if (fromCol == toCol && fromRow == toRow) return
        val movingPiece = pieceAt(fromCol, fromRow) ?: return

        pieceAt(toCol, toRow)?.let {
            if (it.player == movingPiece.player) {
                return
            }
            pieceBox.remove(it)
        }
        pieceBox.remove(movingPiece)
        pieceBox.add(ChessPiece(toCol, toRow, movingPiece.player, movingPiece.rank, movingPiece.resId))
    }

    // Проверка возможности хода конем
    private fun canKnightMove(from: Square, to: Square): Boolean {
        return ((abs(from.col - to.col) == 2 && abs(from.row - to.row) == 1) ||
                (abs(from.col - to.col) == 1 && abs(from.row - to.row) == 2))
    }

    // Проверка возможности хода ладьей
    private fun canRookMove(from: Square, to: Square): Boolean {
        return (from.col == to.col && isClearVertical(from, to) || from.row == to.row && isClearHorizontal(from, to))
    }

    // Проверка возможности хода слоном
    private fun canBishopMove(from: Square, to: Square): Boolean {
        if (abs(from.row - to.row) == abs(from.col - to.col)) {
            return isClearDiagonally(from, to)
        }
        return false
    }

    // Проверка возможности хода ферзем (как слоном или ладьей)
    private fun canQueenMove(from: Square, to: Square): Boolean {
        return canBishopMove(from, to) || canRookMove(from, to)
    }

    // Проверка возможности хода королем
    private fun canKingMove(from: Square, to: Square): Boolean {
        return canQueenMove(from, to) && (abs(from.row - to.row) == 1 || abs(from.col - to.col) == 1)
    }

    // Проверка возможности хода пешкой
    private fun canPawnMove(from: Square, to: Square): Boolean {
        if (from.col == to.col) {
            if (from.row == 1) {
                return (to.row == 2 || to.row == 3) && isClearVertical(from, to)
            } else if (from.row == 6) {
                return (to.row == 5 || to.row == 4) && isClearVertical(from, to)
            }
        }
        val pawnColor = pieceAt(from) ?: return false
        when (pawnColor.player) {
            ChessPlayer.WHITE -> {
                if (canQueenMove(from, to) && from.row > 1) {
                    return to.row - from.row == 1
                }
            }
            ChessPlayer.BLACK -> {
                if (canQueenMove(from, to) && from.row < 6) {
                    return from.row - to.row == 1
                }
            }
        }
        return false
    }

    // Проверка отсутствия фигур по горизонтали между двумя клетками
    private fun isClearHorizontal(from: Square, to: Square): Boolean {
        if (from.row != to.row) return false
        val gap = abs(from.col - to.col) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            if (pieceAt(nextCol, from.row) != null) {
                return false
            }
        }
        return true
    }

    // Проверка отсутствия фигур по вертикали между двумя клетками
    private fun isClearVertical(from: Square, to: Square): Boolean {
        if (from.col != to.col) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(from.col, nextRow) != null) {
                return false
            }
        }
        return true
    }

    // Проверка отсутствия фигур по диагонали между двумя клетками
    private fun isClearDiagonally(from: Square, to: Square): Boolean {
        if (abs(from.col - to.col) != abs(from.row - to.row)) return false
        val gap = abs(from.row - to.row) - 1
        if (gap == 0) return true
        for (i in 1..gap) {
            val nextCol = if (to.col > from.col) from.col + i else from.col - i
            val nextRow = if (to.row > from.row) from.row + i else from.row - i
            if (pieceAt(nextCol, nextRow) != null) {
                return false
            }
        }
        return true
    }

    // Переопределение метода toString для отображения текущего состояния доски
    override fun toString(): String {
        var desc = " \n"
        for (row in 7 downTo 0) {
            desc += "$row"
            for (column in 0..7) {
                desc += " "
                desc += pieceAt(column, row)?.let {
                    when (it.rank) {
                        ChessRank.KING -> if (it.player == ChessPlayer.WHITE) "k" else "K"
                        ChessRank.QUEEN -> if (it.player == ChessPlayer.WHITE) "q" else "Q"
                        ChessRank.BISHOP -> if (it.player == ChessPlayer.WHITE) "b" else "B"
                        ChessRank.ROOK -> if (it.player == ChessPlayer.WHITE) "r" else "R"
                        ChessRank.KNIGHT -> if (it.player == ChessPlayer.WHITE) "n" else "N"
                        ChessRank.PAWN -> if (it.player == ChessPlayer.WHITE) "p" else "P"
                    }
                } ?: "."
            }
            desc += "\n"
        }
        desc += "  0 1 2 3 4 5 6 7"
        return desc
    }
}
