package com.nike.trackit.ui.dashboard

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.tabs.TabLayoutMediator
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.nike.trackit.R
import com.nike.trackit.core.AdminReceiver
import com.nike.trackit.databinding.ActivityMainBinding
import com.nike.trackit.ui.adapter.TrackitAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        setUpTabLayout()
    }

    override fun onStart() {
        checkToggleState()
        super.onStart()
    }

    private fun checkToggleState() {
        Dexter.withActivity(this@MainActivity)
            .withPermissions(listOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION))
            .withListener(object :MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()){
                       checkGpsLocationEnabled()
                    }else{

                    }

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token?.continuePermissionRequest()
                }

            }).check()
    }


//    private fun toggleFab() {
//        binding.fab.apply {
//            setOnClickListener {
//                Dexter.withActivity(this@MainActivity)
//                    .withPermissions(listOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION))
//                    .withListener(object :MultiplePermissionsListener {
//                        override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
//                            if (report!!.areAllPermissionsGranted()){
////                                Toast.makeText(this@MainActivity,"$response",Toast.LENGTH_LONG).show()
//                               checkGpsLocationEnabled()
//                            }
//                        }
//
//                        override fun onPermissionRationaleShouldBeShown(
//                            permissions: MutableList<PermissionRequest>?,
//                            token: PermissionToken?
//                        ) {
//                            token?.continuePermissionRequest()
//                        }
//
//                    }).check()
//            }
//        }
//    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    private fun checkGpsLocationEnabled(){
        val locationManager:LocationManager= getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) gpsMaterialDialog()
    }

    private fun gpsMaterialDialog(){
        MaterialDialog(this).show {
            cornerRadius(5F)
            message(text = "Your gps seems to be disabled you have to enabled it for this app to function")
            positiveButton(text = "enable") {
                startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
//                binding.fab.backgroundTintList= ColorStateList.valueOf(resources.getColor(R.color.colorPrimaryDark))
            }
            negativeButton {
                hide()
            }

        }
    }

    private val fragmentArray= arrayListOf(
        Main.newInstance(),
        Settings.newInstance()
    )

    private fun setUpTabLayout(){
        val plan=TrackitAdapter(this,fragmentArray)
        binding.pager.adapter=plan
        TabLayoutMediator(binding.tabs,binding.pager,
            TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position){
                0->tab.text="Home"
                1->tab.text="Settings"
            }
        }).attach()
    }

}
