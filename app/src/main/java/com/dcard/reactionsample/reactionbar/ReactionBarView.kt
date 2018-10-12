package com.dcard.reactionsample.reactionbar

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.dcard.reactionsample.R

class ReactionBarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var iconSize = 0f
    private var maskSize = 0f
    private var radius = 0f
    private var maskRadius = 0f
    private var offset = 0f
    private val list = mutableListOf<Bitmap>()
    private lateinit var rectF: RectF
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
    private var reactionAlpha = 0

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.ReactionBarView, 0, 0).apply {
                iconSize = getDimensionPixelSize(R.styleable.ReactionBarView_reactionSize, 48).toFloat()
                offset = getDimensionPixelSize(R.styleable.ReactionBarView_strokeSize, 8).toFloat()
                reactionAlpha = getInteger(R.styleable.ReactionBarView_reactionAlpha, 255)
                maskSize = iconSize + offset
                radius = iconSize / 2f
                maskRadius = maskSize / 2f

                recycle()
            }
        }

        calculateRectF()
    }

    fun addBitmap(bitmap: Bitmap) {
        list.add(bitmap)
        calculateRectF()
        requestLayout()
        invalidate()
    }

    fun addBitmap(l: List<Bitmap>) {
        list.clear()
        list.addAll(l)
        calculateRectF()
        requestLayout()
        postInvalidate()
    }

    fun clearBitmap() {
        list.clear()
        calculateRectF()
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(getWidth(list.size).toInt(), iconSize.toInt())
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //  setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val layerID = canvas.saveLayer(rectF, paint)
        canvas.saveLayerAlpha(rectF,reactionAlpha)

        var circleX: Float

        for (i in list.size - 1 downTo 0) {
            circleX = getCircleX(i)

            canvas.drawBitmap(list[i], null, getBitmapRecF(circleX), paint)
            paint.xfermode = xfermode
            if (i != 0) {
                circleX = getCircleX(i - 1)
                canvas.drawCircle(circleX, radius, maskRadius, paint)
                paint.xfermode = null
            }
        }
        canvas.restoreToCount(layerID)
    }

    private fun calculateRectF() {
        rectF = RectF(0f, 0f, getWidth(list.size), iconSize)
    }

    private fun getCircleX(n: Int) =
            ((2 * n + 1) * radius - n * offset)

    private fun getWidth(size: Int) =
            when (size) {
                0 -> 0f
                else -> getCircleX(size - 1) + radius
            }

    private fun getBitmapRecF(circleX: Float) =
            RectF(circleX - radius, 0f, circleX + radius, iconSize)

}