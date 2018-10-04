package com.dcard.reactionsample

import android.animation.Animator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
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

    fun showReactionView(childX: Int, childY: Int, childWidth: Int, childHeight: Int) {

        if (emojiView != null) {
            emojiView!!.layoutParams = createParams(childX, childY, childWidth, childHeight)
        } else {
            emojiView = EmojiView(context).apply {
                //                this.setBackgroundColor(Color.GREEN)
                id = R.id.id_root_view
                ViewCompat.setElevation(this, 32f)
                isLaunchFromBottom = isLaunchAnimationBeginFromBottom(childY)
            }

            addView(emojiView, createParams(childX, childY, childWidth, childHeight))
            customTouchEventListener = emojiView
        }

        emojiView?.run {
            isLaunchFromBottom = isLaunchAnimationBeginFromBottom(childY)
            calculateParabolaEndPosition(childX, childY, childWidth, childHeight).also {
                parabolaEndX = it[0]
                parabolaEndY = it[1]
            }
            parabolaEndSize = 24
        }

        post {
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
    private fun createParams(childX: Int, childY: Int, childWidth: Int, childHeight: Int): FrameLayout.LayoutParams {

        reactionViewWidth = (ReactionConstants.getReactionViewSize(5) * dp).toInt()

        return FrameLayout.LayoutParams(reactionViewWidth, reactionViewHeight).apply {
            when {
                childY <= reactionTopBound && childX < reactionRightBound -> {
                    //  left & top
                    setMargins(childX, childY, 0, 0)
                }

                childY <= reactionTopBound && childX >= reactionRightBound -> {
                    //  right & top
                    setMargins(childX + childWidth - reactionViewWidth, childY,
                            0, 0)
                }

                childY > reactionTopBound && childX < reactionRightBound -> {
                    //  left & bottom
                    setMargins(childX, childY + childHeight - reactionViewHeight,
                            0, 0)
                }

                childY > reactionTopBound && childX >= reactionRightBound -> {
                    //  right & bottom
                    setMargins(
                            childX + childWidth - reactionViewWidth,
                            childY + childHeight - reactionViewHeight, 0, 0)
                }
            }
        }
    }

    private fun calculateParabolaEndPosition(childX: Int, childY: Int, childWidth: Int, childHeight: Int): IntArray {
        reactionViewWidth = (ReactionConstants.getReactionViewSize(5) * dp).toInt()

        return when {
            childY <= reactionTopBound && childX < reactionRightBound -> {
                //  left & top
                intArrayOf(0, 0)

            }

            childY <= reactionTopBound && childX >= reactionRightBound -> {
                //  right & top
                intArrayOf(reactionViewWidth - childWidth, 0)
            }

            childY > reactionTopBound && childX < reactionRightBound -> {
                //  left & bottom
                intArrayOf(0, reactionViewHeight - childHeight)
            }

            childY > reactionTopBound && childX >= reactionRightBound -> {
                //  right & bottom
                intArrayOf(reactionViewWidth - childWidth, reactionViewHeight - childHeight)
            }

            else -> {
                intArrayOf(0, 0)
            }
        }
    }

    /**
     * the launch animation of ReactionView could begin from the bottom of [EmojiView]
     * if touch position is not too high
     */
    private fun isLaunchAnimationBeginFromBottom(touchY: Int) =
            touchY > reactionTopBound


    fun hideReactionView() {
        if (emojiView == null)
            return

        runExitAnim()
    }

    fun isReactionViewShowing() = emojiView != null && emojiView!!.alpha == 1f
}