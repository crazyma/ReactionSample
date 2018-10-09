package com.dcard.reactionsample.result

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class ReactionResultAdapter(context: Context): RecyclerView.Adapter<ReactionResultViewHolder>() {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReactionResultViewHolder {
        return ReactionResultViewHolder.create(inflater, parent)
    }

    override fun getItemCount(): Int {
        return 40
    }

    override fun onBindViewHolder(holder: ReactionResultViewHolder, position: Int) {
        holder.bind("name $position")
    }
}