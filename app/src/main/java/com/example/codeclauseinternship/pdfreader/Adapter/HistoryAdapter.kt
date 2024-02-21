package com.example.codeclauseinternship.pdfreader.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codeclauseinternship.pdfreader.Interface.OnClick
import com.example.codeclauseinternship.pdfreader.Interface.OnClickHistory
import com.example.codeclauseinternship.pdfreader.Model.FileModel
import com.example.codeclauseinternship.pdfreader.Model.HistoryModel
import com.example.codeclauseinternship.pdfreader.R


class HistoryAdapter(private var list: ArrayList<HistoryModel>, private var onClick: OnClickHistory) :
    RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = list[position]

        holder.tvName.text = ItemsViewModel.filename
        holder.tvDate.text = ItemsViewModel.date
        holder.tvSize.text = ItemsViewModel.size

        holder.itemView.setOnClickListener {
            onClick.onClick(position)
        }

        holder.ivDelete.setOnClickListener {
            onClick.onClickDelete(position)
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivDelete: ImageView = itemView.findViewById(R.id.ivDelete)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
    }

}