package com.example.labspart2.faculty

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class FacultyAddActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, FacultyAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): FacultyAddFragment {
        return FacultyAddFragment.newInstance()
    }
}