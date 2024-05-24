package com.example.labspart2.faculty

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class FacultyRefactorActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent {
            val intent = Intent(packageContext, FacultyRefactorActivity::class.java)
            intent.putExtra("facultyId", id)
            return intent
        }
    }

    override fun createFragment(): FacultyRefactorFragment {
        val id = intent.getSerializableExtra("facultyId") as Int?
        return FacultyRefactorFragment.newInstance(id)
    }
}