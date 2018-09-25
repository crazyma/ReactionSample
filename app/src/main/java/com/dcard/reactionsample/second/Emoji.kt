package com.dcard.reactionsample.second

import android.graphics.Canvas
import android.graphics.drawable.Drawable

class Emoji {

    var drawable: Drawable? = null
    var resourceId: Int = 0
    var normalSize: Int = 0
    var smallSize: Int = 0
    var bigSize: Int = 0

    var baseLine = 0    //  要先設定
    var currentX = 0
    var currentY = 0
    var currentSize = 0
    var beginSize = 0
    var endSize = 0

    fun drawEmoji(canvas: Canvas){
        drawable?.apply {
            setBounds(currentX, currentY, currentX + currentSize, currentY + currentSize)
            draw(canvas)
        }
    }

}