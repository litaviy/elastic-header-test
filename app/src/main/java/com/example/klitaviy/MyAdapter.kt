package com.example.klitaviy

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.klitaviy.collapseditemtest.R

/**
 * Created by klitaviy on 10/3/18-3:48 PM.
 */
class MyAdapter(private val layoutId: Int, private val text: String, private val count: Int) : RecyclerView.Adapter<MyAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    override fun getItemCount(): Int = count

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind("$text : $position")
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.itemContent)
        fun bind(text: String) {
            title.text = text
        }
    }
}