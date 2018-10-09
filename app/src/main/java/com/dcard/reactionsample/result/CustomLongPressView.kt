package com.dcard.reactionsample.result

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class CustomLongPressView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var isPressing = false
    var isLongClick = false

    var customLongClickListener: (() -> Unit)? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("badu", "ACTION_DOWN")
                recordPress()
                pressCounting()

            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                Log.d("badu", "ACTION_UP")
                cancelPress()
                if (!isLongClick) {
                    performClick()
                }
            }

        }

        return true
    }

    @Synchronized
    private fun recordPress() {
        isPressing = true
    }

    @Synchronized
    private fun cancelPress() {
        isPressing = false
    }

    @Synchronized
    private fun checkLongClick() {
        isLongClick = isPressing
        if (isPressing) {
            Log.d("badu", "long click")
            customLongClickListener?.invoke()
        } else {
            Log.d("badu", "not long click")
        }
    }

    private fun pressCounting() {
        Thread(Runnable {
            Thread.sleep(200)
            checkLongClick()
        }).start()
    }

}