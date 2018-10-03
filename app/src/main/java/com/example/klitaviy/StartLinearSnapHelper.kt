package com.example.klitaviy


import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by klitaviy on 10/3/18-5:51 PM.
 * Source - https://gist.github.com/justinthomas-syncbak/f2806deb56fb22dc72ef5af4ce7347e5
 */
open class StartLinearSnapHelper : LinearSnapHelper() {

    protected var _verticalHelper: OrientationHelper? = null
    protected var _horizontalHelper: OrientationHelper? = null

    //By default, this looks for the child's center to be snapped to, so we have to override
    //to change to the start of the view.  If we didn't do this, it would scroll over and over
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        if (layoutManager.canScrollVertically()) {
            return findFirstView(layoutManager, getVerticalHelper(layoutManager))
        } else if (layoutManager.canScrollHorizontally()) {
            return findFirstView(layoutManager, getHorizontalHelper(layoutManager))
        }
        return null
    }

    //Override to use the start of the child view instead of the center
    override fun calculateDistanceToFinalSnap(layoutManager: RecyclerView.LayoutManager, targetView: View): IntArray? {
        val out = IntArray(2, { 0 })
        if (layoutManager.canScrollHorizontally()) {
            out[0] = distanceToStart(targetView, getHorizontalHelper(layoutManager))
        } else {
            out[0] = 0
        }

        if (layoutManager.canScrollVertically()) {
            out[1] = distanceToStart(targetView, getVerticalHelper(layoutManager))
        } else {
            out[1] = 0
        }
        return out
    }

    protected fun findFirstView(layoutManager: RecyclerView.LayoutManager, helper: OrientationHelper): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        //If the last item is completely visible, don't snap to any view
        if (layoutManager is LinearLayoutManager) {
            val lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition()
            if (lastVisibleItemPosition == layoutManager.itemCount - 1) return null
        }

        var closestChild: View? = null
        var start: Int = 0

        if (layoutManager.clipToPadding) {
            start = helper.startAfterPadding
        }

        var absClosest = Integer.MAX_VALUE

        for (i in 0..childCount - 1) {
            val child = layoutManager.getChildAt(i)
            val childStart = helper.getDecoratedStart(child)
            val absDistance = Math.abs(childStart - start)

            /** if child start is closer than previous closest, set it as closest   */
            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }

    protected fun distanceToStart(targetView: View, helper: OrientationHelper): Int {
        return helper.getDecoratedStart(targetView) - helper.startAfterPadding
    }

    //We have to re-define these because they are private to the [LinearSnapHelper]
    protected fun getVerticalHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (_verticalHelper == null) {
            _verticalHelper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        return _verticalHelper!!
    }

    protected fun getHorizontalHelper(
            layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (_horizontalHelper == null) {
            _horizontalHelper = OrientationHelper.createHorizontalHelper(layoutManager)
        }
        return _horizontalHelper!!
    }
}