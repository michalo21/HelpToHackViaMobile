package com.example.helptohackviamobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.InetSocketAddress
import java.net.Socket


class ResultAdapter(private val resultlist: List<Result>) : RecyclerView.Adapter<ResultAdapter.FlagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlagViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.result_layout, parent,false)
        return FlagViewHolder(view)
    }

    override fun getItemCount() = resultlist.size

    override fun onBindViewHolder(viewHolder: FlagViewHolder, position: Int) {
        viewHolder.bind(resultlist[position])

    }

    class FlagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val resultService: TextView = itemView.findViewById(R.id.serviceText)
        private val resultPort: TextView = itemView.findViewById(R.id.portText)
        private val resultStatus: TextView = itemView.findViewById(R.id.statusText)


        fun bind(result : Result) {
           resultService.text = result.service
           resultPort.text = result.port
           resultStatus.text = result.status
        }
    }
}