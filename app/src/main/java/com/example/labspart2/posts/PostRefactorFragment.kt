package com.example.labspart2.posts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.labspart2.R
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class PostRefactorFragment: Fragment() {
    companion object{
        fun newInstance(postId: Int?) =
            PostRefactorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("postId", postId)
                }
            }
    }
    private val okHttpClient = OkHttpClient()
    private var postId = 0
    private var post: Post? = null
    private lateinit var postNameField: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        postId = (requireActivity().intent.getSerializableExtra("postId") as Int?)!!
        val request = Request.Builder().url("http://192.168.3.57:5000/Post/${postId}").build()
        okHttpClient.newCall(request).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                post = Post(jsonItem.getInt("id"), jsonItem.getString("post_name"))
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.post_refactor_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)

        editBtn.setOnClickListener{
            if (postNameField.text.toString().isEmpty()){
                Toast.makeText(context, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("post_name", postNameField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.57:5000/Post/${postId}").put(body).build()

                okHttpClient.newCall(request).enqueue(object : Callback{
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
            val request = Request.Builder().url("http://192.168.3.57:5000/Post/${postId}").delete().build()
            okHttpClient.newCall(request).enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {}
                override fun onResponse(call: Call, response: Response) {
                    backward()
                }
            })
        }

        backBtn.setOnClickListener{
            backward()
        }

        postNameField = view.findViewById(R.id.post_name)
        postNameField.setText(post?.postName)
        return view
    }

    fun backward(){
        val intent = PostsActivity.newIntent(context)
        context?.startActivity(intent)
    }
}
