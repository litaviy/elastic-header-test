package com.example.klitaviy

import android.animation.ValueAnimator
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.klitaviy.collapseditemtest.R
import com.example.klitaviy.fragment.ElasticHeaderFragment
import com.example.klitaviy.fragment.MyFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "scroll_deb"

    // Min. offset from the TOP to show header in any case.
    private val MIN_OFFSET_TO_TOP = 10
    // Offset that user have to scroll DOWN to start  header expansion.
    private val MIN_OFFSET_TO_SHOW_HEADER = 150
    // Header collapsing / expansion.
    private val ANIMATION_DURATION = 150L
    // Makes change smoothly.
    private val HEADER_CHANGES_DIVIDER = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val headerLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        headerRecyclerView.layoutManager = headerLayoutManager
        headerRecyclerView.adapter = MyAdapter(R.layout.header_item_layout, "Header", 20)
//        val snapHelper = StartLinearSnapHelper()
//        snapHelper.attachToRecyclerView(headerRecyclerView)

        val collapsedHeaderHeight = resources.getDimensionPixelSize(R.dimen.header_min_h)
        val expandedHeaderHeight = collapsedHeaderHeight * 2
        val halfExpandedHeaderHeight = collapsedHeaderHeight + (collapsedHeaderHeight / 2)
        var currentHeight = expandedHeaderHeight
        val headerItemWidth = resources.getDimensionPixelSize(R.dimen.header_min_w)

        headerRecyclerView.afterMeasure {
            this.changeHeight(expandedHeaderHeight)
        }

        val fragmentsListener: ElasticHeaderFragment.Listener = object : ElasticHeaderFragment.Listener {

            override fun onListUp(dy: Int) {
                if (currentHeight > collapsedHeaderHeight) {
                    currentHeight -= (dy / HEADER_CHANGES_DIVIDER)
                    currentHeight = if (currentHeight > collapsedHeaderHeight) {
                        currentHeight
                    } else {
                        collapsedHeaderHeight
                    }
                    headerRecyclerView.changeHeight(currentHeight)
                }
            }

            override fun onListDown(dy: Int, scrollOffset: Int) {
                if (scrollOffset > MIN_OFFSET_TO_SHOW_HEADER) {
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

            override fun onScrollStateIdle(scrollOffset: Int) {
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

        val pagerAdapter = MyPagerAdapter(supportFragmentManager)

        for (i in 1..10) {
            val fragment = MyFragment.create(i)
            fragment.fragmentListener = fragmentsListener
            pagerAdapter.addFragment(fragment)
        }

        contentViewPager.offscreenPageLimit = 10
        contentViewPager.adapter = pagerAdapter
        contentViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val headerOffset = (headerItemWidth / contentViewPager.width) * positionOffsetPixels
//                log("headerOffset : $headerOffset")
//                headerLayoutManager.offsetChildrenHorizontal(headerOffset)
//                headerLayoutManager.scrollHorizontallyBy(headerItemWidth, headerRecyclerView.Recycler(), headerRecyclerView.scrollState)
            }

            override fun onPageSelected(position: Int) {
            }
        })
    }

    private fun log(message: String) {
        Log.d(TAG, message)
    }
}
