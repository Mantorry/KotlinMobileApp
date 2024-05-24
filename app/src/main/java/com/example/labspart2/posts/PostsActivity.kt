package com.example.labspart2.posts

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.labspart2.SingleFragmentActivity

class PostsActivity: SingleFragmentActivity() {
    companion object{
        fun newIntent(packageContext: Context?): Intent?{
            var intent = Intent(packageContext, PostsActivity::class.java)
            return intent
        }
    }

    override fun createFragment(): Fragment = PostsFragment()
}