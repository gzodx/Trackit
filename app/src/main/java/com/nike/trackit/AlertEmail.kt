package com.nike.trackit


import android.accounts.AccountManager
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.GoogleAuthUtil
import com.google.android.gms.common.AccountPicker
import com.nike.trackit.databinding.FragmentAlertEmailBinding
import com.nike.trackit.utils.get
import com.nike.trackit.utils.putData
import com.nike.trackit.utils.savepref


class AlertEmail : Fragment() {

    companion object{
        private const val REQUEST_CODE_EMAIL = 1
    }
    lateinit var binding:FragmentAlertEmailBinding
    lateinit var navigationController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_alert_email, container, false)
        navigationController=findNavController()
        setUpNavigation()
        checkValidForValidEmail()
        return binding.root
    }

    private fun setUpNavigation(){
        binding.activate.setOnClickListener {
            getAccount()
        }
        binding.next.setOnClickListener { navigationController.navigate(R.id.action_alertEmail_to_permission) }
        binding.back.setOnClickListener { navigationController.navigateUp() }
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



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_EMAIL && resultCode == RESULT_OK) {
            val accountName = data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)
            context?.savepref()?.putData("email",accountName.toString())
            activatedAdmin()
            Log.d("debug","$accountName========================")
        }
    }

    fun activatedAdmin(){
        binding.activate.visibility= View.GONE
        binding.activated.visibility= View.VISIBLE
        binding.next.visibility= View.VISIBLE
    }

    fun checkValidForValidEmail(){
        if ( context?.savepref()?.get("email","null")=="null"){
            binding.activate.visibility= View.VISIBLE
            binding.activated.visibility= View.GONE
            binding.next.visibility= View.VISIBLE
        }else{
            activatedAdmin()
        }
    }


}
