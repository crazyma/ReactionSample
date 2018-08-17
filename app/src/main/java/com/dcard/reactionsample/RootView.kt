package com.dcard.reactionsample

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

class RootView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), CustomConstraintLayout.CustomTouchEventListener {

    var reactionCount = 5
    var bottomPosition = 0
    var rightPosition = 0
    var dividerPositionArray: IntArray? = null

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        rightPosition = w
        bottomPosition = h

        dividerPositionArray = IntArray(reactionCount).apply {
            val interval = w / reactionCount.toFloat()
            for (i in 0 until reactionCount) {
                this[i] = (interval * (i + 1)).toInt()
            }
        }

    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
//        Log.d("badu","the parent of RootView : " + parent)
//        if(parent is CustomConstraintLayout){
//            postDelayed({
//                parent.requestDisallowInterceptTouchEvent(true)
//            },500)
//        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x.toInt()
        val y = event.y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                Log.d("badu", "[DOWN] x: $x, y: $y | area : " + recognizePosition(x,y))
            }
            MotionEvent.ACTION_MOVE -> {
                if(parent is CustomConstraintLayout){
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                Log.d("badu", "[MOVE] x: $x, y: $y | area : " + recognizePosition(x,y))
            }
            MotionEvent.ACTION_UP -> {
                Log.d("badu", "[UP] x: $x, y: $y | area : " + recognizePosition(x,y))
            }
            else -> return super.onTouchEvent(event)
        }

        return true
    }

    override fun onHandleTouchEvent(event: MotionEvent) {
        val x = event.x.toInt() - x.toInt()
        val y = event.y.toInt() - y.toInt()

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                Log.d("badu", "[onHandleTouchEvent DOWN] x: $x, y: $y | area : " + recognizePosition(x,y))
            }
            MotionEvent.ACTION_MOVE -> {
                if(parent is CustomConstraintLayout){
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                Log.d("badu", "[onHandleTouchEvent MOVE] x: $x, y: $y | area : " + recognizePosition(x,y))
            }
            MotionEvent.ACTION_UP -> {
                Log.d("badu", "[onHandleTouchEvent UP] x: $x, y: $y | area : " + recognizePosition(x,y))
            }
        }
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    private fun recognizePosition(x: Int, y: Int): Int {

        if (y in 0..bottomPosition && x in 0 .. rightPosition) {
            dividerPositionArray?.run {
                val size = this.size
                for (i in 0 until size) {
                    if (x < this[i]) {
                        return i
                    }
                }
            }
        }

        return -1
    }

}