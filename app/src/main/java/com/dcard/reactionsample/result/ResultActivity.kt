package com.dcard.reactionsample.result

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
//        setupRecyclerView()

        customLongPressView.customLongClickListener = {
            Log.i("badu","Long Click")
        }

        customLongPressView.setOnClickListener {
            Log.i("badu", "Click")
        }
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = ReactionResultAdapter(this)
    }

    fun buttonClicked(v: View){
        ReactionResultBottomSheet.newInstance().show(supportFragmentManager, "ReactionResult")
    }

}