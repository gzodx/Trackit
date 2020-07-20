package com.nike.trackit.core

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Context.DEVICE_POLICY_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.location.Location
import android.os.Build
import android.os.Looper
import android.os.StrictMode
import android.os.UserHandle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.nike.trackit.api.RetrofitBuilder
import com.nike.trackit.ui.onboarding.OnboardingMain
import com.nike.trackit.utils.get
import com.nike.trackit.utils.putData
import com.nike.trackit.utils.savepref
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.FROYO)
class AdminReceiver : DeviceAdminReceiver() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
    }

    override fun onUserStarted(context: Context, intent: Intent, startedUser: UserHandle) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        super.onUserStarted(context, intent, startedUser)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onPasswordFailed(context: Context, intent: Intent, user: UserHandle) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        sharedPreferences= context.savepref()
        val noOfTries:Int=sharedPreferences.get("key","null").toInt()
        val mgr =
            context.getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val no = mgr.currentFailedPasswordAttempts

        if (no >= noOfTries) {
            val policy =StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                context.startForegroundService(Intent(context, CameraService::class.java))
            }else{
                context.startService(Intent(context, CameraService::class.java))
            }

        }
        super.onPasswordFailed(context, intent, user)
    }

    override fun onPasswordSucceeded(context: Context, intent: Intent, user: UserHandle) {
//        sharedPreferences.putData()
        super.onPasswordSucceeded(context, intent, user)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        sharedPreferences= context.savepref()
        sharedPreferences.putData("key","null")
//        ContextCompat.startActivity(context, Intent(context,OnboardingMain::class.java),null)
        super.onDisabled(context, intent)
    }

}

