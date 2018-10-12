package com.dcard.reactionsample.reactionbar

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.dcard.reactionsample.R

class ReactionBarView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val ICON_SIZE = 48
        private const val MASK_SIZE = 56
        private const val OFFSET = 4
    }

    val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.reaction_smile)
    val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.reaction_chu)
    val bitmap5 = BitmapFactory.decodeResource(resources, R.drawable.reaction_cry)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val dp = Resources.getSystem().displayMetrics.density
    private val iconSize = ICON_SIZE * dp
    private val maskSize = MASK_SIZE * dp
    private val radius = iconSize / 2
    private val maskRadius = maskSize / 2
    private val offset = OFFSET * dp
    private val list = mutableListOf<Bitmap>()
    private var rectF: RectF
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)

    init {
        rectF = RectF(0f, 0f, getWidth(list.size), iconSize)
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
            (2 * n + 1) * radius - n * offset

    private fun getWidth(size: Int) =
            when (size) {
                0 -> 0f
                else -> getCircleX(size - 1) + radius
            }

    private fun getBitmapRecF(circleX: Float) =
            RectF(circleX - radius, 0f, circleX + radius, iconSize)

}