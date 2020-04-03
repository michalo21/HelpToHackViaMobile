package com.example.helptohackviamobile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class LinuxFragment : Fragment() {

    private var linearLayoutManager: LinearLayoutManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("WrongConstant")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.linux_fragment, container, false)
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.recycler_linux) as RecyclerView
        linearLayoutManager = LinearLayoutManager(activity, LinearLayout.VERTICAL, false)

        val listBundle = arguments!!.getStringArrayList("hostLinux")
        val lista: ArrayList<Ip> = ArrayList()
        for(i in listBundle!!){
            lista.add(Ip(i))
        }

        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = IpAdapter(lista) // Your adapter
        recyclerView.setHasFixedSize(true);
        return rootView



    }


}


