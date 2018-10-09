package com.example.klitaviy

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by klitaviy on 10/9/18-3:46 PM.
 */
class MyPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    fun <T : Fragment> addFragment(fragment: T) {
        fragments.add(fragment)
        notifyDataSetChanged()
    }
}