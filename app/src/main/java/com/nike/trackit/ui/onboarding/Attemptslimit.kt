package com.nike.trackit.ui.onboarding


import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.nike.trackit.R
import com.nike.trackit.databinding.FragmentAttemptsLimitBinding
import com.nike.trackit.utils.get
import com.nike.trackit.utils.putData
import com.nike.trackit.utils.savepref


class Attemptslimit : Fragment() {

    lateinit var binding: FragmentAttemptsLimitBinding
    lateinit var navController: NavController
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       binding=DataBindingUtil.inflate(inflater, R.layout.fragment_attempts_limit, container, false)
        navController=findNavController()
        sharedPreferences=context!!.savepref()
        navigation()
        return binding.root
    }

    fun navigation(){
        binding.next.setOnClickListener {
        navController.navigate(R.id.action_attempts_limit_to_alertEmail)
        }

        binding.back.setOnClickListener {
            navController.navigateUp()
        }

        binding.spinner.setOnItemSelectedListener { view, position, id, item ->
          when(position){
              0->{saveAttempts("key","")
                  binding.next.visibility=GONE
              }
              1->{saveAttempts("key","1")}
              2->{saveAttempts("key","2")}
              3->{saveAttempts("key","3")}
              4->{saveAttempts("key","4")}
          }
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {

        activity?.menuInflater?.inflate(R.menu.dropdown_menu,menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.failed_attempts_1->{}
        }
        return super.onContextItemSelected(item)
    }


    private fun saveAttempts(key:String,value:String) {
        binding.next.visibility=VISIBLE
        sharedPreferences.putData(key, value)
    }

    override fun onResume() {
      if (sharedPreferences.get("key","null").contentEquals("null")){
          binding.spinner.setItems(
              "Attempts limits",
              "1 failed attempt",
              "2 failed attempt",
              "3 failed attempt",
              "4 failed attempt"
          )
      }else{
          binding.next.visibility= VISIBLE
          binding.spinner.setItems(
              sharedPreferences.get("key","null"),
              "1 failed attempt",
              "2 failed attempt",
              "3 failed attempt",
              "4 failed attempt"
          )
      }
        super.onResume()
    }


}
