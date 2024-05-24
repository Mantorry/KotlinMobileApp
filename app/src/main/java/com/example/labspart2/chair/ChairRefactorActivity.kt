package com.example.labspart2.chair

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class ChairRefactorActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent {
            val intent = Intent(packageContext, ChairRefactorActivity::class.java)
            intent.putExtra("chairId", id)
            return intent
        }
    }

    override fun createFragment(): ChairRefactorFragment {
        val id = intent.getSerializableExtra("chairId") as Int?
        return ChairRefactorFragment.newInstance(id)
    }
}