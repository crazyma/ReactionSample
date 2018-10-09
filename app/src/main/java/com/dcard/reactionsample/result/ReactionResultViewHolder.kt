package com.dcard.reactionsample.result

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dcard.reactionsample.R
import kotlinx.android.synthetic.main.list_item_reaction.view.*

class ReactionResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup) =
                ReactionResultViewHolder(inflater.inflate(R.layout.list_item_reaction, parent, false))
    }

    fun bind(string: String){
        itemView.reactionName.text = "string"
        itemView.reactionCount.text = (Math.random() * 1000).toInt().toString()
    }

}