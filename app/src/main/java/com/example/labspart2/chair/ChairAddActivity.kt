package com.example.labspart2.chair

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class ChairAddActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, ChairAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): ChairAddFragment {
        return ChairAddFragment.newInstance()
    }
}