package com.example.labspart2.posts

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.labspart2.R
import com.example.labspart2.chair.ChairsActivity
import com.example.labspart2.faculty.FacultiesActivity
import com.example.labspart2.teacher.TeachersActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PostsFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var postsAdapter: PostsAdapter? = null

    private val okHttpClient = OkHttpClient()
    private var postsList = mutableListOf<Post>()

    private lateinit var addBtn: Button
    private lateinit var facultyBtn: Button
    private lateinit var chairBtn: Button
    private lateinit var teacherBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.posts_fragment, container, false)
        recyclerView = view.findViewById(R.id.postsContainer)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        addBtn = view.findViewById(R.id.addBtn)
        facultyBtn = view.findViewById(R.id.facultyBtn)
        chairBtn = view.findViewById(R.id.chairBtn)
        teacherBtn = view.findViewById(R.id.teacherBtn)

        addBtn.setOnClickListener{
            val intent = PostAddActivity.newIntent(context)
            context?.startActivity(intent)
        }

        facultyBtn.setOnClickListener {
            val intent = FacultiesActivity.newIntent(context)
            context?.startActivity(intent)
        }

        chairBtn.setOnClickListener {
            val intent = ChairsActivity.newIntent(context)
            context?.startActivity(intent)
        }

        teacherBtn.setOnClickListener {
            val intent = TeachersActivity.newIntent(context)
            context?.startActivity(intent)
        }

        updateUI()
        return view
    }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.3.57:5000/Post").build()
        okHttpClient.newCall(request).enqueue(object: Callback{
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                postsList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    postsList.add(Post(step.getInt("id"), step.getString("post_name")))
                }
            }

        })

        if (postsAdapter == null){
            postsAdapter = PostsAdapter(postsList)
            recyclerView!!.adapter = postsAdapter
        }
    }

    private class Holder (item: View?): ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.post_item_name)

        private lateinit var post: Post

        @SuppressLint("SetTextI18n")
        fun postBinding(post1: Post){
            this.post = post1
            itemNameView?.text = post1.postName
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = PostRefactorActivity.newIntent(context, post.id)
            context.startActivity(intent)
        }

    }


    private class PostsAdapter(postList: List<Post>?): RecyclerView.Adapter<Holder?>(){
        private var posts: List<Post>? = null
        init {this.posts = postList}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.post_item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val post = posts!![position]
            holder.postBinding(post)
        }

        override fun getItemCount(): Int {
            return posts!!.size
        }


    }
}