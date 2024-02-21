package com.example.codeclauseinternship.pdfreader.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeclauseinternship.pdfreader.Adapter.HistoryAdapter
import com.example.codeclauseinternship.pdfreader.DataBase.DBHelper
import com.example.codeclauseinternship.pdfreader.Interface.OnClickHistory
import com.example.codeclauseinternship.pdfreader.Model.HistoryModel
import com.example.codeclauseinternship.pdfreader.PdfViewer
import com.example.codeclauseinternship.pdfreader.databinding.FragmentRecentBinding

class RecentFragment : Fragment() , OnClickHistory {

    lateinit var binding: FragmentRecentBinding

    private var list: ArrayList<HistoryModel> = ArrayList()

    private var dbHelper: DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRecentBinding.inflate(layoutInflater,container,false)


        dbHelper = DBHelper(requireContext())
        list = dbHelper!!.getHistoryData()

        isEmpty()

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = HistoryAdapter(list, this)

        return binding.root
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
        val intent = Intent(requireContext(), PdfViewer::class.java)
        intent.putExtra("fileName", list[pos].filename)
        intent.putExtra("filePath", list[pos].path)
        startActivity(intent)
    }

    override fun onClickDelete(pos: Int) {
        dbHelper?.removeOldHistory(list[pos].path.toString())
        list.clear()
        list = dbHelper!!.getHistoryData()
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = HistoryAdapter(list, this)
        isEmpty()
    }

}