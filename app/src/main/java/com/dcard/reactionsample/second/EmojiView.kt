package com.dcard.reactionsample.second

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dcard.reactionsample.R

/**
 * Base on [AvatarsView]
 */
class EmojiView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var hoverIndex = 0

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
    val emojiList = mutableListOf<Emoji>()

    init {
        xBoundList.add(originalSize)

        emojiList.add(Emoji().apply { drawable = reaction1 })
        emojiList.add(Emoji().apply { drawable = reaction2 })
        emojiList.add(Emoji().apply { drawable = reaction3 })
        emojiList.add(Emoji().apply { drawable = reaction4 })
        emojiList.add(Emoji().apply { drawable = reaction5 })
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

        emojiList.forEach {
            it.drawEmoji(canvas)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                hoverIndex = -1
                modifyBounding(-1)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                if (y in 0..height) {
                    when {
                        x <= originalSize -> {
                            if (hoverIndex != 0) {
                                hoverIndex = 0
                                modifyBounding(hoverIndex)
                                invalidate()
                            }
                        }
                        x <= originalSize * 2 -> {
                            if (hoverIndex != 1) {
                                hoverIndex = 1
                                modifyBounding(hoverIndex)
                                invalidate()
                            }
                        }
                        x <= originalSize * 3 -> {
                            if (hoverIndex != 2) {
                                hoverIndex = 2
                                modifyBounding(hoverIndex)
                                invalidate()
                            }
                        }
                        x <= originalSize * 4 -> {
                            if (hoverIndex != 3) {
                                hoverIndex = 3
                                modifyBounding(hoverIndex)
                                invalidate()
                            }
                        }
                        x <= width -> {
                            if (hoverIndex != 4) {
                                hoverIndex = 4
                                modifyBounding(hoverIndex)
                                invalidate()
                            }
                        }
                        else -> {
                            if (hoverIndex != -1) {
                                hoverIndex = -1
                                modifyBounding(-1)
                                invalidate()
                            }
                        }
                    }
                } else {
                    if (hoverIndex != -1) {
                        hoverIndex = -1
                        modifyBounding(-1)
                        invalidate()
                    }
                }
            }
        }

        return true
    }

    private fun modifyBounding(hoverIndex: Int) {

        if (hoverIndex == -1) {
            for (i in 0 until emojiList.size) {
                emojiList[i].currentX = i * originalSize
                emojiList[i].currentY = height - originalSize
                emojiList[i].currentSize = originalSize
            }
        } else {
            var totalX = 0
            for (i in 0 until emojiList.size) {

                emojiList[i].currentX = totalX

                if (i == hoverIndex) {
                    emojiList[i].currentY = height - biggerSize
                    emojiList[i].currentSize = biggerSize
                    totalX += biggerSize
                } else {
                    emojiList[i].currentY = height - smallerSize
                    emojiList[i].currentSize = smallerSize
                    totalX += smallerSize
                }
            }
        }

    }

}