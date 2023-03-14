package com.example.combine

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.example.combine.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val binding : ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    // 텝 레이아웃에 아이콘을 표시하기 위한 아이콘 리스트
    private val tabIcon = listOf(
        R.drawable.baseline_format_list_bulleted_24,
        R.drawable.baseline_map_24,
        R.drawable.baseline_info_24,
        R.drawable.baseline_format_list_bulleted_24,
        R.drawable.baseline_map_24,
        R.drawable.baseline_info_24,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.viewpager.apply{
            adapter = MyPagerAdapter(context as FragmentActivity)
            /**
             * [setPageTansformer] 프로퍼티를 사용하여 페이지 애니메이션 적용 가능.
             */
            setPageTransformer(ZoomOutPageTransformer())
        }

        /**
         * [TabLayoutMediator]는 탭 레이아웃과 뷰페이저2를 연결하는 데에 사용된다.
         */
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position->
            tab.text = "Title $position"
            tab.setIcon(tabIcon[position])
        }.attach()
    }
}