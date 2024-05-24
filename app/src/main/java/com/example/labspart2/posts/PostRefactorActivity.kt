package com.example.labspart2.posts

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class PostRefactorActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?, id:Int?): Intent{
            val intent = Intent(packageContext, PostRefactorActivity::class.java)
            intent.putExtra("postId", id)
            return intent
        }
    }

    override fun createFragment(): PostRefactorFragment {
        val id = intent.getSerializableExtra("postId") as Int?
        return PostRefactorFragment.newInstance(id)
    }
}