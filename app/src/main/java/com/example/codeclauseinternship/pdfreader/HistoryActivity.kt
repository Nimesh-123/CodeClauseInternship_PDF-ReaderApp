package com.example.codeclauseinternship.pdfreader

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeclauseinternship.pdfreader.Adapter.HistoryAdapter
import com.example.codeclauseinternship.pdfreader.DataBase.DBHelper
import com.example.codeclauseinternship.pdfreader.Interface.OnClickHistory
import com.example.codeclauseinternship.pdfreader.Model.HistoryModel
import com.example.codeclauseinternship.pdfreader.databinding.ActivityHistoryBinding

class HistoryActivity : AppCompatActivity(), OnClickHistory {

    lateinit var binding: ActivityHistoryBinding

    private var list: ArrayList<HistoryModel> = ArrayList()

    private var dbHelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        dbHelper = DBHelper(this)
        list = dbHelper!!.getHistoryData()

        isEmpty()

        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = HistoryAdapter(list, this)

    }

    private fun isEmpty() {
        if (list.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE;
            binding.rvHistory.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvHistory.visibility = View.VISIBLE
        }
    }

    override fun onClick(pos: Int) {
        val intent = Intent(this, PdfViewer::class.java)
        intent.putExtra("fileName", list[pos].filename)
        intent.putExtra("filePath", list[pos].path)
        startActivity(intent)
    }

    override fun onClickDelete(pos: Int) {
        dbHelper?.removeOldHistory(list[pos].path.toString())
        list.clear()
        list = dbHelper!!.getHistoryData()
        binding.rvHistory.layoutManager = LinearLayoutManager(this)
        binding.rvHistory.adapter = HistoryAdapter(list, this)
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