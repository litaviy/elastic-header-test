package com.example.klitaviy

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.klitaviy.collapseditemtest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "scroll_deb"

    // Min. offset from the TOP to show header in any case.
    private val MIN_OFFSET_TO_TOP = 10
    // Offset that user have to scroll DOWN to start  header expansion.
    private val MIN_OFFSET_TO_SHOW_HEADER = 250
    // Header collapsing / expansion.
    private val ANIMATION_DURATION = 150L
    // Makes change smoothly.
    private val HEADER_CHANGES_DIVIDER = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        headerRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        headerRecyclerView.adapter = MyAdapter(R.layout.header_item_layout, 10)
        val snapHelper = StartLinearSnapHelper()
        snapHelper.attachToRecyclerView(headerRecyclerView)

        val collapsedHeaderHeight = resources.getDimensionPixelSize(R.dimen.header_min_h)
        val halfExpandedHeaderHeight = collapsedHeaderHeight + (collapsedHeaderHeight / 2)
        val expandedHeaderHeight = collapsedHeaderHeight * 2
        var currentHeight = expandedHeaderHeight

        headerRecyclerView.afterMeasure {
            this.changeHeight(expandedHeaderHeight)
        }

        contentRecyclerView.layoutManager = LinearLayoutManager(this)
        contentRecyclerView.adapter = MyAdapter(R.layout.content_item_layout, 20)

        var lastVerticalScrollOffset = 0
        contentRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    // UP
                    if (currentHeight > collapsedHeaderHeight) {
                        currentHeight -= (dy / HEADER_CHANGES_DIVIDER)
                        currentHeight = if (currentHeight > collapsedHeaderHeight) {
                            currentHeight
                        } else {
                            collapsedHeaderHeight
                        }
                        headerRecyclerView.changeHeight(currentHeight)
                    }
                } else {
                    // DOWN
                    if (recyclerView.scrollState != RecyclerView.SCROLL_STATE_SETTLING &&
                            lastVerticalScrollOffset - recyclerView.computeVerticalScrollOffset() > MIN_OFFSET_TO_SHOW_HEADER) {
                        // This is users scroll (by finger) AND scrolling offset is more than defined as MIN_OFFSET_TO_SHOW_HEADER
                        // so we start determining should we change header height or not.
                        if (currentHeight < expandedHeaderHeight) {
                            currentHeight += (Math.abs(dy) / HEADER_CHANGES_DIVIDER)
                            currentHeight = if (currentHeight > expandedHeaderHeight) {
                                expandedHeaderHeight
                            } else {
                                currentHeight
                            }
                            headerRecyclerView.changeHeight(currentHeight)
                        }
                    }
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                // Scroll stopped
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val scrollOffset = recyclerView.computeVerticalScrollOffset()
                    lastVerticalScrollOffset = scrollOffset
                    val animation = if ((currentHeight > halfExpandedHeaderHeight) || (scrollOffset < MIN_OFFSET_TO_TOP)) {
                        // User scrolled more that halfExpandedHeaderHeight OR list position is
                        // about to reach its top - so we show header.
                        ValueAnimator.ofInt(currentHeight, expandedHeaderHeight)
                    } else {
                        ValueAnimator.ofInt(currentHeight, collapsedHeaderHeight)
                    }
                    animation?.duration = ANIMATION_DURATION
                    animation?.addUpdateListener {
                        val value = it.animatedValue as Int
                        headerRecyclerView.changeHeight(value)
                        currentHeight = value
                    }
                    animation?.start()
                }
            }
        })
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }
}
