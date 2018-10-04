package com.dcard.reactionsample.spark

import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.activity_spark.*

class SparkActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spark)

        val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.reaction_chu)
        sparkView.iconBitmap = bitmap2
    }

    fun buttonClicked(v: View){
        sparkView.runSpark()
    }

}