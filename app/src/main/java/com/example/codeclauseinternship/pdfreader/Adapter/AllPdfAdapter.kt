package com.example.codeclauseinternship.pdfreader.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codeclauseinternship.pdfreader.Model.FileModel
import com.example.codeclauseinternship.pdfreader.R
import kotlin.collections.ArrayList

class AllPdfAdapter(private val list: ArrayList<FileModel>) :
    RecyclerView.Adapter<AllPdfAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = list[position]
        Log.d("fatal", "onBindViewHolder: " + ItemsViewModel.filename)

//        holder.ivImage.setImageResource(ItemsViewModel.image)
        holder.tvName.text = ItemsViewModel.filename
        holder.tvDate.text = ItemsViewModel.date
        holder.tvSize.text = ItemsViewModel.size

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)
        val ivMore: ImageView = itemView.findViewById(R.id.ivMore)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
    }
}