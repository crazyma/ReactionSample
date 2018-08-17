package com.dcard.reactionsample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_row.view.*

class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

    companion object {
        fun create(inflater: LayoutInflater, parent: ViewGroup) =
                ItemViewHolder(inflater.inflate(
                        R.layout.item_row, parent, false))
    }

    fun bind(task: (View, ViewGroup) -> Unit){

        itemView.itemButton.setOnClickListener {

        }

        itemView.itemButton.setOnLongClickListener {
            task(it, it.parent as ViewGroup)
            false
        }
    }

}