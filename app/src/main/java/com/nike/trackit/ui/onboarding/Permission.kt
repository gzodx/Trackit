package com.nike.trackit.ui.onboarding


import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.androidhiddencamera.HiddenCameraUtils
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.nike.trackit.R
import com.nike.trackit.databinding.FragmentPermissionBinding
import com.nike.trackit.ui.dashboard.MainActivity
import com.nike.trackit.utils.*


class Permission : Fragment() {
lateinit var binding:FragmentPermissionBinding
    var numOfPermissionAllowed=0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater, R.layout.fragment_permission, container, false)
        setUpNavigation()
        return binding.root
    }

    private fun setUpNavigation() {
        binding.next.setOnClickListener {
            context?.savepref()?.putData("onBoard","done")
            activity?.navigateTo<MainActivity>()
        }
        binding.cameraBtn.setOnCheckedChangeListener { buttonView, isChecked ->
         if (isChecked){
             Dexter.withActivity(activity)
                 .withPermission(Manifest.permission.CAMERA)
                 .withListener(object : PermissionListener{
                     override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                         context?.showToast("granted")
                         numOfPermissionAllowed=+1
                         checkNoOfPerm()
                         binding.cameraBtn.isEnabled=false
                         activity?.showDialog("Permission","Trackit need permission to DrawOver Apps"){
                             HiddenCameraUtils.openDrawOverPermissionSetting(activity)
                         }
                     }

                     override fun onPermissionRationaleShouldBeShown(
                         permission: PermissionRequest?,
                         token: PermissionToken?
                     ) {
                         token?.continuePermissionRequest()
                     }

                     override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                         checkNoOfPerm()
                         numOfPermissionAllowed--
                         binding.cameraBtn.isChecked=false                   }

                 })
                 .check()
         }
            else{
             context?.showToast("end any process")
             }
        }
        binding.storageBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                Dexter.withActivity(activity)
                    .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(object : PermissionListener{
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            context?.showToast("granted")
                            numOfPermissionAllowed++
                            checkNoOfPerm()
                            binding.storageBtn.isEnabled=false
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                            token?.continuePermissionRequest()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            checkNoOfPerm()
                            numOfPermissionAllowed--
                            binding.storageBtn.isChecked=false                   }

                    })
                    .check()
            }else{

            }
        }
        binding.locationBtn.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                Dexter.withActivity(activity)
                    .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    .withListener(object : PermissionListener{
                        override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                            context?.showToast("granted")
                            numOfPermissionAllowed++
                            checkNoOfPerm()
                            binding.locationBtn.isEnabled=false
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?
                        ) {
                            token?.continuePermissionRequest()
                        }

                        override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                            checkNoOfPerm()
                            numOfPermissionAllowed--
                            binding.locationBtn.isChecked=false                   }

                    })
                    .check()
            }
        }


    }

    private fun checkNoOfPerm(){
        Log.d("debug", "$numOfPermissionAllowed")
        if (numOfPermissionAllowed==3){
            binding.navBtn.visibility=VISIBLE
        }else{
            if (binding.navBtn.visibility== GONE){
                return
            }else{
                binding.navBtn.visibility= GONE
            }
        }
    }

}
