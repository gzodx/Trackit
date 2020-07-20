package com.nike.trackit.ui.onboarding

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.nike.trackit.R
import com.nike.trackit.model.OnBoarding
import com.nike.trackit.ui.Welcome
import com.nike.trackit.ui.adapter.OnBoardingAdapter
import com.nike.trackit.utils.get
import com.nike.trackit.utils.navigateTo
import com.nike.trackit.utils.putData
import kotlinx.android.synthetic.main.activity_on_boarding.*

class  OnBoardingActivity : AppCompatActivity() {
    private lateinit var onBoardingAdapter: OnBoardingAdapter
    private lateinit var  btnAnimation: Animation
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestFullScreen()
        setContentView(R.layout.activity_on_boarding)
        setupViewpager()
    }

   private fun setupViewpager() {
        sharedPreferences=applicationContext.getSharedPreferences("mypref", Context.MODE_PRIVATE)
        btnAnimation = AnimationUtils.loadAnimation(this, R.anim.entrance)

        val mlist = ArrayList<OnBoarding>().apply {
            add(OnBoarding("Security in Your finger tips","We got you covered", "We Keep Track of your phone where ever it is ", R.drawable.ic_group_36,R.color.colorPrimaryDark))
            add(OnBoarding("Safe All Times ","care less about security", "Trackit always keep you on alert if your device get to a wrong hands", R.drawable.ic_group_36,R.color.colorAccent))
            add(OnBoarding("We Keep an eye","You are safe at all times", "smile", R.drawable.ic_group_36,R.color.colorPrimaryDark))
        }

        onBoardingAdapter = OnBoardingAdapter(mlist)
        pager.adapter = onBoardingAdapter

        tab_indicator.apply {
            setupWithViewPager(pager)

            addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
                override fun onTabReselected(tab: TabLayout.Tab?) {

                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {

                }

                override fun onTabSelected(tab: TabLayout.Tab?) {
                    if (tab?.position==mlist.size.minus(1)){
                        loadlastScreen()
                        main.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                    }
                }
            })
        }

        fab.setOnClickListener {
            var position=pager.currentItem
            if (position<mlist.size){
                fab.show()
                position++
                pager.currentItem=position

            }
            if(position==mlist.size.minus(1)){
                loadlastScreen()
            }
        }

        getStarted.setOnClickListener { navigateTo<OnboardingMain> {  };sharedPreferences.putData("onBoarding",true)}
    }

    private fun loadlastScreen() {
        fab.hide()
        tab_indicator.visibility= View.GONE
        getStarted.apply {
            visibility= VISIBLE
            startAnimation(btnAnimation)
        }
    }

   private fun requestFullScreen(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }

    private fun hasSeenOnBoarding():Boolean{
     return sharedPreferences.get("onBoarding",false)
    }

    override fun onStart() {
        super.onStart()
        if (hasSeenOnBoarding()){
            navigateTo<OnboardingMain> {  }
            return
        }
    }

}
