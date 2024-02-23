package com.example.codeclauseinternship.pdfreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeclauseinternship.pdfreader.Adapter.FavAdapter
import com.example.codeclauseinternship.pdfreader.DataBase.DBHelper
import com.example.codeclauseinternship.pdfreader.Interface.OnClickHistory
import com.example.codeclauseinternship.pdfreader.Model.FavModel
import com.example.codeclauseinternship.pdfreader.databinding.ActivityFavBinding
import com.example.codeclauseinternship.pdfreader.databinding.ActivityMainBinding

class FavActivity : AppCompatActivity(), OnClickHistory {

    private lateinit var binding: ActivityFavBinding

    private var list: ArrayList<FavModel> = ArrayList()

    private var dbHelper: DBHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        dbHelper = DBHelper(this)
        list = dbHelper!!.getFavData()

        isEmpty()

        binding.rvFav.layoutManager = LinearLayoutManager(this)
        binding.rvFav.adapter = FavAdapter(list, this)

    }

    private fun isEmpty() {
        if (list.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE;
            binding.rvFav.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvFav.visibility = View.VISIBLE
        }
    }

    override fun onClick(pos: Int) {
        val intent = Intent(this, PdfViewer::class.java)
        intent.putExtra("fileName", list[pos].filename)
        intent.putExtra("filePath", list[pos].path)
        startActivity(intent)
    }

    override fun onClickDelete(pos: Int) {
        dbHelper?.removeOldFavDocument(list[pos].path.toString())
        list.removeAt(pos)
        list = dbHelper!!.getFavData()
        binding.rvFav.layoutManager = LinearLayoutManager(this)
        binding.rvFav.adapter = FavAdapter(list, this)
        isEmpty()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        finish()
    }

}