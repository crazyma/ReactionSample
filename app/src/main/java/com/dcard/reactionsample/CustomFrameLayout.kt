package com.dcard.reactionsample

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout

class CustomFrameLayout @JvmOverloads constructor(
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
    var reactionView: View? = null

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

        customTouchEventListener?.onHandleTouchEvent(event)

        return true
    }

    fun showReactionView(touchX: Int, touchY: Int, childWidth: Int, childHeight: Int) {
        if (reactionView != null)
            return

        reactionView = RootView(context).apply {
            this.setBackgroundColor(Color.GREEN)
            id = R.id.id_root_view
            ViewCompat.setElevation(this, 32f)
        }


        addView(reactionView, createParams(touchX, touchY, childWidth, childHeight))
    }

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
        if (reactionView == null)
            return

        removeView(reactionView)
        reactionView = null
    }

    fun isReactionViewShowing() = reactionView != null
}