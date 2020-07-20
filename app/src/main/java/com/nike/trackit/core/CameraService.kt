package com.nike.trackit.core

import android.Manifest.permission
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.PRIORITY_MIN
import co.nedim.maildroidx.MaildroidXType
import co.nedim.maildroidx.callback
import co.nedim.maildroidx.sendEmail
import com.androidhiddencamera.CameraConfig
import com.androidhiddencamera.CameraError.*
import com.androidhiddencamera.HiddenCameraService
import com.androidhiddencamera.HiddenCameraUtils
import com.androidhiddencamera.config.CameraFacing
import com.androidhiddencamera.config.CameraImageFormat
import com.androidhiddencamera.config.CameraResolution
import com.google.android.gms.location.*
import com.nike.trackit.R
import com.nike.trackit.api.RetrofitBuilder
import com.nike.trackit.model.RevereCodingModel
import com.nike.trackit.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class CameraService : HiddenCameraService() {
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var logitude:String?=null
    var latitude:String?=null
    lateinit var userEmail:String

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (ActivityCompat.checkSelfPermission(this, permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {
                val cameraConfig: CameraConfig = CameraConfig()
                    .getBuilder(this)
                    .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                    .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                    .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                    .build()

                startCamera(cameraConfig)

                CoroutineScope(IO).launch {
                    Log.d("tagger","capturing image")
                    withContext(Main){
                        startForeground()
                        takePicture()

                    }
                    Log.d("tagger","taken image")
                }

            } else {
                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this)
            }
        } else {
            showToast("Camera permission not available")
        }
        return Service.START_NOT_STICKY

    }

    override fun onCameraError(errorCode: Int) {
        when(errorCode){
            ERROR_CAMERA_OPEN_FAILED->{
                Log.d("tagger","camera is being used")
            }
            ERROR_IMAGE_WRITE_FAILED->{
                Log.d("tagger","camera is being used")
            }
            ERROR_CAMERA_PERMISSION_NOT_AVAILABLE->{
                Log.d("tagger","camera is being used")
            }
            ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION->{
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
            ERROR_DOES_NOT_HAVE_FRONT_CAMERA->{
                Log.d("tagger","camera is being used")
            }
        }
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onImageCapture(imageFile: File) {
        showToast("Captured location image size is ${imageFile.absoluteFile}")
        Log.d("Tagger","Captured location image size is ${imageFile.absoluteFile}")
        reverseGeoCoding(imageFile.absolutePath)
        stopSelf()
    }

    override fun onCreate() {
        userEmail=this@CameraService.savepref().get("email","null")

//        startForeground(1, Notification())
        requestNewLocationData(this)
        super.onCreate()
    }

    private fun startForeground() {
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel("my_service", "My Background Service")
            } else {
                // If earlier version channel ID is not used
                // https://developer.android.com/reference/android/support/v4/app/NotificationCompat.Builder.html#NotificationCompat.Builder(android.content.Context)
                ""
            }

        val notificationBuilder = NotificationCompat.Builder(this, channelId )
        val notification = notificationBuilder.setOngoing(true)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(PRIORITY_MIN)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        startForeground(101, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String): String{
        val chan = NotificationChannel(channelId,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }

    private fun requestNewLocationData(context: Context):Int {
        val mLocationRequest = LocationRequest.create()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        try {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
            )
        }catch (e:SecurityException){
            Log.d("debug","Security while attempting to watchPosition ${e.message}")
        }
        return 1
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            latitude=mLastLocation.latitude.toString()
            logitude=mLastLocation.longitude.toString()
            Log.d("tagger","================${mLastLocation.longitude}")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun reverseGeoCoding(filepath:String){
        CoroutineScope(IO).launch {
            val response= RetrofitBuilder.apiService.ReverseCoding(Constant.APIKEY,latitude?:"6.6018",logitude?:"3.3515")
            if (response.isSuccessful) {
                val data = response.body()
                withContext(Main){
                    sendMail(filepath,data!!.display_name)
                }
            }else{
                reverseGeoCoding(filepath)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun sendMail(filepath:String,data:String){
        val current = now()
        val formatter =DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)


        sendEmail {
            smtp("smtp.zoho.com")
            smtpUsername(Constant.EMAIL)
            smtpPassword(Constant.PASSWORD)
            port("465")
            type(MaildroidXType.HTML)
            Log.d("tagger","==================================$userEmail ================================== ")
            to(userEmail)
            from(Constant.EMAIL)
            subject("Alert Trackit caught someone trying to unlock your device")
            body("Someone tried to unlock your ${Build.MODEL} on $formatted \n $data")
            attachment(filepath)

            callback {
                timeOut(3000)
                onSuccess {
                    Log.d("MaildroidX",  "SUCCESS")
                }
                onFail {
                    Log.d("MaildroidX",  "FAIL")
                }
            }
        }
    }
}
