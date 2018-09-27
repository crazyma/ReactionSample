package com.dcard.reactionsample.second

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import com.dcard.reactionsample.R
import com.dcard.reactionsample.ReactionBaseLayout
import com.dcard.reactionsample.ReactionConstants

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

        private const val STATE_ENTRY = 0x10
        private const val STATE_EXIT = 0x20
        private const val STATE_INTERACTING = 0x30
        private const val STATE_DISABLE = 0x40

        private const val EMOJI_PADDING_BOTTOM = 40
    }

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    val reactionCount = 5
    val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.reaction_smile)
    val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.reaction_chu)
    val bitmap3 = BitmapFactory.decodeResource(resources, R.drawable.reaction_crazy)
    val bitmap4 = BitmapFactory.decodeResource(resources, R.drawable.reaction_noface)
    val bitmap5 = BitmapFactory.decodeResource(resources, R.drawable.reaction_cry)

    var hoverIndex = HOVER_INDEX_NONE
    var normalSize = 0
    var biggerSize = 0
    var smallerSize = 0
    var spacing = 0

    private val emojiList = mutableListOf<Emoji>()
    private val board = Board(context)
    private var animator: ValueAnimator? = null
    private var state = STATE_DISABLE

    private val dp = Resources.getSystem().displayMetrics.density
    private val emojiPaddingBottom = (dp * EMOJI_PADDING_BOTTOM).toInt()

    init {
        emojiList.add(Emoji().apply { bitmap = bitmap1 })
        emojiList.add(Emoji().apply { bitmap = bitmap2 })
        emojiList.add(Emoji().apply { bitmap = bitmap3 })
        emojiList.add(Emoji().apply { bitmap = bitmap4 })
        emojiList.add(Emoji().apply { bitmap = bitmap5 })

        normalSize = (ReactionConstants.getNormalSize(reactionCount) * dp).toInt()
        biggerSize = (ReactionConstants.SIZE_LARGE * dp).toInt()
        smallerSize = (ReactionConstants.SIZE_SMALL * dp).toInt()
        spacing = (ReactionConstants.SPACING * dp).toInt()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

//        setupInitEmojiSize()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        board.drawBoard(canvas)

        emojiList.forEach {
            it.drawEmoji(canvas, paint)
        }

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x.toInt()
        val y = event.y.toInt()
        if (state == STATE_INTERACTING) {
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
        return false
    }

    override fun onHandleTouchEvent(event: MotionEvent) {
        val x = event.x.toInt() - x.toInt()
        val y = event.y.toInt() - y.toInt()

        if (state == STATE_INTERACTING) {
            when (event.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    select(HOVER_INDEX_NONE)
                }

                MotionEvent.ACTION_MOVE -> {

                    if (state == STATE_INTERACTING) {
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
        }
    }

    fun runEntryAnim() {
        state = STATE_ENTRY

        setupEmojiAnimFromEntryState()

        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
        }

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 100
            interpolator = LinearInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float

                calculateEmojiPosition(value)

                postInvalidate()
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    state = STATE_INTERACTING
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
        }.apply { start() }
    }

    fun runExitAnim() {
        state = STATE_EXIT

        setupEmojiAnimToExitState()

        if (animator != null && animator!!.isRunning) {
            animator!!.cancel()
        }

        animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 100
            interpolator = LinearInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Float

                calculateEmojiPosition(value)

                postInvalidate()
            }

            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    state = STATE_DISABLE
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
        }.apply { start() }

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

    private fun select(index: Int) {
        hoverIndex = index

        if (index < 0 || index > reactionCount) {
            runEmojiAnim { setupEmojiAnimToNormalState() }
        } else {
            runEmojiAnim { setupEmojiAnimToHoverState(index) }
        }
    }

    /**
     * the init setting in entry anim right now
     */
    @Deprecated("useless")
    private fun setupInitEmojiSize() {

        board.width = width.toFloat()
        board.currentHeight = (normalSize + spacing * 2).toFloat()
        board.x = 0f
        board.currentY = (height - emojiPaddingBottom + spacing).toFloat()

        for (i in 0 until emojiList.size) {
            emojiList[i].currentX = i * normalSize + (i + 1) * spacing
            emojiList[i].currentY = height - emojiPaddingBottom - normalSize
            emojiList[i].currentSize = normalSize
        }
    }

    private fun setupEmojiAnimFromEntryState() {

        val offset = normalSize / 2

        //  init value
        board.width = width.toFloat()
        board.x = 0f

        //  anim preparation
        board.beginHeight = (normalSize + spacing * 2).toFloat()
        board.endHeight = board.beginHeight
        board.currentY = board.beginBaseY

        board.beginBaseY = (height - emojiPaddingBottom + spacing + offset).toFloat()
        board.endBaseY = (height - emojiPaddingBottom + spacing).toFloat()
        board.currentY = board.beginBaseY

        for (i in 0 until emojiList.size) {
            emojiList[i].beginSize = normalSize
            emojiList[i].endSize = normalSize
            emojiList[i].beginY = height - emojiPaddingBottom - normalSize + offset
            emojiList[i].endY = height - emojiPaddingBottom - normalSize
            emojiList[i].currentY = emojiList[i].beginY
        }
    }

    private fun setupEmojiAnimToExitState() {
        val offset = normalSize / 2

        //  anim preparation
        board.beginHeight = board.currentHeight
        board.endHeight = (normalSize + spacing * 2).toFloat()

        board.beginBaseY = board.currentY
        board.endBaseY = (height - emojiPaddingBottom + spacing + offset).toFloat()

        for (i in 0 until emojiList.size) {

            //  anim preparation
            emojiList[i].beginSize = emojiList[i].currentSize
            emojiList[i].endSize = normalSize

            emojiList[i].beginY = emojiList[i].currentY
            emojiList[i].endY = height - emojiPaddingBottom - normalSize + offset
        }
    }

    private fun setupEmojiAnimToNormalState() {
        //  anim preparation
        board.beginHeight = board.currentHeight
        board.endHeight = (normalSize + 2 * spacing).toFloat()

        for (i in 0 until emojiList.size) {
            //  anim preparation
            emojiList[i].beginSize = emojiList[i].currentSize
            emojiList[i].endSize = normalSize
        }
    }

    private fun setupEmojiAnimToHoverState(hoverIndex: Int) {

        if (hoverIndex < 0 || hoverIndex > emojiList.size) return

        board.beginHeight = board.currentHeight
        board.endHeight = (smallerSize + 2 * spacing).toFloat()

        for (i in 0 until emojiList.size) {
            emojiList[i].beginSize = emojiList[i].currentSize

            if (i == hoverIndex) {
                emojiList[i].endSize = biggerSize
            } else {
                emojiList[i].endSize = smallerSize
            }
        }
    }

    private fun calculateEmojiPosition(interpolatedValue: Float) {

        board.currentY = getBoardAnimatedY(interpolatedValue)
        board.currentHeight = getBoardAnimatedSize(interpolatedValue)

        for (i in 0 until emojiList.size) {
            emojiList[i].currentSize = getEmojiAnimatedSize(i, interpolatedValue)
            emojiList[i].currentY = getEmojiAnimatedY(i, interpolatedValue)
        }

        calculateCoordinateX()
    }

    private fun calculateEmojiSize(interpolatedValue: Float) {

        board.currentHeight = getBoardAnimatedSize(interpolatedValue)

        for (i in 0 until emojiList.size) {
            emojiList[i].currentSize = getEmojiAnimatedSize(i, interpolatedValue)
            emojiList[i].currentY = height - emojiPaddingBottom - emojiList[i].currentSize
        }

        calculateCoordinateX()
    }

    private fun getEmojiAnimatedY(position: Int, interpolatedValue: Float): Int {
        val changeY = emojiList[position].endY - emojiList[position].beginY
        return emojiList[position].beginY + (interpolatedValue * changeY).toInt()
    }

    private fun getEmojiAnimatedSize(position: Int, interpolatedValue: Float): Int {
        val changeSize = emojiList[position].endSize - emojiList[position].beginSize
        return emojiList[position].beginSize + (interpolatedValue * changeSize).toInt()
    }

    private fun getBoardAnimatedY(interpolatedValue: Float): Float {
        val changeY = board.endBaseY - board.beginBaseY
        return board.beginBaseY + interpolatedValue * changeY
    }

    private fun getBoardAnimatedSize(interpolatedValue: Float): Float {
        val changeSize = board.endHeight - board.beginHeight
        return board.beginHeight + interpolatedValue * changeSize
    }

    private fun calculateCoordinateX() {
        //  the first emoji
        emojiList[0].currentX = spacing

        //  the last emoji
        emojiList.last().currentX = width - spacing - emojiList.last().currentSize

        //  the emojis before the hover index
        for (i in 1 until hoverIndex) {
            emojiList[i].currentX = emojiList[i - 1].currentX + emojiList[i - 1].currentSize + spacing
        }

        //  the emojis before after hover index
        for (i in emojiList.size - 2 downTo hoverIndex + 1) {
            emojiList[i].currentX = emojiList[i + 1].currentX - emojiList[i].currentSize - spacing
        }

        //  the hover emoji
        if (hoverIndex > 0 && hoverIndex != emojiList.size - 1) {
            if (hoverIndex <= (emojiList.size / 2 - 1)) {
                emojiList[hoverIndex].currentX = emojiList[hoverIndex - 1].currentX + emojiList[hoverIndex - 1].currentSize + spacing
            } else {
                emojiList[hoverIndex].currentX = emojiList[hoverIndex + 1].currentX - emojiList[hoverIndex].currentSize - spacing
            }
        }
    }

}