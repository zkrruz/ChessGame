package com.example.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import com.example.myapplication.ChessPiece
import com.example.myapplication.Square
import kotlin.math.min

class ChessBoard(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    // Фактор масштабирования доски
    private val scaleFactor = 0.9f
    private var originX = 1f
    private var originY = 1f
    private var cellSide = 1f
    private val paint = Paint()

    // Список идентификаторов ресурсов изображений шахматных фигур
    private val imageIds = setOf(
        R.drawable.bishop_black,
        R.drawable.bishop_white,
        R.drawable.king_black,
        R.drawable.king_white,
        R.drawable.knight_black,
        R.drawable.knight_white,
        R.drawable.pawn_black,
        R.drawable.pawn_white,
        R.drawable.queen_black,
        R.drawable.queen_white,
        R.drawable.rook_black,
        R.drawable.rook_white
    )

    // Карта для хранения изображений фигур
    private val bitmaps = mutableMapOf<Int, Bitmap>()

    lateinit var chessDelegate: ChessDelegate
    private var fromCol = -1
    private var fromRow = -1
    private var movingX = -1f
    private var movingY = -1f
    private var movingBitmap: Bitmap? = null
    private var movingPiece: ChessPiece? = null

    init {
        loadBitmaps() // Загрузка изображений фигур
    }

    // Обработка измерений приложения
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val smaller = min(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(smaller, smaller)
    }

    // Рисование доски и фигур
    override fun onDraw(canvas: Canvas) {
        canvas ?: return
        val chessboardSide = min(width, height) * scaleFactor
        cellSide = chessboardSide / 8f
        originX = (width - chessboardSide) / 2f
        originY = (height - chessboardSide) / 2f
        drawChessBoard(canvas)
        drawPieces(canvas)
    }

    // Обработка событий касания экрана
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return false
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                fromCol = ((event.x - originX) / cellSide).toInt()
                fromRow = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate.pieceAt(Square(fromCol, fromRow))?.let {
                    movingPiece = it
                    movingBitmap = bitmaps[it.resId]
                }
            }
            MotionEvent.ACTION_UP -> {
                val col = ((event.x - originX) / cellSide).toInt()
                val row = 7 - ((event.y - originY) / cellSide).toInt()
                chessDelegate.movePiece(Square(fromCol, fromRow), Square(col, row))
                movingPiece = null
                movingBitmap = null
            }
            MotionEvent.ACTION_MOVE -> {
                movingX = event.x
                movingY = event.y
                invalidate()
            }
        }
        return true
    }

    // Отрисовка шахматных фигур
    private fun drawPieces(canvas: Canvas) {
        for (row in 0..7) {
            for (col in 0..7) {
                chessDelegate.pieceAt(Square(col, row))?.let {
                    if (it != movingPiece) {
                        drawPieceAt(col, row, canvas, it.resId)
                    }
                }
            }
        }
        movingBitmap?.let {
            // Отрисовка двигающейся фигуры
            canvas.drawBitmap(
                it,
                null,
                RectF(
                    movingX - (cellSide / 2),
                    movingY - (cellSide / 2),
                    movingX + (cellSide / 2),
                    movingY + (cellSide / 2)
                ),
                paint
            )
        }
    }

    // Отрисовка фигуры на доске
    private fun drawPieceAt(col: Int, row: Int, canvas: Canvas, resId: Int) {
        val adjustedRow = 7 - row
        val piece = bitmaps[resId]!!
        canvas.drawBitmap(
            piece,
            null,
            RectF(
                originX + col * cellSide,
                originY + adjustedRow * cellSide,
                originX + (col + 1) * cellSide,
                originY + (adjustedRow + 1) * cellSide
            ),
            paint
        )
    }

    // Отрисовка шахматной доски
    private fun drawChessBoard(canvas: Canvas) {
        for (i in 0..7) {
            for (j in 0..7) {
                paint.color = if ((i + j) % 2 == 0) Color.LTGRAY else Color.DKGRAY
                canvas.drawRect(
                    originX + i * cellSide,
                    originY + j * cellSide,
                    originX + (i + 1) * cellSide,
                    originY + (j + 1) * cellSide,
                    paint
                )
            }
        }
    }

    // Загрузка изображений фигур в карту
    private fun loadBitmaps() {
        imageIds.forEach {
            bitmaps[it] = BitmapFactory.decodeResource(resources, it)
        }
    }
}
