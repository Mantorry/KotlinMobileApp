package com.example.labspart2.chair

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
import com.example.labspart2.faculty.FacultiesActivity
import com.example.labspart2.faculty.Faculty
import com.example.labspart2.faculty.FacultyAdapter
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class ChairRefactorFragment: Fragment() {
    companion object{
        fun newInstance(chairId: Int?) =
            ChairRefactorFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("chairId", chairId)
                }
            }
    }
    private val okHttpClient = OkHttpClient()
    private var chairId = 0
    private var chair: Chair? = null
    private var facultiesList = mutableListOf<Faculty>()
    private lateinit var facultyField: Spinner
    private lateinit var codeField: EditText
    private lateinit var chairNameField: EditText
    private lateinit var chairShortnameField: EditText
    private lateinit var editBtn: Button
    private lateinit var deleteBtn: Button
    private lateinit var backBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chairId = (requireActivity().intent.getSerializableExtra("chairId") as Int?)!!
        val request = Request.Builder().url("http://192.168.3.57:5000/Chair/${chairId}").build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonItem = JSONObject(response.body()!!.string())
                chair = Chair(jsonItem.getInt("id"), jsonItem.getInt("faculty_id"),
                    jsonItem.getString("code"), jsonItem.getString("chair_name"),
                    jsonItem.getString("chair_short_name"), jsonItem.getString("faculty_short_name"))
            }
        })
        val facultyRequest = Request.Builder().url("http://192.168.3.57:5000/Faculty").build()
        okHttpClient.newCall(facultyRequest).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response) {
                val jsonList = JSONObject(response.body()!!.string()).getJSONArray("Response")
                facultiesList.clear()
                for (i in 0..<jsonList.length()){
                    val step = JSONObject(jsonList.get(i).toString())
                    facultiesList.add(Faculty(step.getInt("id"), step.getString("faculty_name"), step.getString("faculty_short_name")))
                }
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.chair_refactor_fragment, container, false)
        editBtn = view.findViewById(R.id.editBtn)
        backBtn = view.findViewById(R.id.backBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        facultyField = view.findViewById(R.id.spinner)

        facultyField.adapter = FacultyAdapter(requireContext(), facultiesList)
        facultyField.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chair?.facultyId = facultiesList[position].id
                chair?.facultyShortName = facultiesList[position].facultyShortName
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

        }

        facultyField.setSelection(facultiesList.indexOfFirst { it.id == chair?.facultyId })

        editBtn.setOnClickListener{
            if (codeField.text.toString().isEmpty() && chairNameField.text.toString().isEmpty() && chairShortnameField.text.toString().isEmpty()){
                Toast.makeText(context, "Все поля должны быть заполнены!", Toast.LENGTH_LONG).show()
            }
            else {
                val body = MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("faculty_id", chair?.facultyId.toString())
                    .addFormDataPart("code", codeField.text.toString())
                    .addFormDataPart("chair_name", chairNameField.text.toString())
                    .addFormDataPart("chair_short_name", chairShortnameField.text.toString())
                    .build()
                val request = Request.Builder().url("http://192.168.3.57:5000/Chair/${chairId}").put(body).build()

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
            val request = Request.Builder().url("http://192.168.3.57:5000/Chair/${chairId}").delete().build()
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

        codeField = view.findViewById(R.id.code)
        codeField.setText(chair?.code)
        chairNameField = view.findViewById(R.id.chair_name)
        chairNameField.setText(chair?.chairName)
        chairShortnameField = view.findViewById(R.id.chair_short_name)
        chairShortnameField.setText(chair?.chairShortName)
        return view
    }

    fun backward(){
        val intent = ChairsActivity.newIntent(context)
        context?.startActivity(intent)
    }
}