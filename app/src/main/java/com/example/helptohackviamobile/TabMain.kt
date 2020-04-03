package com.example.helptohackviamobile

import android.media.MediaPlayer
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.tab_main.*


class TabMain : AppCompatActivity() {
    private val hostLinux = arrayListOf<String>()
    private val hostWindows = arrayListOf<String>()
    private val hostOther = arrayListOf<String>()
    private var listHost: MutableMap<String,String> = mutableMapOf()
    private lateinit var secondMusic: MediaPlayer



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_main)
        secondMusic = MediaPlayer.create(this, R.raw.second)
        secondMusic.isLooping = true


        setSupportActionBar(toolbar)

        val bundle = intent.extras
        val jsonString = bundle?.getString("listHostKey")
        val gson = Gson()
        val listHostType = object : TypeToken<MutableMap<String, String>>() {}.type
        listHost = gson.fromJson(jsonString, listHostType)
        for ((k, v) in listHost) {
            if(v == "128"){
                hostWindows.add(k)
            }else if(v == "64"){
               hostLinux.add(k)
            }else{
                hostOther.add(k)
            }
        }
        if(hostWindows.isNullOrEmpty()){
            hostWindows.add("NO HOSTS WITH WINDOWS OS")
        }
        if(hostLinux.isNullOrEmpty()){
            hostLinux.add("NO HOSTS WITH LINUX OS")
        }
        if(hostOther.isNullOrEmpty()){
            hostOther.add("NO HOSTS WITH OTHER OS")
        }

        val windowsFragment = WindowsFragment()
        val bundleWindows = Bundle()
        bundleWindows.putStringArrayList("hostWindows", hostWindows)
        windowsFragment.arguments = bundleWindows

        val linuxFragment = LinuxFragment()
        val bundleLinux = Bundle()
        bundleLinux.putStringArrayList("hostLinux", hostLinux)
        linuxFragment.arguments = bundleLinux

        val otherFragment = OtherFragment()
        val bundleOther = Bundle()
        bundleOther.putStringArrayList("hostOther", hostOther)
        otherFragment.arguments = bundleOther

        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(windowsFragment, "Windows")
        adapter.addFragment(linuxFragment, "Linux")
        adapter.addFragment(otherFragment, "Other")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
        secondMusic.start()
    }

    override fun onResume() {
        super.onResume()
        if(!secondMusic.isPlaying){
        secondMusic = MediaPlayer.create(this, R.raw.second)
        secondMusic.start()
        }
    }
    override fun onPause() {
        super.onPause()
        secondMusic.stop()
    }
}