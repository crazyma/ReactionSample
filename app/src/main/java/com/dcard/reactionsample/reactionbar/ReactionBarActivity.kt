package com.dcard.reactionsample.reactionbar

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.activity_reaction_bar.*

class ReactionBarActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reaction_bar)

        Handler().post {
            val bitmap1 = BitmapFactory.decodeResource(resources, R.drawable.reaction_smile)
            val bitmap2 = BitmapFactory.decodeResource(resources, R.drawable.reaction_chu)
            val bitmap5 = BitmapFactory.decodeResource(resources, R.drawable.reaction_cry)
            reactionBarView.addBitmap(listOf(bitmap1, bitmap2, bitmap5))
        }

    }

}