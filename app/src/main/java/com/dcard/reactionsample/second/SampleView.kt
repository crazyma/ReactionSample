package com.dcard.reactionsample.second

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class SampleView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val radius = 50f
    private var scaleRadius = radius
    private var previousScale = 1f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
    }
    private var centerX = 0f
    private var centerY = 0f

    private var animator: ValueAnimator? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2f
        centerY = h / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(centerX, centerY, scaleRadius, paint)
    }

    fun bigger() {
        cancel()

        animator = ValueAnimator.ofFloat(previousScale, 2f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            addUpdateListener {
                previousScale = it.animatedValue as Float
                scaleRadius = radius * previousScale

                postInvalidate()
            }
        }.apply { start() }

    }

    fun smaller() {
        cancel()

        animator = ValueAnimator.ofFloat(previousScale, 0.5f).apply {
            duration = 1000
            interpolator = LinearInterpolator()
            addUpdateListener {
                previousScale = it.animatedValue as Float
                scaleRadius = radius * previousScale

                postInvalidate()
            }
        }.apply { start() }

    }

    fun cancel(){
        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
        }
    }

}