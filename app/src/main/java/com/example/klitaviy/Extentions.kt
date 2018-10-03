package com.example.klitaviy

import android.view.View
import android.view.ViewTreeObserver

/**
 * Created by klitaviy on 10/3/18-4:19 PM.
 */
fun View.changeHeight(height: Int) {
    layoutParams.height = height
    requestLayout()
}

inline fun <T : View> T.afterMeasure(crossinline f: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                f()
            }
        }
    })
}