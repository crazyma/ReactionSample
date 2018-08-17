package com.dcard.reactionsample

import android.annotation.SuppressLint
import android.content.Context
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.MotionEvent

class CustomConstraintLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr){


    interface CustomTouchEventListener{
        fun onHandleTouchEvent(event: MotionEvent)
    }

    var interruptingTouchEvent = false
    var customTouchEventListener : CustomTouchEventListener? = null

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {

        if(interruptingTouchEvent){
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

}