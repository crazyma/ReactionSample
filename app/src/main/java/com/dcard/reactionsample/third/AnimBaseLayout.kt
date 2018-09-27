package com.dcard.reactionsample.third

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class AnimBaseLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val targetWidth = 40f
    private var firstCircleX = 0f
    private var firstCircleY = 0f
    private var secondCircleX = 0f
    private var secondCircleY = 0f
    private var controlX = 0f
    private var controlY = 0f

    private var distanceX = 0f
    private var distanceY = 0f
    private var movingCircleX = 0f
    private var movingCircleY = 0f

    private var paint = Paint().apply {
        isAntiAlias = true
        color = Color.BLACK
    }

    fun runAnim(){
        ValueAnimator.ofFloat(0f,1f).apply {
            duration = 300
            interpolator = LinearInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float

                calculateMovingPosition(value)

                postInvalidate()
            }
        }.apply { start() }
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        firstCircleX = w / 3f
        secondCircleX = firstCircleX * 2f

        firstCircleY = h / 3f
        secondCircleY = firstCircleY * 2f

        distanceX = secondCircleX - firstCircleX

        controlX = w / 2f
        controlY = 0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = Color.BLACK
        canvas.drawCircle(firstCircleX, firstCircleY, targetWidth, paint)
        canvas.drawCircle(secondCircleX, secondCircleY, targetWidth, paint)
        paint.color = Color.BLUE
        canvas.drawCircle(movingCircleX, movingCircleY, targetWidth, paint)
    }

    private fun calculateMovingPosition(t: Float){
        movingCircleX = bezierAlg(firstCircleX, controlX, secondCircleX, t)
        movingCircleY = bezierAlg(firstCircleY, controlY, secondCircleY, t)
    }

    private fun bezierAlg(p0: Float, p1: Float, p2: Float, t: Float) =
            (1f - t) * (1f - t) * p0 + 2f * t * (1f - t) * p1 + t * t * p2

}