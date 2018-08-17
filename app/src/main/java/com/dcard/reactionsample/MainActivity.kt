package com.dcard.reactionsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun addReactionView() {

    }

    fun buttonClicked(v: View) {
        val it = v
        Log.d("badu", "x :" + it.x + " y :" + it.y)
        if (rootLayout.isReactionViewShowing()) {
            rootLayout.hideReactionView()
        } else {
            rootLayout.showReactionView(it.x.toInt(), it.y.toInt(), it.width, it.height)
        }

        it.post {
            rootLayout.interruptingTouchEvent = true
        }
    }
}
