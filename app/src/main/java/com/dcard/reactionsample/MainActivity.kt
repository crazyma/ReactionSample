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

                val intArray = IntArray(2)
                view.getLocationOnScreen(intArray)

                val rootIntArray = IntArray(2)
                rootLayout.getLocationOnScreen(rootIntArray)

                val offsetTopByRootView = intArray[1] - rootIntArray[1]


                Log.i("badu", "view location on screen : " + intArray[0] + ", " + offsetTopByRootView)

                Log.d("badu", "view view " + view.x.toInt() + " " + (view.y + viewGroup.y).toInt() + " " + view.width + " " + view.height)

                view.post {
                    rootLayout.showReactionView(view.x.toInt(), (view.y + viewGroup.y).toInt(), view.width, view.height)
                    rootLayout.interruptingTouchEvent = true
                }

            }
        }
    }

    fun buttonClicked(v: View) {
        val it = v
        Log.d("badu", "x :" + it.x + " y :" + it.y)

        val intArray = IntArray(2)
        v.getLocationOnScreen(intArray)

        val rootIntArray = IntArray(2)
        rootLayout.getLocationOnScreen(rootIntArray)

        val offsetTopByRootView = intArray[1] - rootIntArray[1]

        Log.d("badu", " intArrayx :" + intArray[0] + " y :" + intArray[1])

        if (rootLayout.isReactionViewShowing()) {
            rootLayout.hideReactionView()
        } else {
            rootLayout.showReactionView(intArray[0], offsetTopByRootView, it.width, it.height)
        }

        it.post {
            rootLayout.interruptingTouchEvent = true
        }
    }
}
