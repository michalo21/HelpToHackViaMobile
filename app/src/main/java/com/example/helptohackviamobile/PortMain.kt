package com.example.helptohackviamobile

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PortMain : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var portMusic: MediaPlayer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_port_main)
        portMusic = MediaPlayer.create(this, R.raw.main)
        portMusic.isLooping = true
        portMusic.start()

        var listResult = arrayListOf<Result>()

        val bundle = intent.extras
        val jsonString = bundle?.getString("listResultKey")
        val gson = Gson()
        val listResultType = object : TypeToken<ArrayList<Result>>() {}.type
        listResult = gson.fromJson(jsonString,listResultType)



        viewManager = LinearLayoutManager(this)
        viewAdapter = ResultAdapter(listResult)

        recyclerView = findViewById<RecyclerView>(R.id.recycler_scanner).apply {
            layoutManager = viewManager
            adapter = viewAdapter
            setHasFixedSize(true)
        }
    }
    override fun onResume() {
        super.onResume()
        if(!portMusic.isPlaying){
            portMusic = MediaPlayer.create(this, R.raw.main)
            portMusic.start()
        }
    }
    override fun onPause() {
        super.onPause()
        portMusic.stop()

    }
}
