package com.dcard.reactionsample

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
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

    private val dp = Resources.getSystem().displayMetrics.density

    var interruptingTouchEvent = false
    var customTouchEventListener: CustomTouchEventListener? = null
    var reactionRightBound = 0
    var reactionTopBound = 0
    var reactionViewWidth = 0
    var reactionViewHeight = 0
    var emojiView: EmojiView? = null

    init {
        reactionViewWidth = context.resources.getDimensionPixelSize(R.dimen.width_reaction)
        reactionViewHeight = context.resources.getDimensionPixelSize(R.dimen.height_reaction)
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
        customTouchEventListener?.onHandleTouchEvent(event)

        return true
    }

    fun showReactionView(touchX: Int, touchY: Int, childWidth: Int, childHeight: Int) {
        if (emojiView != null) {
            emojiView!!.layoutParams = createParams(touchX, touchY, childWidth, childHeight)
        } else {
            emojiView = EmojiView(context).apply {
                this.setBackgroundColor(Color.GREEN)
                id = R.id.id_root_view
                alpha = 0f
                ViewCompat.setElevation(this, 32f)
            }

            addView(emojiView, createParams(touchX, touchY, childWidth, childHeight))
            customTouchEventListener = emojiView
        }

        emojiView!!.post {
            runEntryAnim()
        }
    }

    private fun runEntryAnim() {
        ObjectAnimator.ofFloat(emojiView!!, "alpha", 1f, 1f).apply {
            duration = ReactionConstants.DURATION_TRANSACTION
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                    interruptingTouchEvent = true
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {
                    emojiView!!.runEntryAnim()
                }

            })
        }.start()
    }

    private fun runExitAnim() {
        ObjectAnimator.ofFloat(emojiView!!, "alpha", 1f, 1f).apply {
            duration = ReactionConstants.DURATION_TRANSACTION
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(animation: Animator?) {}

                override fun onAnimationEnd(animation: Animator?) {
                }

                override fun onAnimationCancel(animation: Animator?) {}

                override fun onAnimationStart(animation: Animator?) {
                    emojiView!!.runExitAnim()
                }

            })
        }.start()
    }

    /**
     * Create proper LayoutParams to show the ReactionView in right position
     */
    private fun createParams(touchX: Int, touchY: Int, childWidth: Int, childHeight: Int): FrameLayout.LayoutParams {

        reactionViewWidth = (ReactionConstants.getReactionViewSize(5) * dp).toInt()

        return FrameLayout.LayoutParams(reactionViewWidth, reactionViewHeight).apply {
            when {
                touchY <= reactionTopBound && touchX < reactionRightBound -> {
                    //  left & top
                    setMargins(touchX, touchY, 0, 0)
                }

                touchY <= reactionTopBound && touchX >= reactionRightBound -> {
                    //  right & top
                    setMargins(touchX + childWidth - reactionViewWidth, touchY,
                            0, 0)
                }

                touchY > reactionTopBound && touchX < reactionRightBound -> {
                    //  left & bottom
                    setMargins(touchX, touchY + childHeight - reactionViewHeight,
                            0, 0)
                }

                touchY > reactionTopBound && touchX >= reactionRightBound -> {
                    //  right & bottom
                    setMargins(
                            touchX + childWidth - reactionViewWidth,
                            touchY + childHeight - reactionViewHeight, 0, 0)
                }
            }
        }
    }

    fun hideReactionView() {
        if (emojiView == null)
            return

        runExitAnim()
    }

    fun isReactionViewShowing() = emojiView != null && emojiView!!.alpha == 1f
}