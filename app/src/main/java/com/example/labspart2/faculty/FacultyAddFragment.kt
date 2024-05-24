package com.example.labspart2.faculty

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
import java.io.IOException

class FacultyAddFragment: Fragment() {
    companion object{
        fun newInstance() = FacultyAddFragment()
    }

    private val okHttpClient = OkHttpClient()
    private lateinit var facultyNameField: EditText
    private lateinit var facultyShortnameField: EditText
    private lateinit var addBtn: Button
    private lateinit var backBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.faculty_add_fragment, container, false)
        addBtn = view.findViewById(R.id.addBtn)
        backBtn = view.findViewById(R.id.backBtn)
        facultyNameField = view.findViewById(R.id.faculty_name)
        facultyShortnameField = view.findViewById(R.id.faculty_shortname)

        addBtn.setOnClickListener{
            if (facultyNameField.text.toString().isEmpty() && facultyShortnameField.text.toString().isEmpty()){
                Toast.makeText(context, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("faculty_name", facultyNameField.text.toString())
                    .addFormDataPart("faculty_short_name", facultyShortnameField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.57:5000/Faculty").post(body).build()

                okHttpClient.newCall(request).enqueue(object : Callback {
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
        val intent = FacultiesActivity.newIntent(context)
        context?.startActivity(intent)
    }
}