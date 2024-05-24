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

class PostAddFragment : Fragment() {
    companion object{
        fun newInstance() = PostAddFragment()
    }

    private val okHttpClient = OkHttpClient()
    private lateinit var postNameField: EditText
    private lateinit var addBtn: Button
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.post_add_fragment, container, false)
        addBtn = view.findViewById(R.id.addBtn)
        backBtn = view.findViewById(R.id.backBtn)
        postNameField = view.findViewById(R.id.post_name)

        addBtn.setOnClickListener{
            if (postNameField.text.toString().isEmpty()){
                Toast.makeText(context, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("post_name", postNameField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.57:5000/Post").post(body).build()

                okHttpClient.newCall(request).enqueue(object : Callback{
                    override fun onFailure(call: Call, e: IOException) {}

                    override fun onResponse(call: Call, response: Response) {
                        backward()
                    }

                })
            }
        }

        backBtn.setOnClickListener{ backward() }
        return view
    }

    fun backward(){
        val intent = PostsActivity.newIntent(context)
        context?.startActivity(intent)
    }
}