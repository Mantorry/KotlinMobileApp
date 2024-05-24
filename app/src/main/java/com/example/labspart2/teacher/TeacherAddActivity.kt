package com.example.labspart2.teacher

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class TeacherAddActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, TeacherAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): TeacherAddFragment {
        return TeacherAddFragment.newInstance()
    }
}