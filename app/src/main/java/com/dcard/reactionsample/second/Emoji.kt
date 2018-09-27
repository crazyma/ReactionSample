package com.dcard.reactionsample.second

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable

class Emoji: InterpolatedCalculator(){

    var drawable: Drawable? = null
    var bitmap: Bitmap? = null
    var resourceId: Int = 0
    var normalSize: Int = 0
    var smallSize: Int = 0
    var bigSize: Int = 0

    var baseLine = 0    //  要先設定

    var currentX = 0
    var currentY = 0
    var currentSize = 0

    var beginY = 0
    var endY = 0
    var beginSize = 0
    var endSize = 0

    fun drawEmoji(canvas: Canvas, paint: Paint) {

        bitmap?.apply {
            canvas.drawBitmap(this, null, Rect(currentX, currentY, currentX + currentSize, currentY + currentSize), paint)
        }
    }

    fun calculateCurrentY(fraction: Float){
        currentY = calculateInterpolatedValue(beginY, endY, fraction).toInt()
    }

    fun calculateCurrentSize(fraction: Float){
        currentSize = calculateInterpolatedValue(beginSize, endSize, fraction).toInt()
    }

}