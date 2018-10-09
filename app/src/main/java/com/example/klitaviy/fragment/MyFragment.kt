package com.example.klitaviy.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.klitaviy.collapseditemtest.R
import kotlinx.android.synthetic.main.my_fragment.*

/**
 * Created by klitaviy on 10/9/18-12:58 PM.
 */
class MyFragment : ElasticHeaderFragment<MutableList<String>>() {

    companion object {
        fun create(position: Int): MyFragment {
            val data: MutableList<String> = MutableList(20) {
                "Content : $position : $it"
            }
            val args = packBundle(data)
            val fragment = MyFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun getRecyclerView(): RecyclerView = fragmentContentRecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.my_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data: DataHolder<MutableList<String>>? = getDataHolder()
        data?.let {
            fragmentContentRecyclerView.layoutManager = LinearLayoutManager(context)
            fragmentContentRecyclerView.adapter = MyFragmentAdapter(it.data)
        }
    }
}