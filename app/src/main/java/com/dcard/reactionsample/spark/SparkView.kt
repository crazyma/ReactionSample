package com.dcard.reactionsample.spark

import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.dcard.reactionsample.ReactionConstants

class SparkView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {


    private val dp = Resources.getSystem().displayMetrics.density

    val iconPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.STROKE
    }
    val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        style = Paint.Style.FILL
    }

    val iconOriginalSize: Int = (24 * dp).toInt()
    val iconShrinkSize: Int = (12 * dp).toInt()
    var iconStartSize = 0
    var iconEndSize = 0


    val dotStartDistance = 16f * dp
    val dotEndDistance = 24f * dp
    val dotStartRadius = 4f * dp
    val dotEndRadius = 0f
    val circleStartRadius = iconOriginalSize / 4f
    val circleEndRadius = 18f * dp
    val circleStartStrokeWidth = iconOriginalSize / 4f
    val circleEndStrokeWidth = 0f

    //  Current Variable
    var iconCurrentSize = iconOriginalSize
    var currentCircleRadius = 0f
    var currentCircleStrokeWidth = circleStartStrokeWidth
    var dotCurrentDistance = 0f
    var biggerDotCurrentRadius = 0f
    var smallerDotCurrentRadius = 0f

    var centerX = 0
    var centerY = 0
    var isLongPressCancaled = false

    var iconBitmap: Bitmap? = null
        set(value) {
            field = value
            postInvalidate()
        }

    private var animator: ValueAnimator? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX = w / 2
        centerY = h / 2
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt() - x.toInt()
        val y = event.y.toInt() - y.toInt()

        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                runShrinkAnim()
                true
            }
            MotionEvent.ACTION_UP -> {

                isLongPressCancaled = true
                runStretchAnim()

                true
            }
            else -> {
                super.onTouchEvent(event)
            }
        }

    }

    override fun performClick(): Boolean {
        runSpark()
        return super.performClick().apply {
            if (this)
                runSpark()
        }

    }

    private fun runShrinkAnim() {

        setupShrinkAnim()
        runIconAnim()
    }

    private fun runStretchAnim() {

        setupStretchAnim()
        runIconAnim()
    }

    private fun setupShrinkAnim() {
        iconStartSize = iconCurrentSize
        iconEndSize = iconShrinkSize
    }

    private fun setupStretchAnim() {
        iconStartSize = iconCurrentSize
        iconEndSize = iconOriginalSize
    }

    private fun calculateIconSize(fraction: Float) {
        iconCurrentSize = calculateInterpolatedValue(iconStartSize, iconEndSize, fraction).toInt()
    }

    private fun runIconAnim() {
        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
        }

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = ReactionConstants.DURATION_HOVER
            interpolator = LinearInterpolator()
            addUpdateListener {
                val fraction = it.animatedValue as Float

                calculateIconSize(fraction)

                postInvalidate()
            }
        }.apply { start() }
    }


    fun runSpark() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 500
            interpolator = LinearInterpolator()
            addUpdateListener {
                val fraction = it.animatedValue as Float

                calculateCircleRadius(fraction)
                calculateDotDistance(fraction)
                calculateBiggerDotAppearance(fraction)
                calculateSmallerAppearance(fraction)

                postInvalidate()
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    currentCircleRadius = 0f
                    postInvalidate()
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
        }.apply { start() }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // smaller dot
        for (i in 0 until 6) {
            val angdeg = 60.0 * i
            val xx = dotCurrentDistance * Math.cos(Math.toRadians(angdeg))
            val yy = dotCurrentDistance * Math.sin(Math.toRadians(angdeg))
            canvas.drawCircle(centerX.toFloat() + xx.toFloat(), centerY.toFloat() - yy.toFloat(), smallerDotCurrentRadius, dotPaint)
        }

        //  bigger dot
        for (i in 0 until 6) {
            val angdeg = 60.0 * i + 30.0
            val xx = dotCurrentDistance * Math.cos(Math.toRadians(angdeg))
            val yy = dotCurrentDistance * Math.sin(Math.toRadians(angdeg))
            canvas.drawCircle(centerX.toFloat() + xx.toFloat(), centerY.toFloat() - yy.toFloat(), biggerDotCurrentRadius, dotPaint)
        }

        circlePaint.strokeWidth = currentCircleStrokeWidth
        if (circlePaint.strokeWidth > 0f) {
            canvas.drawCircle(centerX.toFloat(), centerY.toFloat(), currentCircleRadius, circlePaint)
        }

        iconBitmap?.run {
            val offset = iconCurrentSize / 2
            canvas.drawBitmap(this, null, Rect(centerX - offset, centerY - offset, centerX + offset, centerY + offset), iconPaint)
        }
    }

    private fun calculateCircleRadius(fraction: Float) {
        val v = when {
            fraction <= .5f -> {
                fraction * 2f
            }
            else -> {
                1f
            }
        }

        currentCircleRadius = calculateInterpolatedValue(circleStartRadius, circleEndRadius, v)
        currentCircleStrokeWidth = calculateInterpolatedValue(circleStartStrokeWidth, circleEndStrokeWidth, v)
    }

    private fun calculateDotDistance(fraction: Float) {
        dotCurrentDistance = calculateInterpolatedValue(dotStartDistance, dotEndDistance, fraction)
    }

    private fun calculateBiggerDotAppearance(fraction: Float) {
        biggerDotCurrentRadius = calculateInterpolatedValue(dotStartRadius, dotEndRadius, fraction)
    }

    private fun calculateSmallerAppearance(fraction: Float) {
        val v = when {
            fraction <= .5f -> {
                fraction * 2f
            }
            else -> {
                1f
            }
        }
        smallerDotCurrentRadius = calculateInterpolatedValue(dotStartRadius, dotEndRadius, v)
    }

    private fun calculateInterpolatedValue(start: Float, end: Float, fraction: Float) =
            start + fraction * (end - start)

    private fun calculateInterpolatedValue(start: Int, end: Int, fraction: Float) =
            start + fraction * (end - start)

}