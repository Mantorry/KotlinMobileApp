package com.example.labspart2.teacher

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.labspart2.SingleFragmentActivity

class TeachersActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, TeachersActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = TeachersFragment()
}