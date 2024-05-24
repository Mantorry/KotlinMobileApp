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

class TeacherAddFragment: Fragment() {
    companion object{
        fun newInstance() = TeacherAddFragment()
    }
    private val okHttpClient = OkHttpClient()
    private var chairsList = mutableListOf<Chair>()
    private var postsList = mutableListOf<Post>()
    private var chairId = 0
    private var postId = 0
    private lateinit var chairField: Spinner
    private lateinit var postField: Spinner
    private lateinit var secondNameField: EditText
    private lateinit var firstNameField: EditText
    private lateinit var lastNameField: EditText
    private lateinit var phone: EditText
    private lateinit var email: EditText
    private lateinit var addBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val chairRequest = Request.Builder().url("http://192.168.3.57:5000/Chair").build()
        okHttpClient.newCall(chairRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                chairsList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    chairsList.add(
                        Chair(step.getInt("id"), step.getInt("faculty_id"),
                        step.getString("code"), step.getString("chair_name"),
                        step.getString("chair_short_name"), step.getString("faculty_short_name"))
                    )
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
        val view = inflater.inflate(R.layout.teacher_add_fragment, container, false)
        addBtn = view.findViewById(R.id.addBtn)
        backBtn = view.findViewById(R.id.backBtn)
        chairField = view.findViewById(R.id.spinner_chair)
        postField = view.findViewById(R.id.spinner_post)
        secondNameField = view.findViewById(R.id.second_name)
        firstNameField = view.findViewById(R.id.first_name)
        lastNameField = view.findViewById(R.id.last_name)
        phone = view.findViewById(R.id.ph_field)
        email = view.findViewById(R.id.em_field)

        chairField.adapter = ChairAdapter(requireContext(), chairsList)
        chairField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chairId = chairsList[position].id
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
                postId = postsList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        addBtn.setOnClickListener{
            if (chairId.toString().isEmpty() && postId.toString().isEmpty() &&
                secondNameField.text.toString().isEmpty() && firstNameField.text.toString().isEmpty()
                && lastNameField.text.toString().isEmpty()
                && phone.text.toString().isEmpty() && email.text.toString().isEmpty()){
                Toast.makeText(context, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("chair_id", chairId.toString())
                    .addFormDataPart("post_id", postId.toString())
                    .addFormDataPart("second_name", secondNameField.text.toString())
                    .addFormDataPart("first_name", firstNameField.text.toString())
                    .addFormDataPart("last_name", lastNameField.text.toString())
                    .addFormDataPart("phone", phone.text.toString())
                    .addFormDataPart("email", email.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.57:5000/Teacher").post(body).build()

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

        backBtn.setOnClickListener{
            backward()
        }

        return view
    }

    fun backward(){
        val intent = TeachersActivity.newIntent(context)
        context?.startActivity(intent)
    }
}