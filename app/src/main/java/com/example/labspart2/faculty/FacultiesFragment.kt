package com.example.labspart2.faculty

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
import com.example.labspart2.R
import com.example.labspart2.chair.ChairsActivity
import com.example.labspart2.posts.PostsActivity
import com.example.labspart2.teacher.TeachersActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class FacultiesFragment: Fragment(){
    private var recyclerView: RecyclerView? = null
    private var facultiesAdapter: FacultiesAdapter? = null

    private val okHttpClient = OkHttpClient()
    private var facultiesList = mutableListOf<Faculty>()

    private lateinit var addBtn: Button
    private lateinit var postBtn: Button
    private lateinit var chairBtn: Button
    private lateinit var teacherBtn: Button


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.faculties_fragment, container, false)
        recyclerView = view.findViewById(R.id.facultiesContainer)
        recyclerView!!.layoutManager = LinearLayoutManager(activity)
        addBtn = view.findViewById(R.id.addBtn)
        postBtn = view.findViewById(R.id.postBtn)
        chairBtn = view.findViewById(R.id.chairBtn)
        teacherBtn = view.findViewById(R.id.teacherBtn)

        addBtn.setOnClickListener{
            val intent = FacultyAddActivity.newIntent(context)
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

        teacherBtn.setOnClickListener {
            val intent = TeachersActivity.newIntent(context)
            context?.startActivity(intent)
        }

        updateUI()
        return view
    }

    private fun updateUI(){
        val request = Request.Builder().url("http://192.168.3.57:5000/Faculty").build()
        okHttpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
            }
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                facultiesList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    facultiesList.add(Faculty(step.getInt("id"), step.getString("faculty_name"), step.getString("faculty_short_name")))
                }
            }

        })

        if (facultiesAdapter == null){
            facultiesAdapter = FacultiesAdapter(facultiesList)
            recyclerView!!.adapter = facultiesAdapter
        }
    }

    private class Holder (item: View?): RecyclerView.ViewHolder(item!!), View.OnClickListener {
        var itemNameView: TextView? = itemView.findViewById(R.id.faculty_item_name)

        private lateinit var faculty: Faculty

        @SuppressLint("SetTextI18n")
        fun facultyBinding(faculty1: Faculty){
            this.faculty = faculty1
            itemNameView?.text = faculty1.facultyName + " " + faculty1.facultyShortName
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val context = v!!.context
            val intent = FacultyRefactorActivity.newIntent(context, faculty.id)
            context.startActivity(intent)
        }

    }


    private class FacultiesAdapter(facultyList: List<Faculty>?): RecyclerView.Adapter<Holder?>(){
        private var faculties: List<Faculty>? = null
        init {this.faculties = facultyList}

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            var layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.faculty_item, parent, false)
            return Holder(view)
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val faculty = faculties!![position]
            holder.facultyBinding(faculty)
        }

        override fun getItemCount(): Int {
            return faculties!!.size
        }


    }
}