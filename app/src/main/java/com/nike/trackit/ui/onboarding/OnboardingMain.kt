package com.nike.trackit.ui.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.nike.trackit.R
import com.nike.trackit.ui.dashboard.MainActivity
import com.nike.trackit.utils.get
import com.nike.trackit.utils.navigateTo
import com.nike.trackit.utils.savepref

class OnboardingMain : AppCompatActivity() {

    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_main)
        navController=findNavController(R.id.nav_host_fragment)

        if (savepref().get("onBoard","null") !=="null"){
            navigateTo<MainActivity> {  }
        }
    }



    override fun onBackPressed() = when {
        navController.graph.startDestination == navController.currentDestination?.id ->  finishAffinity()
        else ->goUp()
    }

    private fun goUp(){
        ActivityNavigator.applyPopAnimationsToPendingTransition(this)
        navController.navigateUp()
    }
}
