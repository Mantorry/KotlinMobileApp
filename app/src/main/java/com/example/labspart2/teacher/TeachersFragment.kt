package com.example.labspart2.teacher

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
import com.example.labspart2.chair.ChairsActivity
import com.example.labspart2.faculty.FacultiesActivity
import com.example.labspart2.posts.PostsActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class TeachersFragment: Fragment() {
    private var recyclerView: RecyclerView? = null
    private var teachersAdapter: TeachersAdapter? = null

    private val okHttpClient = OkHttpClient()
    private var teachersList = mutableListOf<Teacher>()

    private lateinit var addBtn: Button
    private lateinit var facultyBtn: Button
    private lateinit var postBtn: Button
    private lateinit var chairBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.teachers_fragment, container, false)
        recyclerView = view.findViewById(R.id.teachersContainer)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        addBtn = view.findViewById(R.id.addBtn)
        facultyBtn = view.findViewById(R.id.facultyBtn)
        postBtn = view.findViewById(R.id.postBtn)
        chairBtn = view.findViewById(R.id.chairBtn)

        addBtn.setOnClickListener{
            val intent = TeacherAddActivity.newIntent(context)
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

        chairBtn.setOnClickListener {
            val intent = ChairsActivity.newIntent(context)
            context?.startActivity(intent)
        }

        updateUI()
        return view
    }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.3.57:5000/Teacher").build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                teachersList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    teachersList.add(
                        Teacher(step.getInt("id"), step.getInt("chair_id"),
                        step.getInt("post_id"), step.getString("second_name"),
                        step.getString("first_name"), step.getString("last_name"),
                        step.getString("phone"), step.getString("email"),
                        step.getString("chair_short_name"), step.getString("post_name"),)
                    )
                }
            }

        })

        if (teachersAdapter == null){
            teachersAdapter = TeachersAdapter(teachersList)
            recyclerView!!.adapter = teachersAdapter
        }
    }

    private class Holder (item: View?): RecyclerView.ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.teacher_item_name)

        private lateinit var teacher: Teacher

        @SuppressLint("SetTextI18n")
        fun teacherBinding(teacher1: Teacher){
            this.teacher = teacher1
            itemNameView?.text = teacher1.secondName + " " + teacher1.firstName + " " + teacher1.lastName
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = TeacherRefactorActivity.newIntent(context, teacher.id)
            context.startActivity(intent)
        }

    }


    private class TeachersAdapter(teacherList: List<Teacher>?): RecyclerView.Adapter<Holder?>(){
        private var teachers: List<Teacher>? = null
        init {this.teachers = teacherList}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.teacher_item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val teacher = teachers!![position]
            holder.teacherBinding(teacher)
        }

        override fun getItemCount(): Int {
            return teachers!!.size
        }


    }
}