package com.dcard.reactionsample

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TrickerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var currentStatue = 0


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    val longPressCheck = Runnable {

    }

}