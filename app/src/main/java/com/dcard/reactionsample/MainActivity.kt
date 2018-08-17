package com.dcard.reactionsample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
    }

    private fun addReactionView() {

    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RecyclerViewAdapter(this).apply {
            clickTask = { view, viewGroup ->
                view.getLocationOnScreen()
                Log.d("badu", "view view " + view.x.toInt() + " " + (view.y + viewGroup.y).toInt() + " " + view.width + " " + view.height)
                rootLayout.showReactionView(view.x.toInt(), (view.y + viewGroup.y).toInt(), view.width, view.height)
            }
        }
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
