package com.example.klitaviy.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.View
import java.io.Serializable

/**
 * Created by klitaviy on 10/9/18-4:02 PM.
 */
abstract class ElasticHeaderFragment<T> : Fragment() {

    companion object {

        private val key_data = "key_data"

        fun <T> packBundle(data: T): Bundle {
            val args = Bundle()
            val dataHolder: DataHolder<T> = DataHolder(data)
            args.putSerializable(key_data, dataHolder)
            return args
        }
    }

    interface Listener {
        fun onListUp(dy: Int)
        fun onListDown(dy: Int, scrollOffset: Int)
        fun onScrollStateIdle(scrollOffset: Int)
    }

    private val TAG = "el_fr_deb"
    var fragmentListener: Listener? = null

    abstract fun getRecyclerView(): RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = getRecyclerView()
        var lastVerticalScrollOffset = 0
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    // UP
                    fragmentListener?.onListUp(dy)
                } else {
                    // DOWN
                    fragmentListener?.onListDown(dy, lastVerticalScrollOffset - recyclerView.computeVerticalScrollOffset())
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Scroll stopped
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVerticalScrollOffset = recyclerView.computeVerticalScrollOffset()
                    fragmentListener?.onScrollStateIdle(lastVerticalScrollOffset)
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentListener = null
    }

    protected fun getDataHolder(): DataHolder<T>? = try {
        arguments?.getSerializable(key_data) as? DataHolder<T>
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    data class DataHolder<T>(val data: T) : Serializable
}