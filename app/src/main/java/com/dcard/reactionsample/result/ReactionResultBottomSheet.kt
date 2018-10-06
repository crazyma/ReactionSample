package com.dcard.reactionsample.result

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.layout_reaction_result.*

class ReactionResultBottomSheet: BottomSheetDialogFragment() {

    lateinit var adapter : ReactionResultAdapter

    companion object {
        fun newInstance(): ReactionResultBottomSheet =
                ReactionResultBottomSheet()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_reaction_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ReactionResultAdapter(context!!)

        resultRecyclerView.adapter = adapter
        resultRecyclerView.layoutManager = GridLayoutManager(context!!, 2)
    }
}