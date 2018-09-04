package com.dcard.reactionsample

import android.content.Context
import android.graphics.Canvas
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class AvatarsView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var flag = 0

    var interval = 0
    val reaction1 = ContextCompat.getDrawable(context, R.drawable.reaction_smile)
    val reaction2 = ContextCompat.getDrawable(context, R.drawable.reaction_chu)
    val reaction3 = ContextCompat.getDrawable(context, R.drawable.reaction_crazy)
    val reaction4 = ContextCompat.getDrawable(context, R.drawable.reaction_noface)
    val reaction5 = ContextCompat.getDrawable(context, R.drawable.reaction_cry)

    init{

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        interval = w / 5
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        when(flag){
            0 -> {
                reaction1?.run {
                    setBounds(0,0,interval,height)
                    draw(canvas)
                }
            }
            1 -> {
                reaction2?.run {
                    setBounds(interval,0,2 * interval,height)
                    draw(canvas)
                }
            }
            2 -> {
                reaction3?.run {
                    setBounds(2 * interval,0,3 * interval,height)
                    draw(canvas)
                }
            }
            3 -> {
                reaction4?.run {
                    setBounds(3 * interval,0,4 * interval,height)
                    draw(canvas)
                }
            }
            4 -> {
                reaction5?.run {
                    setBounds(4 * interval,0,width,height)
                    draw(canvas)
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x.toInt()
        val y = event.y.toInt()

        when(event.action){
            MotionEvent.ACTION_MOVE -> {
                if(y in 0..height) {
                    when {
                        x <= interval -> {
                            if(flag != 0){
                                flag = 0
                                invalidate()
                            }

                        }
                        x <= interval * 2 -> {
                            if(flag != 1){
                                flag = 1
                                invalidate()
                            }
                        }
                        x <= interval * 3 -> {
                            if(flag != 2){
                                flag = 2
                                invalidate()
                            }
                        }
                        x <= interval * 4 -> {
                            if(flag != 3){
                                flag = 3
                                invalidate()
                            }
                        }
                        x <= width -> {
                            if(flag != 4){
                                flag = 4
                                invalidate()
                            }
                        }
                    }
                }
            }
        }

        return true
    }


}