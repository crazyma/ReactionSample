package com.dcard.reactionsample.result

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
//        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = GridLayoutManager(this,2)
        recyclerView.adapter = ReactionResultAdapter(this)
    }


}