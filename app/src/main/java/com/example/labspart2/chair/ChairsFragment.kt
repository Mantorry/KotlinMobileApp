package com.example.labspart2.chair

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.labspart2.R
import com.example.labspart2.faculty.FacultiesActivity
import com.example.labspart2.posts.PostsActivity
import com.example.labspart2.teacher.TeachersActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ChairsFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var chairsAdapter: ChairsAdapter? = null

    private val okHttpClient = OkHttpClient()
    private var chairsList = mutableListOf<Chair>()

    private lateinit var addBtn: Button
    private lateinit var facultyBtn: Button
    private lateinit var postBtn: Button
    private lateinit var teacherBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.chairs_fragment, container, false)
        recyclerView = view.findViewById(R.id.chairsContainer)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        addBtn = view.findViewById(R.id.addBtn)
        facultyBtn = view.findViewById(R.id.facultyBtn)
        postBtn = view.findViewById(R.id.postBtn)
        teacherBtn = view.findViewById(R.id.teacherBtn)

        addBtn.setOnClickListener{
            val intent = ChairAddActivity.newIntent(context)
            context?.startActivity(intent)
        }

        facultyBtn.setOnClickListener {
            val intent = FacultiesActivity.newIntent(context)
            context?.startActivity(intent)
        }

        postBtn.setOnClickListener {
            val intent = PostsActivity.newIntent(context)
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
        val request = Request.Builder().url("http://192.168.3.57:5000/Chair").build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                chairsList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    chairsList.add(Chair(step.getInt("id"), step.getInt("faculty_id"),
                        step.getString("code"), step.getString("chair_name"),
                        step.getString("chair_short_name"), step.getString("faculty_short_name")))
                }
            }

        })

        if (chairsAdapter == null){
            chairsAdapter = ChairsAdapter(chairsList)
            recyclerView!!.adapter = chairsAdapter
        }
    }

    private class Holder (item: View?): RecyclerView.ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.chair_item_name)

        private lateinit var chair: Chair

        @SuppressLint("SetTextI18n")
        fun chairBinding(chair1: Chair){
            this.chair = chair1
            itemNameView?.text = chair1.code + " | " + chair1.chairName + " " + chair1.chairShortName
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = ChairRefactorActivity.newIntent(context, chair.id)
            context.startActivity(intent)
        }

    }


    private class ChairsAdapter(chairList: List<Chair>?): RecyclerView.Adapter<Holder?>(){
        private var chairs: List<Chair>? = null
        init {this.chairs = chairList}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.chair_item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val chair = chairs!![position]
            holder.chairBinding(chair)
        }

        override fun getItemCount(): Int {
            return chairs!!.size
        }


    }
}