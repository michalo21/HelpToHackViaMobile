package com.example.helptohackviamobile

import android.annotation.TargetApi
import android.content.Intent
import android.media.MediaPlayer
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.delay
import org.apache.commons.net.util.SubnetUtils
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface


class MainActivity : AppCompatActivity() {
    private val listHost: MutableMap<String,String> = mutableMapOf()
    private lateinit var mainMusic: MediaPlayer
    private lateinit var animeWOW: MediaPlayer
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainMusic = MediaPlayer.create(this, R.raw.elevator)
        mainMusic.isLooping = true
        mainMusic.start()

        animeWOW = MediaPlayer.create(this, R.raw.ani)

        progressBar = ip_progress_bar
        progressBar.visibility = View.VISIBLE

        val statusWifi: TextView = wifi_status

        button_start.setOnClickListener {


            statusWifi.visibility = View.INVISIBLE

            button_start.isClickable = false
            doAsync {
                val ipAndroidApp: InetAddress? = Inet4Address.getByName(getLocalIpAddress())

                if(ipAndroidApp.toString() != "/0.0.0.0"){

                    val subnet: String? = NetworkInterface.getByInetAddress(ipAndroidApp).interfaceAddresses[1].networkPrefixLength.toString()
                    val ipAndroidAppSliced: String = ipAndroidApp.toString().substring(1)
                    val nameIP = "$ipAndroidAppSliced/$subnet"
                    val utils = SubnetUtils(nameIP)

                    progressBar.max = utils.info.allAddresses.size

                    var iterator = 0

                    for (i in utils.info.allAddresses) {
                        val helper = Inet4Address.getByName(i)
                        if (helper.isReachable(110)) {
                            iterator += 1
                            progressBar.progress = iterator
                            if (pingCommand(i) == "128") {
                                listHost += (Ip(i).ipaddress to "128")
                            } else if (pingCommand(i) == "64") {
                                listHost += (Ip(i).ipaddress to "64")
                            } else {
                                listHost += (Ip(i).ipaddress to pingCommand(i))
                            }
                        } else {
                            iterator += 1
                            progressBar.progress = iterator
                            continue
                        }

                    }
                    uiThread {
                        mainMusic.stop()
                        button_start.isClickable = true
                        animeWOW.start()
                        val gson = Gson()
                        val intent = Intent(this@MainActivity, TabMain::class.java)
                        val jsonString = gson.toJson(listHost)
                        intent.putExtra("listHostKey", jsonString)
                        startActivity(intent)
                        progressBar.progress = 0

                    }
                }else{
                uiThread {
                    button_start.isClickable = true
                    statusWifi.visibility = View.VISIBLE
                }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(!mainMusic.isPlaying){
            mainMusic = MediaPlayer.create(this, R.raw.elevator)
            mainMusic.start()
        }
    }
    override fun onPause() {
        super.onPause()
        mainMusic.stop()
    }


    private fun getLocalIpAddress(): String?{
        try {
            val wifiManager: WifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            return ipToString(wifiManager.connectionInfo.ipAddress)
        }
        catch (ex: Exception){
            Log.e("IP Address", ex.toString())
        }
        return null
    }

    private fun ipToString(i: Int): String{
        return (i and 0xFF).toString() + "." +
                (i shr 8 and 0xFF) + "." +
                (i shr 16 and 0xFF) + "." +
                (i shr 24 and 0xFF)
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun pingCommand(ip: String): String {
        val runtime = Runtime.getRuntime()
        val returner = try{
            val cmd = arrayOf("/bin/sh", "-c", "ping -c 1 $ip | grep ttl | sed 's/.*ttl=\\([[:digit:]]*\\).*/\\1/' ")
            val process = runtime.exec(cmd)
            val bufferedReader = BufferedReader(InputStreamReader(process.inputStream))
            val log = StringBuilder()
            for (line in bufferedReader.lines()) {
                log.append(line);
            }
            return log.toString()
        }
        catch (ex: IOException){
            Log.e("PingCommand", ex.toString())
        }
        return returner.toString()
    }
}


