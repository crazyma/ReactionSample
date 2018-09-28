package com.dcard.reactionsample.third

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.activity_third.*

class ThirdActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
    }

    fun buttonClicked(v: View) {
        animBaseLayout.runAnim()
    }

}