package com.nike.trackit.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.IntegerRes
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat.startActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar
import com.nike.trackit.R


/**
 * extension function for intent
 * **/
inline fun<reified T:Any> newIntent(activity: Activity):Intent= Intent(activity,T::class.java)

inline  fun <reified T:Any> Activity.navigateTo(
    noinline init: Intent.()->Unit={}){
    val intent=newIntent<T>(this)
    intent.init()
    startActivity(intent)
}


fun validateEmail(email:String):Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

fun View.showSnack(message:String){
Snackbar.make(this,message,Snackbar.LENGTH_LONG).show()
}

//Todo understand kotlin dsl core
@SuppressLint("ResourceType")
inline fun View.snack(@IntegerRes messageRes: Int, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    snack(resources.getString(messageRes), length, f)
}

inline fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.setBackgroundTint(resources.getColor(R.color.colorAccent))
    snack.show()
}

@SuppressLint("ResourceType")
fun Snackbar.action(@IntegerRes actionRes: Int, color: Int? = null, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), color, listener)
}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }

}

fun Activity.showDialog(title:String,message:String,action:()->Unit){
    MaterialDialog(this).show {
        title(text = title)
        message(text = message)

        positiveButton(R.string.agree) { dialog ->
            action()
            hide()
        }

    }


}

fun Context.showDialog(title:String,message:String){
    MaterialDialog(this).show {
        title(text = title)
        message(text = message)

        positiveButton(R.string.agree) { dialog ->
            hide()
        }

    }


}

fun Context.showToast(message: String){
    Toast.makeText(this,message,Toast.LENGTH_LONG).show()
}

//SharedPrefence Extention Method
inline fun <reified T:Any> SharedPreferences.putData(Key:String, value:T){
    val editor = this.edit()
    when (T::class) {
        Boolean::class -> editor.putBoolean(Key, value as Boolean)
        Float::class -> editor.putFloat(Key, value as Float)
        String::class -> editor.putString(Key, value as String)
        Int::class -> editor.putInt(Key, value as Int)
        Long::class -> editor.putLong(Key, value as Long)
        else -> {
            if (value is Set<*>) {
                editor.putStringSet(Key, value as Set<String>)
            }
        }
    }
    editor.commit()
}

inline fun <reified T> SharedPreferences.get(Key:String,defaultValue:T):T
{
    when(T::class){
        Boolean::class->return this.getBoolean(Key,defaultValue as Boolean) as T
        Float::class->return this.getFloat(Key,defaultValue as Float) as T
        String::class->return this.getString(Key,defaultValue as String) as T
        Int::class->return this.getInt(Key,defaultValue as Int) as T
        Long::class->return this.getLong(Key,defaultValue as Long) as T
        else->{
            if (defaultValue is Set<*>){
                return this.getStringSet(Key,defaultValue as Set<String>) as T
            }
        }
    }
    return defaultValue
}

fun Activity.savepref():SharedPreferences{
 return  applicationContext.getSharedPreferences("mypref", Context.MODE_PRIVATE)
}

fun Context.savepref():SharedPreferences{
    return  applicationContext.getSharedPreferences("mypref", Context.MODE_PRIVATE)
}

@NonNull
fun Application.isConnectedToTheInternet(): Boolean{
    val cnxManager = baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    try{
        val netInfo : NetworkInfo? = cnxManager.activeNetworkInfo
        return netInfo?.isConnectedOrConnecting ?: false
//        cm.activeNetworkInfo.isConnected
    }catch (e: Exception){
        Log.e(TAG, "isConnectedToTheInternet: ${e.message}")
    }
    return false
}
