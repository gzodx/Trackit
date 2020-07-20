package com.nike.trackit.ui.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nike.trackit.R
import com.nike.trackit.utils.navigateTo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        CoroutineScope(Main).launch {
            delay(3000)
            navigateTo<OnBoardingActivity> {  }
        }
    }
}