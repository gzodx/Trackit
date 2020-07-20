package com.nike.trackit.ui.dashboard

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.nike.trackit.R
import com.nike.trackit.core.AdminReceiver
import com.nike.trackit.databinding.FragmentMainBinding


class Main : Fragment() {

    lateinit var binding:FragmentMainBinding
    lateinit var componentName: ComponentName
    lateinit var devicePolicyManager: DevicePolicyManager
    private val ADMIN_INTENT = 15


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_main, container, false)
        binding.io.background=resources.getDrawable(R.drawable.line_green)
        binding.io.setOnClickListener {
        if (devicePolicyManager.isAdminActive(componentName)){
          devicePolicyManager.removeActiveAdmin(componentName)
            binding.io.apply {
                isCheckable=false
                strokeColor=resources.getColor(R.color.colorPrimaryDark)
                background=resources.getDrawable(R.drawable.line)
            }
        }else{
            val intent=Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, R.string.device_admin_explanation)
            startActivityForResult(intent,ADMIN_INTENT)

        }
        }
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        componentName= ComponentName(context!!,AdminReceiver::class.java)
        devicePolicyManager =activity?.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        super.onCreate(savedInstanceState)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==ADMIN_INTENT){
            binding.io.background=resources.getDrawable(R.drawable.line_green)
        }else{

        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object{
        fun newInstance()=Main()
    }
}