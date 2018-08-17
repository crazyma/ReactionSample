package com.dcard.reactionsample

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RecyclerViewAdapter(

        private val context: Context

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var clickTask: ((View, ViewGroup) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return ItemViewHolder.create(layoutInflater, parent)
    }

    override fun getItemCount() = 20

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ItemViewHolder){
            clickTask?.run {
                holder.bind(this)
            }
        }
    }

}