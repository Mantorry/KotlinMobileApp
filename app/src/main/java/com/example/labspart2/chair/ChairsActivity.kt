package com.example.labspart2.chair

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.labspart2.SingleFragmentActivity

class ChairsActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, ChairsActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = ChairsFragment()
}