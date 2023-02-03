package com.example.farmus_application.ui.farm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

private const val NUM = 2

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return NUM
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> return Tab1Fragment()
            1 -> return Tab2Fragment()
        }
        return Tab1Fragment()
    }
}