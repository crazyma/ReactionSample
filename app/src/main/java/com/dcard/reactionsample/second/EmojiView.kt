package com.dcard.reactionsample.second

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.dcard.reactionsample.R
import com.dcard.reactionsample.ReactionBaseLayout

/**
 * Base on [AvatarsView]
 */
class EmojiView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), ReactionBaseLayout.CustomTouchEventListener {

    companion object {
        private const val HOVER_INDEX_NONE = -1

        private const val EMOJI_PADDING_BOTTOM = 80
    }

    val reactionCount = 5
    val reaction1 = ContextCompat.getDrawable(context, R.drawable.reaction_smile)
    val reaction2 = ContextCompat.getDrawable(context, R.drawable.reaction_chu)
    val reaction3 = ContextCompat.getDrawable(context, R.drawable.reaction_crazy)
    val reaction4 = ContextCompat.getDrawable(context, R.drawable.reaction_noface)
    val reaction5 = ContextCompat.getDrawable(context, R.drawable.reaction_cry)

    var hoverIndex = HOVER_INDEX_NONE
    var normalSize = 0
    var biggerSize = 0
    var smallerSize = 0

    private val emojiList = mutableListOf<Emoji>()
    private var animator: ValueAnimator? = null

    private val dp = Resources.getSystem().displayMetrics.density
    private val emojiPaddingBottom = (dp * EMOJI_PADDING_BOTTOM).toInt()

    init {
        emojiList.add(Emoji().apply { drawable = reaction1 })
        emojiList.add(Emoji().apply { drawable = reaction2 })
        emojiList.add(Emoji().apply { drawable = reaction3 })
        emojiList.add(Emoji().apply { drawable = reaction4 })
        emojiList.add(Emoji().apply { drawable = reaction5 })
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        normalSize = w / reactionCount
        biggerSize = normalSize * 2
        smallerSize = (w - biggerSize) / (reactionCount - 1)

        setupInitEmojiSize()
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
                select(HOVER_INDEX_NONE)
            }

            MotionEvent.ACTION_MOVE -> {
                if (x in 0..width && y in 0..height) {

                    for (i in 0 until reactionCount) {
                        if (x <= normalSize * (i + 1)) {
                            if (hoverIndex != i) {
                                select(i)
                            }
                            break
                        }
                    }

                } else {
                    if (hoverIndex != HOVER_INDEX_NONE) {
                        select(HOVER_INDEX_NONE)
                    }
                }
            }
        }

        return true
    }

    override fun onHandleTouchEvent(event: MotionEvent) {
        val x = event.x.toInt() - x.toInt()
        val y = event.y.toInt() - y.toInt()

        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                select(HOVER_INDEX_NONE)
            }

            MotionEvent.ACTION_MOVE -> {
                if (x in 0..width && y in 0..height) {

                    for (i in 0 until reactionCount) {
                        if (x <= normalSize * (i + 1)) {
                            if (hoverIndex != i) {
                                select(i)
                            }
                            break
                        }
                    }

                } else {
                    if (hoverIndex != HOVER_INDEX_NONE) {
                        select(HOVER_INDEX_NONE)
                    }
                }
            }
        }
    }

    private fun select(index: Int) {
        hoverIndex = index

        if (index < 0 || index > reactionCount) {
            runEmojiAnim { setupEmojiAnimToNormalState() }
        } else {
            runEmojiAnim { setupEmojiAnimToHoverState(index) }
        }
    }

    private fun setupInitEmojiSize(){
        for (i in 0 until emojiList.size) {
            emojiList[i].currentX = i * normalSize
            emojiList[i].currentY = height - emojiPaddingBottom - normalSize
            emojiList[i].currentSize = normalSize
        }
    }

    private fun setupEmojiAnimToNormalState() {

        for (i in 0 until emojiList.size) {
            emojiList[i].beginSize = emojiList[i].currentSize
            emojiList[i].endSize = normalSize
        }
    }

    private fun setupEmojiAnimToHoverState(hoverIndex: Int) {

        if (hoverIndex < 0 || hoverIndex > emojiList.size) return

        for (i in 0 until emojiList.size) {
            emojiList[i].beginSize = emojiList[i].currentSize

            if (i == hoverIndex) {
                emojiList[i].endSize = biggerSize
            } else {
                emojiList[i].endSize = smallerSize
            }
        }
    }

    private fun runEmojiAnim(setupEmojiSize: () -> Unit) {

        setupEmojiSize()

        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
        }

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 100
            interpolator = LinearInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float

                calculateEmojiSize(value)

                postInvalidate()
            }
        }.apply { start() }

    }

    private fun calculateEmojiSize(interpolatedValue: Float) {
        for (i in 0 until emojiList.size) {
            emojiList[i].currentSize = getAnimatedSize(i, interpolatedValue)
            emojiList[i].currentY = height - emojiPaddingBottom - emojiList[i].currentSize
        }

        calculateCoordinateX()
    }

    private fun getAnimatedSize(position: Int, interpolatedValue: Float): Int {
        val changeSize = emojiList[position].endSize - emojiList[position].beginSize
        return emojiList[position].beginSize + (interpolatedValue * changeSize).toInt()
    }

    private fun calculateCoordinateX() {
        //  the first emoji
        emojiList[0].currentX = 0

        //  the last emoji
        emojiList.last().currentX = width - emojiList.last().currentSize

        //  the emojis before the hover index
        for (i in 1 until hoverIndex) {
            emojiList[i].currentX = emojiList[i - 1].currentX + emojiList[i - 1].currentSize
        }

        //  the emojis before after hover index
        for (i in emojiList.size - 2 downTo hoverIndex + 1) {
            emojiList[i].currentX = emojiList[i + 1].currentX - emojiList[i].currentSize
        }

        //  the hover emoji
        if (hoverIndex > 0 && hoverIndex != emojiList.size - 1) {
            if (hoverIndex <= (emojiList.size / 2 - 1)) {
                emojiList[hoverIndex].currentX = emojiList[hoverIndex - 1].currentX + emojiList[hoverIndex - 1].currentSize
            } else {
                emojiList[hoverIndex].currentX = emojiList[hoverIndex + 1].currentX - emojiList[hoverIndex].currentSize
            }
        }
    }

}