package com.example.codeclauseinternship.pdfreader.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeclauseinternship.pdfreader.Adapter.AllPdfAdapter
import com.example.codeclauseinternship.pdfreader.Model.FileModel
import com.example.codeclauseinternship.pdfreader.databinding.FragmentDocumentBinding
import java.io.File
import java.lang.Math.log10
import java.text.SimpleDateFormat
import kotlin.math.pow


class DocumentFragment : Fragment() {

    private lateinit var binding: FragmentDocumentBinding

    private var fileModelArrayList: ArrayList<File> = ArrayList()
    private var list: ArrayList<FileModel> = ArrayList()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentDocumentBinding.inflate(layoutInflater, container, false)

        getFile(File(Environment.getExternalStorageDirectory().absolutePath))

        isEmpty()

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    fun getFile(file: File): ArrayList<File> {
        val listFiles = file.listFiles()
        Log.d("fatal", "getFile: " + file.listFiles())
        if (listFiles != null && listFiles.isNotEmpty()) {
            Log.d("fatal", "getFile: 000000")
            for (i in listFiles.indices) {
                Log.d("fatal", "getFile: 11111")
                if (listFiles[i].isDirectory) {
                    Log.d("fatal", "getFile: 22222")
                    fileModelArrayList.add(listFiles[i])
                    getFile(listFiles[i])
                } else if (listFiles[i].name.endsWith(".pdf")) {
                    Log.d("fatal", "getFile: 333333")
                    fileModelArrayList.add(listFiles[i])
                    val lastModified = listFiles[i].lastModified()
                    val simpleDateFormat = SimpleDateFormat("hh:mm a  |  dd, MMMM yyyy")
                    Log.d("fatal", "getFile: " + listFiles[i].name)
                    val fileModel = FileModel(
                        listFiles[i].path,
                        simpleDateFormat.format(java.lang.Long.valueOf(lastModified)),
                        listFiles[i].name,
                        false,
                        readableFileSize(listFiles[i].length())
                    )
                    list.add(fileModel)
                    Log.d("fatal", "getFile: " + list.size)
                    binding.rvAllDocument.layoutManager = LinearLayoutManager(requireContext())
                    binding.rvAllDocument.adapter = AllPdfAdapter(list)
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
}