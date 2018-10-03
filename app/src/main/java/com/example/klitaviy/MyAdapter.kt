package com.example.klitaviy

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by klitaviy on 10/3/18-3:48 PM.
 */
class MyAdapter(private val layoutId: Int, private val count: Int) : RecyclerView.Adapter<MyAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false))

    override fun getItemCount(): Int = count

    override fun onBindViewHolder(holder: Holder, position: Int) {
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view)
}