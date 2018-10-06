package com.dcard.reactionsample.result

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.GridLayoutManager
import android.util.AttributeSet
import android.util.Log
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.layout_reaction_result.view.*

class ReactionResultBaseLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        inflate(getContext(), R.layout.layout_reaction_result_base, this)
        setupRecyclerView(context)

        Log.d("badu","recycler width : ${resultRecyclerView.width}")
    }

    private fun setupRecyclerView(context: Context) {
        postDelayed( {Log.d("badu","recycler width : ${resultRecyclerView.width}")

            resultRecyclerView.layoutManager = GridLayoutManager(context,2)
            resultRecyclerView.adapter = ReactionResultAdapter(context)
            Log.d("badu","recycler width : ${resultRecyclerView.width}")
        }, 2000)
    }
}