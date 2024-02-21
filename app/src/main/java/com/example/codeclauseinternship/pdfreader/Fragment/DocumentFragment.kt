package com.example.codeclauseinternship.pdfreader.Fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeclauseinternship.pdfreader.Adapter.AllPdfAdapter
import com.example.codeclauseinternship.pdfreader.DataBase.DBHelper
import com.example.codeclauseinternship.pdfreader.Interface.OnClick
import com.example.codeclauseinternship.pdfreader.Model.FileModel
import com.example.codeclauseinternship.pdfreader.Model.HistoryModel
import com.example.codeclauseinternship.pdfreader.PdfViewer
import com.example.codeclauseinternship.pdfreader.databinding.FragmentDocumentBinding
import java.io.File
import java.lang.Math.log10
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.pow


class DocumentFragment : Fragment(), OnClick {

    private lateinit var binding: FragmentDocumentBinding
    private var adapter: AllPdfAdapter? = null

    private var fileModelArrayList: ArrayList<File> = ArrayList()
    private var list: ArrayList<FileModel> = ArrayList()
    private var dbHelper: DBHelper? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDocumentBinding.inflate(layoutInflater, container, false)

        dbHelper = DBHelper(requireContext())

        val pdfFile = File(
            Environment.getExternalStorageDirectory().absolutePath
        )
        getFile(pdfFile)

        binding.rvAllDocument.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllDocument.adapter = AllPdfAdapter(requireContext(),list, this)

        isEmpty()

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    fun getFile(file: File): ArrayList<File> {
        val listFiles = file.listFiles()
        if (listFiles != null && listFiles.isNotEmpty()) {
            for (i in listFiles.indices) {
                if (listFiles[i].isDirectory) {
                    fileModelArrayList.add(listFiles[i])
                    getFile(listFiles[i])
                } else if (listFiles[i].name.endsWith(".pdf")) {
                    fileModelArrayList.add(listFiles[i])
                    val lastModified = listFiles[i].lastModified()
                    val simpleDateFormat = SimpleDateFormat("dd, MMMM yyyy  |")
                    val fileModel = FileModel(
                        listFiles[i].path,
                        simpleDateFormat.format(java.lang.Long.valueOf(lastModified)),
                        listFiles[i].name,
                        dbHelper!!.isAlreadyAvailableFavourite(listFiles[i].path),
                        readableFileSize(listFiles[i].length())
                    )
                    list.add(fileModel)

                }
            }
        }
        return fileModelArrayList
    }

    fun readableFileSize(size: Long): String {
        if (size <= 0) {
            return "0"
        }
        val units = arrayOf("B", "kB", "MB", "GB", "TB", "EB")
        val log = log10(size.toDouble()) / log10(1024.0)
        val index = log.toInt()
        val formattedSize = String.format("%.1f", size / 1024.0.pow(index))
        return "$formattedSize ${units[index]}"
    }

    override fun onResume() {
        super.onResume()
        isEmpty()
    }

    private fun isEmpty() {
        if (list.isEmpty()) {
            binding.tvNoData.visibility = View.VISIBLE;
            binding.rvAllDocument.visibility = View.GONE
        } else {
            binding.tvNoData.visibility = View.GONE
            binding.rvAllDocument.visibility = View.VISIBLE
        }
    }

    override fun onClick(pos: Int) {
        val intent = Intent(requireContext(), PdfViewer::class.java)
        intent.putExtra("fileName", list[pos].filename)
        intent.putExtra("filePath", list[pos].path)
        startActivity(intent)
        if (dbHelper?.isAlreadyAvailableHistory(list[pos].path.toString()) == true) {
            return
        }
        val historyModel =
            HistoryModel(
                list[pos].path,
                list[pos].date,
                list[pos].filename,
                false,
                list[pos].size
            )
        dbHelper?.insertHistory(historyModel)

    }
}