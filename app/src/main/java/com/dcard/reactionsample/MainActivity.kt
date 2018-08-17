package com.dcard.reactionsample

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.view.ViewCompat
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnLongClickListener {
            Log.d("badu", "x :" + it.x + " y :" + it.y)
            addReactionView()

            it.post {
                rootLayout.interruptingTouchEvent = true
            }

            true
        }
    }

    private fun addReactionView(){
        val view = RootView(this).apply {
            this.setBackgroundColor(Color.GREEN)
            id = R.id.id_root_view
        }
        ViewCompat.setElevation(view, 32f)
        rootLayout.addView(view)

        val set = ConstraintSet()
        set.clone(rootLayout)
        set.connect(view.id, ConstraintSet.START, R.id.button, ConstraintSet.START, 0)
        set.connect(view.id, ConstraintSet.TOP, R.id.button, ConstraintSet.TOP, 0)
        set.constrainWidth(view.id, 600)
        set.constrainHeight(view.id, 600)

        set.applyTo(rootLayout)

        rootLayout.customTouchEventListener = view
    }
}
