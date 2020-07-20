package com.nike.trackit.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.nike.trackit.R
import com.nike.trackit.databinding.OnboardingItemBinding
import com.nike.trackit.model.OnBoarding

class OnBoardingAdapter constructor(private val list: List<OnBoarding> ): PagerAdapter() {
    private lateinit var binding: OnboardingItemBinding

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view==`object`
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater=container.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding=DataBindingUtil.inflate(inflater, R.layout.onboarding_item,container,false)
        binding.headerText.text=list[position].headerText
        binding.textdesc.text=list[position].titleText
        binding.subHeaderText.text=list[position].subheader
        binding.imageView.setImageResource(list[position].images)
//        binding.root.background=list[position].color
        container.addView(binding.root)
        return binding.root
    }

}