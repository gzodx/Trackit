package com.nike.trackit.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController

import com.nike.trackit.R
import com.nike.trackit.databinding.FragmentWelcomeBinding
import com.nike.trackit.ui.dashboard.MainActivity
import com.nike.trackit.ui.onboarding.OnBoardingActivity
import com.nike.trackit.utils.get
import com.nike.trackit.utils.navigateTo
import com.nike.trackit.utils.savepref


class Welcome : Fragment() {

    lateinit var binding:FragmentWelcomeBinding
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_welcome, container, false)
        navController=findNavController()
        checkOnBoarding()
        navigation()
        return binding.root
    }

    private fun navigation() {
        binding.btnWelcome.setOnClickListener {
        navController.navigate(R.id.action_welcome_to_activateAdmin)
        }
    }

    private fun checkOnBoarding(){

    }


}
