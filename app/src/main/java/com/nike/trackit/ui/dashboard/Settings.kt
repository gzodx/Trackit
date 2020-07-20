package com.nike.trackit.ui.dashboard

import android.accounts.AccountManager
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItemsSingleChoice
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.common.AccountPicker
import com.nike.trackit.R
import com.nike.trackit.databinding.FragmentSettingsBinding
import com.nike.trackit.utils.putData
import com.nike.trackit.utils.savepref


class Settings : Fragment() {
lateinit var binding: FragmentSettingsBinding
    lateinit var sharedPreferences: SharedPreferences



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)
        sharedPreferences=context!!.savepref()
        binding.selectDialogListview.setOnItemClickListener { parent, view, position, id ->
            when(position){
                0-> getAccount()
                1-> changeNumberOfUnlockTimes()
            }
        }
        return binding.root
    }


    companion object{
        private const val REQUEST_CODE_EMAIL = 1
        fun newInstance()=Settings()
    }

    private fun getAccount(){
        try {
            val intent = AccountPicker.newChooseAccountIntent(
                null,
                null,
                arrayOf(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE),
                false,
                null,
                null,
                null,
                null
            )
            startActivityForResult(intent, REQUEST_CODE_EMAIL)
        } catch (e: ActivityNotFoundException) { // TODO
        }

    }


    private fun changeNumberOfUnlockTimes(){
        MaterialDialog(context!!).show {
            listItemsSingleChoice(R.array.Attempts){dialog, index, text ->
                when(index){
                    0->{saveAttempts("key","1")
                    }
                    1->{saveAttempts("key","2")}
                    2->{saveAttempts("key","3")}
                    3->{saveAttempts("key","4")}
                    4->{saveAttempts("key","5")}
                }
            }
            positiveButton(text = "ok") {

            }
        }
    }
    private fun saveAttempts(key:String,value:String) {
        sharedPreferences.putData(key, value)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == Activity.RESULT_OK) {
            val accountName = data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            context?.savepref()?.putData("email",accountName.toString())
            Toast.makeText(context,"Default email has been changed to $accountName",Toast.LENGTH_LONG).show()
        }
    }

}