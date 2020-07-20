package com.nike.trackit.core

//import android.content.Context
//import android.location.Location
//import android.os.Build
//import android.os.Looper
//import android.util.Log
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.gms.location.*
//import com.nike.trackit.api.RetrofitBuilder
//import com.nike.trackit.model.RevereCodingModel
//import com.nike.trackit.ui.MainViewModel
//import com.nike.trackit.utils.Constant
//import com.nike.trackit.utils.Constant.Companion.APIKEY
//import kotlinx.coroutines.*
//import kotlinx.coroutines.Dispatchers.IO
//import kotlinx.coroutines.Dispatchers.Main
//import retrofit2.HttpException
//import java.io.File
//import java.time.LocalDateTime
//import java.time.format.DateTimeFormatter
//import java.time.format.FormatStyle
//import java.util.*
//import javax.activation.DataHandler
//import javax.activation.FileDataSource
//import javax.mail.*
//import javax.mail.internet.InternetAddress
//import javax.mail.internet.MimeBodyPart
//import javax.mail.internet.MimeMessage
//import javax.mail.internet.MimeMultipart
//
//
//@RequiresApi(Build.VERSION_CODES.O)
//class Mail(datasource: File,val context: Context) {
//    lateinit var mFusedLocationClient: FusedLocationProviderClient
//    var logitude:String?=null
//    var latitude:String?=null
//
//    init {
//        val current = LocalDateTime.now()
//        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//        val formatted = current.format(formatter)
//
//        val coroutineScope= CoroutineScope(IO)
//
//        coroutineScope.launch(IO) {
//            withContext(Main){
//                requestNewLocationData()
//                delay(3000)
//            }
//            try {
//                Log.d("tagger","$latitude $logitude")
//                val response= RetrofitBuilder.apiService.ReverseCoding(APIKEY,latitude?:"6.6018",logitude?:"3.3515")
//                if (response.isSuccessful){
//                    val data=response.body()
//                    Properties().apply {
//                        put("mail.smtp.host", "smtp.mailgun.org")
//                        put("mail.smtp.auth", "true")
//
//                        val session=Session.getDefaultInstance(                                       this, object : Authenticator() {
//                            override fun getPasswordAuthentication(): PasswordAuthentication {
//                                return PasswordAuthentication(Constant.EMAIL,Constant.PASSWORD)
//                            }
//                        })
//                        session.getTransport("smtps")
//
//                        try {
//
//                            MimeMessage(session).apply {
//                                setFrom( InternetAddress(Constant.EMAIL))
//                                addRecipient(Message.RecipientType.TO,InternetAddress("freedom.chuks7@gmail.com"))
//                                Log.d("Debug", "freedom.chuks7@gmail.com")
//                                subject = "Alert Trackit caught someone trying to unlock your device"
//                                setText("Someone tried to unlock your ${Build.MODEL} on $formatted")
//
//
//
//                                val multipart = MimeMultipart("related")
//                                var messageBodyPart = MimeBodyPart()
//                                val htmlText = "<H3>Someone tried to unlock your ${Build.MODEL} on $formatted</H3><img src=\"cid:image\"> " +
//                                        "<h3>${data?.display_name}</h3>"
//                                messageBodyPart.setContent(htmlText, "text/html")
//                                multipart.addBodyPart(messageBodyPart)
//                                messageBodyPart= MimeBodyPart()
//                                val data=FileDataSource(datasource)
//                                messageBodyPart.dataHandler =  DataHandler(data);
//                                messageBodyPart.setHeader("Content-ID", "<image>");
//
//                                // add image to the multipart
//                                multipart.addBodyPart(messageBodyPart);
//
//                                // put everything together
//                                setContent(multipart);
//
//                                multipart.addBodyPart(messageBodyPart);
//
//                                Transport.send(this)
//
//                            }
//
//                        }catch (e:MessagingException){
//
//                            Log.d("something happenedd","$e")
//                            //retry
//                        }
//
//
//                    }
//                }
//            }catch (e:HttpException){
//                Log.d("Https: error","an error occured ${e.printStackTrace()}")
//            }
//        }
//
//    }
//
//    private fun requestNewLocationData():Int {
//        val mLocationRequest = LocationRequest.create()
//        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
//        mLocationRequest.interval = 0
//        mLocationRequest.fastestInterval = 0
//        mLocationRequest.numUpdates = 1
//        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
//        try {
//            mFusedLocationClient.requestLocationUpdates(
//                mLocationRequest, mLocationCallback,
//                Looper.myLooper()
//            )
//        }catch (e:SecurityException){
//            Log.d("debug","Security while attempting to watchPosition ${e.message}")
//        }
//return 1
//    }
//
//    private val mLocationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            val mLastLocation: Location = locationResult.lastLocation
//            if (mLastLocation!==null){
//                latitude=mLastLocation.latitude.toString()
//                logitude=mLastLocation.longitude.toString()
//                Log.d("tagger","================${mLastLocation.longitude}")
//            }
//        }
//    }
//
//
//
//}