package com.dcard.reactionsample

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnLongClickListener {
            Log.d("badu", "x :" + it.x + " y :" + it.y)
            addReactionView()
            true
        }
    }

    private fun addReactionView(){
        val view = View(this).apply {
            this.setBackgroundColor(Color.GREEN)
        }

        val params = ConstraintLayout.LayoutParams(200,200)

        rootLayout.addView(view, params)
    }
}
