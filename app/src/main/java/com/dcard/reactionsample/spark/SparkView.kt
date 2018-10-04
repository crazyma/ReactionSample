package com.dcard.reactionsample.spark

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class SparkView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val dp = Resources.getSystem().displayMetrics.density

    val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val biggerCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }
    val smallCirclePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }


    var iconSize: Int = (24 * dp).toInt()
    var iconCurrentSize = iconSize


    var biggerCircleCurrentRadius = 0f
    var smallerCircleCurrentRadius = 0f
    var biggerCircleStartRadius = iconSize / 2f
    var smallerCircleStartRadius = 0f
    val maxCircleRadius = 24f * dp

    var centerX = 0
    var centerY = 0

    var iconBitmap: Bitmap? = null
     set(value) {
         field = value
         postInvalidate()
     }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
    }

    fun runSpark(){
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 200
            interpolator = LinearInterpolator()
            addUpdateListener {
                val fraction = it.animatedValue as Float

                calculateCircleRadius(fraction)

                postInvalidate()
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
        }.apply { start() }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), biggerCircleCurrentRadius, biggerCirclePaint)
        canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), smallerCircleCurrentRadius, smallCirclePaint)

        iconBitmap?.run {
            val offset = iconCurrentSize / 2
            canvas.drawBitmap(this, null, Rect(centerX - offset, centerY - offset, centerX + offset, centerY + offset), iconPaint)
        }
    }

    private fun calculateCircleRadius(fraction: Float){

        biggerCircleCurrentRadius = calculateInterpolatedValue(biggerCircleStartRadius, maxCircleRadius, fraction)
        smallerCircleCurrentRadius = calculateInterpolatedValue(smallerCircleStartRadius, maxCircleRadius, fraction)

    }

    private fun calculateInterpolatedValue(start: Float, end: Float, fraction: Float) =
            start + fraction * (end - start)

    private fun calculateInterpolatedValue(start: Int, end: Int, fraction: Float) =
            start + fraction * (end - start)

}