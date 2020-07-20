package com.nike.trackit.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class TrackitAdapter (f:FragmentActivity, val fragmentlist:ArrayList<Fragment>): FragmentStateAdapter(f){
    override fun getItemCount()=fragmentlist.size

    override fun createFragment(position: Int):Fragment{
        return fragmentlist[position]
    }

}