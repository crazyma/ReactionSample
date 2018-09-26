package com.dcard.reactionsample

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.dcard.reactionsample.second.EmojiView

class ReactionBaseLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    interface CustomTouchEventListener {
        fun onHandleTouchEvent(event: MotionEvent)
    }

    var interruptingTouchEvent = false
    var customTouchEventListener: CustomTouchEventListener? = null
    var reactionRightBound = 0
    var reactionTopBound = 0
    var reactionWidth = 0
    var reactionHeight = 0
    var emojiView: EmojiView? = null

    init {
        reactionWidth = context.resources.getDimensionPixelSize(R.dimen.reaction_width)
        reactionHeight = context.resources.getDimensionPixelSize(R.dimen.reaction_height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        reactionRightBound = (w * 0.7f).toInt()
        reactionTopBound = (h * 0.3).toInt()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        if (interruptingTouchEvent) {
            interruptingTouchEvent = false
            return true
        }

        return super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                hideReactionView()
            }

            else -> {
                customTouchEventListener?.onHandleTouchEvent(event)
            }
        }

        return true
    }

    fun showReactionView(touchX: Int, touchY: Int, childWidth: Int, childHeight: Int) {
        if (emojiView != null) {
            emojiView!!.layoutParams = createParams(touchX, touchY, childWidth, childHeight)
        } else {
            emojiView = EmojiView(context).apply {
                this.setBackgroundColor(Color.GREEN)
                alpha = 0f
                id = R.id.id_root_view
                ViewCompat.setElevation(this, 32f)
            }

            addView(emojiView, createParams(touchX, touchY, childWidth, childHeight))
            customTouchEventListener = emojiView
        }

        runEntryAnim()
    }

    private fun runEntryAnim() {
        val anim1 = ObjectAnimator.ofFloat(emojiView!!, "translationY", 50f, 0f).apply {
            duration = TRANSACTION_DURATION
        }

        val anim2 = ObjectAnimator.ofFloat(emojiView!!, "alpha", 0f, 1f).apply {
            duration = TRANSACTION_DURATION
        }

        AnimatorSet().apply {
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    interruptingTouchEvent = true
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
            playTogether(anim1, anim2)
            start()
        }
    }

    private fun runExitAnim() {
        val anim1 = ObjectAnimator.ofFloat(emojiView!!, "translationY", 0f, 50f).apply {
            duration = TRANSACTION_DURATION
        }

        val anim2 = ObjectAnimator.ofFloat(emojiView!!, "alpha", 1f, 0f).apply {
            duration = TRANSACTION_DURATION
        }

        AnimatorSet().apply {
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {}

            })
            playTogether(anim1, anim2)
            start()
        }
    }

    /**
     * Create proper LayoutParams to show the ReactionView in right position
     */
    private fun createParams(touchX: Int, touchY: Int, childWidth: Int, childHeight: Int) =
            FrameLayout.LayoutParams(reactionWidth, reactionHeight).apply {
                when {
                    touchY <= reactionTopBound && touchX < reactionRightBound -> {
                        //  left & top
                        setMargins(touchX, touchY, 0, 0)
                    }

                    touchY <= reactionTopBound && touchX >= reactionRightBound -> {
                        //  right & top
                        setMargins(touchX + childWidth - reactionWidth, touchY,
                                0, 0)
                    }

                    touchY > reactionTopBound && touchX < reactionRightBound -> {
                        //  left & bottom
                        setMargins(touchX, touchY + childHeight - reactionHeight,
                                0, 0)
                    }

                    touchY > reactionTopBound && touchX >= reactionRightBound -> {
                        //  right & bottom
                        setMargins(
                                touchX + childWidth - reactionWidth,
                                touchY + childHeight - reactionHeight, 0, 0)
                    }
                }
            }

    fun hideReactionView() {
        if (emojiView == null)
            return

        runExitAnim()
    }

    fun isReactionViewShowing() = emojiView != null && emojiView!!.alpha == 1f

    companion object {
        private const val TRANSACTION_DURATION = 120L
    }
}