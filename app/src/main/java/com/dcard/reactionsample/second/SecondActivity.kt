package com.dcard.reactionsample.second

import android.animation.ValueAnimator
import android.graphics.RectF
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.LinearInterpolator
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }

    fun buttonClicked(v: View){
        when(v.id){
            R.id.biggerButton -> {
                customView.bigger()
            }
            R.id.smallButton -> {
                customView.smaller()
            }

            R.id.stopButton -> {
                customView.cancel()
            }
        }
    }

}