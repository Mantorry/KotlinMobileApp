package com.example.labspart2.teacher

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class TeacherRefactorActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent {
            val intent = Intent(packageContext, TeacherRefactorActivity::class.java)
            intent.putExtra("teacherId", id)
            return intent
        }
    }

    override fun createFragment(): TeacherRefactorFragment {
        val id = intent.getSerializableExtra("teacherId") as Int?
        return TeacherRefactorFragment.newInstance(id)
    }
}