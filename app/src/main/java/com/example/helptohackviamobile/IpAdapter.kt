package com.example.helptohackviamobile

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.InetSocketAddress
import java.net.Socket


class IpAdapter(private val iplist: List<Ip>) : RecyclerView.Adapter<IpAdapter.FlagViewHolder>() {
    private val topPorts = mapOf("http" to "80", "telnet" to "23", "https" to "443", "ftp" to "21", "ssh" to "22", "smtp" to "25", "ms-wbt-server" to "3389", "pop3" to "110", "microsoft-ds" to "445", "netbios-ssn" to "139", "imap" to "143", "domain" to "53", "msrpc" to "135", "mysql" to "3306", "http-proxy" to "8080", "pptp" to "1723", "rpcbind" to "111", "pop3s" to "995", "imaps" to "993", "vnc" to "5900", "NFS-or-IIS" to "1025", "submission" to "587", "sun-answerbook" to "8888", "smux" to "199", "h323q931" to "1720", "smtps" to "465", "afp" to "548", "ident" to "113", "hosts2-ns" to "81", "X11:1" to "6001", "snet-sensor-mgmt" to "10000", "shell" to "514", "sip" to "5060", "bgp" to "179", "LSA-or-nterm" to "1026", "cisco-sccp" to "2000", "https-alt" to "8443", "http-alt" to "8000", "filenet-tms" to "32768", "rtsp" to "554", "rsftp" to "26", "ms-sql-s" to "1433", "unknown" to "49152", "dc" to "2001", "printer" to "515", "http" to "8008", "unknown" to "49154", "IIS" to "1027", "nrpe" to "5666", "ldp" to "646", "upnp" to "5000", "pcanywheredata" to "5631", "ipp" to "631", "unknown" to "49153", "blackice-icecap" to "8081", "nfs" to "2049", "kerberos-sec" to "88", "finger" to "79", "vnc-http" to "5800", "pop3pw" to "106", "ccproxy-ftp" to "2121", "nfsd-status" to "1110", "unknown" to "49155", "X11" to "6000", "login" to "513", "ftps" to "990", "wsdapi" to "5357", "svrloc" to "427", "unknown" to "49156", "klogin" to "543", "kshell" to "544", "admdog" to "5101", "news" to "144", "echo" to "7", "ldap" to "389", "ajp13" to "8009", "squid-http" to "3128", "snpp" to "444", "abyss" to "9999", "airport-admin" to "5009", "realserver" to "7070", "aol" to "5190", "ppp" to "3000", "postgresql" to "5432", "upnp" to "1900", "mapper-ws_ethd" to "3986", "daytime" to "13", "ms-lsa" to "1029", "discard" to "9", "ida-agent" to "5051", "unknown" to "6646", "unknown" to "49157", "unknown" to "1028", "rsync" to "873", "wms" to "1755", "pn-requester" to "2717", "radmin" to "4899", "jetdirect" to "9100", "nntp" to "119", "time" to "37")
    private val clickableItem = true
    private lateinit var progressBar: ProgressBar


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.ip_layout, parent,false)
        return FlagViewHolder(view)
    }

    override fun getItemCount() = iplist.size

    override fun onBindViewHolder(viewHolder: FlagViewHolder, position: Int) {
        viewHolder.bind(iplist[position])

        viewHolder.itemView.setOnClickListener(View.OnClickListener {
            progressBar = viewHolder.itemView.findViewById(R.id.circle_bar)
            progressBar.visibility = View.VISIBLE
            val listResult = ArrayList<Result>()
            val position1 = viewHolder.adapterPosition
            val ip = (iplist[position1].ipaddress)
            viewHolder.itemView.isClickable = false
            doAsync {
                for ((k, v) in topPorts) {
                    try {
                        val socket = Socket()
                        socket.connect(InetSocketAddress(ip, v.toInt()), 200)
                        socket.close()
                        listResult.add(Result(k, v, "OPEN"))
                    } catch (ex: Exception) {

                    }
                }
                if(listResult.isNullOrEmpty()){
                    listResult.add(Result("NO", "PORTS", "OPEN"))
                }
                uiThread{
                    progressBar.visibility = View.GONE
                    val gson = Gson()
                    val jsonString = gson.toJson(listResult)
                    val intent = Intent(viewHolder.itemView.context, PortMain::class.java)
                    intent.putExtra("listResultKey", jsonString)
                    viewHolder.itemView.context.startActivity(intent)
                    viewHolder.itemView.isClickable = true
                }
            }
        })

    }

    class FlagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ipadres: TextView = itemView.findViewById(R.id.ipTextView)

        fun bind(ip : Ip) {
            ipadres.text = ip.ipaddress
        }
    }
}