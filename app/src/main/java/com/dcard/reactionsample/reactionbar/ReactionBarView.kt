package com.dcard.reactionsample.reactionbar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
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

    init {
        Log.d("badu", "icon size : $iconSize")
        list.add(bitmap1)
        list.add(bitmap2)
        list.add(bitmap5)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        setMeasuredDimension(getWidth(list.size).toInt(), iconSize.toInt())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

//        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val layerID = canvas.saveLayer(RectF(0f, 0f, getWidth(list.size), iconSize), paint)

        var circleX: Float
        for(i in list.size - 1 downTo 0){
            circleX = getCircleX(i)
            Log.d("badu","circleX : $circleX")
            canvas.drawBitmap(list[i], null, RectF(circleX - radius, 0f, circleX + radius, iconSize), paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
            if(i != 0){
                circleX = getCircleX(i -1)
                canvas.drawCircle(circleX, radius, maskRadius, paint)
                paint.xfermode = null
            }
        }
        canvas.restoreToCount(layerID)

//        Log.d("badu","circleX :")
//        canvas.drawBitmap(list[1], null, RectF(getCircleX(1) - radius, 0f, getCircleX(1) + radius, iconSize), paint)
//        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
//        canvas.drawCircle(getCircleX(0), radius, maskRadius, paint)
//        paint.xfermode = null
//        canvas.drawBitmap(list[0], null, RectF(getCircleX(0) - radius, 0f, getCircleX(0) + radius, iconSize), paint)
//
//        canvas.restoreToCount(layerID)
    }

    fun getMaskedBitmap() {
        val canvas = Canvas(bitmap1)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
        canvas.drawBitmap(bitmap2, null, Rect(60, 0, 72 + 60, 72), paint)
    }

    private fun getCircleX(n: Int) =
            (2 * n + 1) * radius - n * offset

    private fun getWidth(size: Int) =
            when (size) {
                0 -> 0f
                else -> getCircleX(size - 1) + radius
            }

}