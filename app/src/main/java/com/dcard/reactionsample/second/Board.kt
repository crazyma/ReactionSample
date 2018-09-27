package com.dcard.reactionsample.second

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.content.ContextCompat

class Board(context: Context) {

    var x = 0f
    var width = 0f
    var currentY = 0f
    var beginBaseY = 0f
    var endBaseY = 0f

    var currentHeight = 0f
    var beginHeight = 0f
    var endHeight = 0f

    private lateinit var paint: Paint

    init {
        initPaint(context)
    }

    fun drawBoard(canvas: Canvas){
        val radius = currentHeight / 2
        val board = RectF(x, currentY - currentHeight, x + width, currentY)
        canvas.drawRoundRect(board, radius, radius, paint)
    }

    private fun initPaint(context: Context){
        paint = Paint().apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            color = ContextCompat.getColor(context, android.R.color.white)
            setShadowLayer(5.0f, 0.0f, 2.0f, -0x1000000)
        }
    }
}