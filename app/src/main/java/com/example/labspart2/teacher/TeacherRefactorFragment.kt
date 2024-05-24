package com.example.labspart2.teacher

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.labspart2.R
import com.example.labspart2.chair.Chair
import com.example.labspart2.chair.ChairAdapter
import com.example.labspart2.chair.ChairRefactorFragment
import com.example.labspart2.posts.Post
import com.example.labspart2.posts.PostAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class TeacherRefactorFragment: Fragment() {
    companion object{
        fun newInstance(teacherId: Int?) =
            TeacherRefactorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("teacherId", teacherId)
                }
            }
    }
    private val okHttpClient = OkHttpClient()
    private var teacherId = 0
    private var teacher: Teacher? = null
    private var chairsList = mutableListOf<Chair>()
    private var postsList = mutableListOf<Post>()
    private lateinit var chairField: Spinner
    private lateinit var postField: Spinner
    private lateinit var secondNameField: EditText
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        teacherId = (requireActivity().intent.getSerializableExtra("teacherId") as Int?)!!
        val request = Request.Builder().url("http://192.168.3.57:5000/Teacher/${teacherId}").build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                teacher = Teacher(jsonItem.getInt("id"), jsonItem.getInt("chair_id"),
                    jsonItem.getInt("post_id"), jsonItem.getString("second_name"),
                    jsonItem.getString("first_name"), jsonItem.getString("last_name"),
                    jsonItem.getString("phone"), jsonItem.getString("email"),
                    jsonItem.getString("chair_short_name"), jsonItem.getString("post_name"),)
            }
        })
        val chairRequest = Request.Builder().url("http://192.168.3.57:5000/Chair").build()
        okHttpClient.newCall(chairRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
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
        val postRequest = Request.Builder().url("http://192.168.3.57:5000/Post").build()
        okHttpClient.newCall(postRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                postsList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    postsList.add(Post(step.getInt("id"), step.getString("post_name")))
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.teacher_refactor_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        chairField = view.findViewById(R.id.spinner_chair)
        postField = view.findViewById(R.id.spinner_post)

        chairField.adapter = ChairAdapter(requireContext(), chairsList)
        chairField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                teacher?.chairId = chairsList[position].id
                teacher?.chairShortName = chairsList[position].chairShortName
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        postField.adapter = PostAdapter(requireContext(), postsList)
        postField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                teacher?.postId = postsList[position].id
                teacher?.postName = postsList[position].postName
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        chairField.setSelection(chairsList.indexOfFirst { it.id == teacher?.chairId })
        postField.setSelection(postsList.indexOfFirst { it.id == teacher?.postId })

        editBtn.setOnClickListener{
            if (secondNameField.text.toString().isEmpty() && firstNameField.text.toString().isEmpty() && lastNameField.text.toString().isEmpty()
                && phone.text.toString().isEmpty() && email.text.toString().isEmpty()){
                Toast.makeText(context, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("chair_id", teacher?.chairId.toString())
                    .addFormDataPart("post_id", teacher?.postId.toString())
                    .addFormDataPart("second_name", secondNameField.text.toString())
                    .addFormDataPart("first_name", firstNameField.text.toString())
                    .addFormDataPart("last_name", lastNameField.text.toString())
                    .addFormDataPart("phone", phone.text.toString())
                    .addFormDataPart("email", email.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.57:5000/Teacher/${teacherId}").put(body).build()

                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Toast.makeText(context, "Произошла внутренняя ошибка сети, сервис не доступен!", Toast.LENGTH_LONG).show()
                    }
                    override fun onResponse(call: Call, response: Response) {
                        backward()
                    }

                })
            }
        }

        deleteBtn.setOnClickListener{
            val request = Request.Builder().url("http://192.168.3.57:5000/Teacher/${teacherId}").delete().build()
            okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    backward()
                }
            })
        }

        backBtn.setOnClickListener{
            backward()
        }

        secondNameField = view.findViewById(R.id.second_name)
        secondNameField.setText(teacher?.secondName)
        firstNameField = view.findViewById(R.id.first_name)
        firstNameField.setText(teacher?.firstName)
        lastNameField = view.findViewById(R.id.last_name)
        lastNameField.setText(teacher?.lastName)
        phone = view.findViewById(R.id.ph_field)
        phone.setText(teacher?.phone)
        email = view.findViewById(R.id.em_field)
        email.setText(teacher?.email)
        return view
    }

    fun backward(){
        val intent = TeachersActivity.newIntent(context)
        context?.startActivity(intent)
    }
}