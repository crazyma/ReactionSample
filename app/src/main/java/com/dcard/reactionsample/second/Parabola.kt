package com.dcard.reactionsample.second

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Parabola {

    var bitmap: Bitmap? = null
    var beginX = 0
    var beginY = 0
    var endX = 0
    var endY = 0
    var controlX = 0
    var controlY = 0
    var beginSize = 0
    var endSize = 0

    var currentX = 0
    var currentY = 0
    var currentSize = 0

    private var paint = Paint().apply { isAntiAlias = true }

    init {
        initPaint()
    }

    fun draw(canvas: Canvas) {
        bitmap?.apply {
            canvas.drawBitmap(this, null, Rect(currentX, currentY, currentX + currentSize, currentY + currentSize), paint)
        }
    }

    private fun initPaint() {
        paint = Paint().apply {
            isAntiAlias = true
        }
    }
}