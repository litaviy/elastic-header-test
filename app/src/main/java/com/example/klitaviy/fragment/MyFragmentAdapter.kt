package com.example.klitaviy.fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.klitaviy.collapseditemtest.R

/**
 * Created by klitaviy on 10/9/18-5:21 PM.
 */
class MyFragmentAdapter(private val data: MutableList<String>) : RecyclerView.Adapter<MyFragmentAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.content_item_layout, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data[position])
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.itemContent)
        fun bind(text: String) {
            title.text = text
        }
    }
}