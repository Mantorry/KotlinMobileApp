package com.example.labspart2.faculty

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.labspart2.SingleFragmentActivity

class FacultiesActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, FacultiesActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = FacultiesFragment()
}