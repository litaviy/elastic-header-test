package com.example.klitaviy

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.klitaviy.collapseditemtest.R

/**
 * Created by klitaviy on 10/3/18-3:48 PM.
 */
class MyAdapter(private val data: MutableList<String>,
                private val clickListener: (Int) -> Unit) : RecyclerView.Adapter<MyAdapter.Holder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): Holder =
            Holder(LayoutInflater.from(parent.context).inflate(R.layout.header_item_layout, parent, false))

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(position, data[position], clickListener)
    }

    fun selectItem(position: Int) {
        if (selectedPosition != position) {
            val temp = selectedPosition
            selectedPosition = position
            notifyItemChanged(temp)
            notifyItemChanged(selectedPosition)
        }
    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = view.findViewById(R.id.itemContent)
        private val container: View = view.findViewById(R.id.itemContainer)

        fun bind(position: Int, text: String, clickListener: (Int) -> Unit) {
            title.text = text
            container.setOnClickListener {
                val adapterPosition = adapterPosition
                if (selectedPosition != adapterPosition) {
                    val temp = selectedPosition
                    selectedPosition = adapterPosition
                    notifyItemChanged(temp)
                    notifyItemChanged(selectedPosition)
                }
                clickListener(adapterPosition)
            }
            if (selectedPosition == position) {
                selectItem()
            } else {
                unSelectItem()
            }
        }

        private fun selectItem() {
            container.setBackgroundColor(ContextCompat.getColor(container.context, R.color.contentColor))
        }

        private fun unSelectItem() {
            container.setBackgroundColor(ContextCompat.getColor(container.context, R.color.colorAccent))
        }
    }
}