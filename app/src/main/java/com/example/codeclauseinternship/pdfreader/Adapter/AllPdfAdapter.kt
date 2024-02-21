package com.example.codeclauseinternship.pdfreader.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.codeclauseinternship.pdfreader.DataBase.DBHelper
import com.example.codeclauseinternship.pdfreader.Interface.OnClick
import com.example.codeclauseinternship.pdfreader.Model.FavModel
import com.example.codeclauseinternship.pdfreader.Model.FileModel
import com.example.codeclauseinternship.pdfreader.R


class AllPdfAdapter(
    private var context: Context,
    private var list: ArrayList<FileModel>,
    private var onClick: OnClick
) : RecyclerView.Adapter<AllPdfAdapter.ViewHolder>() {
    private var dbHelper: DBHelper? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_document, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        dbHelper = DBHelper(context)

        val ViewModel = list[position]

        holder.tvName.text = ViewModel.filename
        holder.tvDate.text = ViewModel.date
        holder.tvSize.text = ViewModel.size

        holder.itemView.setOnClickListener {
            onClick.onClick(position)
        }
        holder.ivFav.setOnClickListener {
            if (ViewModel.isBookmarked) {
                ViewModel.isBookmarked = false
                dbHelper!!.removeOldFavDocument(list[position].path.toString())
                holder.ivFav.setImageResource(R.drawable.baseline_favorite_24)
            } else {
                val fileModel = FavModel(
                    list[position].path,
                    list[position].date,
                    list[position].filename,
                    false,
                    list[position].size
                )
                dbHelper!!.insertFav(fileModel)
                ViewModel.isBookmarked = true
                holder.ivFav.setImageResource(R.drawable.baseline_favorite_border_24)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val ivMore: ImageView = itemView.findViewById(R.id.ivMore)
        val ivFav: ImageView = itemView.findViewById(R.id.ivFav)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvSize: TextView = itemView.findViewById(R.id.tvSize)
    }

}