package com.example.labspart2.posts

import android.content.Context
import android.content.Intent
import com.example.labspart2.SingleFragmentActivity

class PostAddActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            val intent = Intent(packageContext, PostAddActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): PostAddFragment {
        return PostAddFragment.newInstance()
    }
}