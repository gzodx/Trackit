package com.nike.trackit.ui.onboarding


import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context.DEVICE_POLICY_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nike.trackit.R
import com.nike.trackit.core.AdminReceiver
import com.nike.trackit.databinding.FragmentActivateAdminBinding



class ActivateAdmin : Fragment() {

    lateinit var binding:FragmentActivateAdminBinding
    lateinit var navController: NavController
    private lateinit var dpm: DevicePolicyManager
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_activate__admin, container, false)
        navController=findNavController()
        navigation()
        return  binding.root
    }

    fun navigation(){
        binding.next.setOnClickListener {
            navController.navigate(R.id.action_activateAdmin_to_attempts_limit)
        }

        binding.back.setOnClickListener {
            navController.navigateUp()
        }

        binding.activate.setOnClickListener{getAdminRights()}

    }

    fun getAdminRights(){
        val componentName=ComponentName(context!!,AdminReceiver::class.java)
        dpm = activity?.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (dpm.isAdminActive(componentName)){
        }else {
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            intent.putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.device_admin_explanation)
            )
            startActivity(intent)
        }
    }


    fun checkAdminRights() {
        val componentName = ComponentName(context!!, AdminReceiver::class.java)
        dpm = activity?.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (dpm.isAdminActive(componentName)) {
            activatedAdmin()
        }


    }

    override fun onResume() {
        checkAdminRights()
        super.onResume()
    }

    fun activatedAdmin(){
        binding.activate.visibility= GONE
        binding.activated.visibility=VISIBLE
        binding.next.visibility= VISIBLE
    }
}
