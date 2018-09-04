package com.dcard.reactionsample

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class AvatarsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var flag = 0

    var originalSize = 0
    var biggerSize = 0
    var smallerSize = 0
    val reaction1 = ContextCompat.getDrawable(context, R.drawable.reaction_smile)
    val reaction2 = ContextCompat.getDrawable(context, R.drawable.reaction_chu)
    val reaction3 = ContextCompat.getDrawable(context, R.drawable.reaction_crazy)
    val reaction4 = ContextCompat.getDrawable(context, R.drawable.reaction_noface)
    val reaction5 = ContextCompat.getDrawable(context, R.drawable.reaction_cry)

    val reactionCount = 5
    val xBoundList = mutableListOf<Int>()
    val yBoundList = mutableListOf<Int>()

    init {
        xBoundList.add(originalSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val count = 5
        originalSize = w / count
        biggerSize = originalSize * 2
        smallerSize = (w - biggerSize) / (count - 1)

        modifyBounding(-1)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        reaction1?.run {
            setBounds(0, yBoundList[0], xBoundList[0], height)
            draw(canvas)
        }
        reaction2?.run {
            setBounds(xBoundList[0], yBoundList[1], xBoundList[1], height)
            draw(canvas)
        }
        reaction3?.run {
            setBounds(xBoundList[1], yBoundList[2], xBoundList[2], height)
            draw(canvas)
        }
        reaction4?.run {
            setBounds(xBoundList[2], yBoundList[3], xBoundList[3], height)
            draw(canvas)
        }
        reaction5?.run {
            setBounds(xBoundList[3], yBoundList[4], width, height)
            draw(canvas)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                if (y in 0..height) {
                    when {
                        x <= originalSize -> {
                            if (flag != 0) {
                                flag = 0
                                modifyBounding(flag)
                                invalidate()
                            }
                        }
                        x <= originalSize * 2 -> {
                            if (flag != 1) {
                                flag = 1
                                modifyBounding(flag)
                                invalidate()
                            }
                        }
                        x <= originalSize * 3 -> {
                            if (flag != 2) {
                                flag = 2
                                modifyBounding(flag)
                                invalidate()
                            }
                        }
                        x <= originalSize * 4 -> {
                            if (flag != 3) {
                                flag = 3
                                modifyBounding(flag)
                                invalidate()
                            }
                        }
                        x <= width -> {
                            if (flag != 4) {
                                flag = 4
                                modifyBounding(flag)
                                invalidate()
                            }
                        }
                        else -> {
                            if (flag != -1) {
                                flag = -1
                                modifyBounding(-1)
                                invalidate()
                            }
                        }
                    }
                } else {
                    if (flag != -1) {
                        flag = -1
                        modifyBounding(-1)
                        invalidate()
                    }
                }
            }
        }

        return true
    }

    private fun modifyBounding(flag: Int){
        modifyYBounding(flag)
        modifyXBounding(flag)
    }

    private fun modifyXBounding(flag: Int) {
        xBoundList.clear()
        var total = 0
        if (flag == -1) {
            for (i in 0 until reactionCount - 1) {
                total += originalSize
                xBoundList.add(total)
            }
        } else {

            for (i in 0 until reactionCount - 1) {
                total += if (flag == i) {
                    biggerSize
                } else {
                    smallerSize
                }

                xBoundList.add(total)
            }
        }
    }

    private fun modifyYBounding(flag: Int) {
        yBoundList.clear()
        val offsetForOriginalSize = height - originalSize
        val offsetForBiggerSize = height - biggerSize
        val offsetForSmallSize = height - smallerSize


        for (i in 0..reactionCount) {
            if (flag == -1) {
                yBoundList.add(offsetForOriginalSize)
            } else {
                if (i == flag) {
                    yBoundList.add(offsetForBiggerSize)
                } else {
                    yBoundList.add(offsetForSmallSize)
                }
            }
        }
    }


}