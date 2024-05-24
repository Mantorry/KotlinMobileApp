package com.example.labspart2.chair

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.labspart2.R

class ChairAdapter(context: Context, list: List<Chair>) : ArrayAdapter<Chair>(context, 0 , list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val chair = getItem(position)
        val view = convertView?: LayoutInflater.from(context).inflate(R.layout.selector, parent, false)
        if (chair != null){
            val name = view.findViewById<TextView>(R.id.name)
            name.text = chair.chairShortName
        }
        return view
    }
}