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
import org.json.JSONObject
import java.io.IOException

class FacultyRefactorFragment: Fragment() {
    companion object{
        fun newInstance(facultyId: Int?) =
            FacultyRefactorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("facultyId", facultyId)
                }
            }
    }
    private val okHttpClient = OkHttpClient()
    private var facultyId = 0
    private var faculty: Faculty? = null
    private lateinit var facultyNameField: EditText
    private lateinit var facultyShortnameField: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        facultyId = (requireActivity().intent.getSerializableExtra("facultyId") as Int?)!!
        val request = Request.Builder().url("http://192.168.3.57:5000/Faculty/${facultyId}").build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                faculty = Faculty(jsonItem.getInt("id"), jsonItem.getString("faculty_name"), jsonItem.getString("faculty_short_name"))
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.faculty_refactor_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)

        editBtn.setOnClickListener{
            if (facultyNameField.text.toString().isEmpty() && facultyShortnameField.text.toString().isEmpty()){
                Toast.makeText(context, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("faculty_name", facultyNameField.text.toString())
                    .addFormDataPart("faculty_short_name", facultyShortnameField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.57:5000/Faculty/${facultyId}").put(body).build()

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
            val request = Request.Builder().url("http://192.168.3.57:5000/Faculty/${facultyId}").delete().build()
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

        facultyNameField = view.findViewById(R.id.faculty_name)
        facultyNameField.setText(faculty?.facultyName)
        facultyShortnameField = view.findViewById(R.id.faculty_shortname)
        facultyShortnameField.setText(faculty?.facultyShortName)
        return view
    }

    fun backward(){
        val intent = FacultiesActivity.newIntent(context)
        context?.startActivity(intent)
    }
}