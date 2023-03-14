package com.example.app.viewpager2

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
    private val NUM_PAGES = 6
    override fun getItemCount(): Int = NUM_PAGES

    // 프래그먼트가 생성되는 형식으로 사용하는 것은 아래와 같이 구성해도 무관하다.
    // 아래 코드는 MyFragment를 3번 돌려쓰는 형식
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                MyFragment.newInstance("Pager1", "")
            }
            1 -> {
                MyFragment.newInstance("Pager2", "")
            }
            2 -> {
                MyFragment.newInstance("Pager3", "")
            }
            3 -> {
                MyFragment.newInstance("Pager4", "")
            }
            4 -> {
                MyFragment.newInstance("Pager5", "")
            }
            else -> {
                MyFragment.newInstance("Pager6", "")
            }
        }
    }

}