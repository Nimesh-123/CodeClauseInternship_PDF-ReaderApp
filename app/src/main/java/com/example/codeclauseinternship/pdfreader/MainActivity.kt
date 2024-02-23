package com.example.codeclauseinternship.pdfreader

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codeclauseinternship.pdfreader.Adapter.AllPdfAdapter
import com.example.codeclauseinternship.pdfreader.DataBase.DBHelper
import com.example.codeclauseinternship.pdfreader.Interface.OnClick
import com.example.codeclauseinternship.pdfreader.Model.FileModel
import com.example.codeclauseinternship.pdfreader.Model.HistoryModel
import com.example.codeclauseinternship.pdfreader.databinding.ActivityMainBinding
import com.google.android.material.card.MaterialCardView
import java.io.File
import java.text.SimpleDateFormat
import kotlin.math.pow


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    var permission = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )
    var isDeleted = true
    var isdialog = false

    private var allPdfAdapter: AllPdfAdapter? = null

    private var fileModelArrayList: ArrayList<File> = ArrayList()
    private var list: ArrayList<FileModel> = ArrayList()
    private var dbHelper: DBHelper? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun init() {
        dbHelper = DBHelper(this)
        pdfLoader().execute()

        binding.ivFav.setOnClickListener {
            val intent = Intent(applicationContext, FavActivity::class.java)
            startActivity(intent)
        }

        binding.ivHistory.setOnClickListener {
            val intent = Intent(applicationContext, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    fun permissionDialog() {
        this.isdialog = true
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_permission)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.setCancelable(false)
        (dialog.findViewById<View>(R.id.llAllow) as MaterialCardView).setOnClickListener {
            this@MainActivity.isdialog = false
            dialog.dismiss()
            grantPermission()
        }
        dialog.show()
    }

    fun hasPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= 30) {
            Environment.isExternalStorageManager()
        } else ContextCompat.checkSelfPermission(
            applicationContext,
            "android.permission.READ_EXTERNAL_STORAGE"
        ) == 0 && ContextCompat.checkSelfPermission(
            applicationContext, "android.permission.WRITE_EXTERNAL_STORAGE"
        ) == 0
    }

    fun grantPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            try {
                val intent = Intent("android.settings.MANAGE_APP_ALL_FILES_ACCESS_PERMISSION")
                intent.addCategory("android.intent.category.DEFAULT")
                intent.setData(
                    Uri.parse(
                        String.format(
                            "package:%s",
                            applicationContext.packageName
                        )
                    )
                )
                startActivityForResult(intent, 1111)
                return
            } catch (unused: Exception) {
                val intent2 = Intent()
                intent2.setAction("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION")
                startActivityForResult(intent2, 1111)
                return
            }
        }
        ActivityCompat.requestPermissions(this, this.permission, 30)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != -1 || Build.VERSION.SDK_INT < 30) {
            return;
        }
        if (Environment.isExternalStorageManager()) {
            Log.e("Permission Granted", "Permission Granted");
        } else {
            Log.e("Permission Denied", "Permission Denied");
        }

    }

    override fun onResume() {
        super.onResume()
        if (!hasPermission()) {
            if (Build.VERSION.SDK_INT >= 30) {
                if (!isdialog) {
                    permissionDialog()
                }
            } else {
                grantPermission()
            }
        } else if (isDeleted) {
            isDeleted = false
            Log.d("fatal", "onResume: 11111")
            init()
        }
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
                        dbHelper!!.isAlreadyAvailableFavourite(listFiles[i].path.toString()),
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
        val log = Math.log10(size.toDouble()) / Math.log10(1024.0)
        val index = log.toInt()
        val formattedSize = String.format("%.1f", size / 1024.0.pow(index))
        return "$formattedSize ${units[index]}"
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

    inner class pdfLoader : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
            binding.progress.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            val pdfFile = File(
                Environment.getExternalStorageDirectory().absolutePath
            )
            list.clear()
            getFile(pdfFile)
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            isEmpty()
            binding.progress.visibility = View.GONE
            binding.rvAllDocument.layoutManager = LinearLayoutManager(applicationContext)
            allPdfAdapter = AllPdfAdapter(applicationContext, list)
            binding.rvAllDocument.adapter = allPdfAdapter

            allPdfAdapter!!.setOnClickListener(object : OnClick {
                override fun onClick(pos: Int) {
                    val intent = Intent(applicationContext, PdfViewer::class.java)
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
                            dbHelper!!.isAlreadyAvailableFavourite(list[pos].path.toString()),
                            list[pos].size
                        )
                    dbHelper?.insertHistory(historyModel)

                }
            })

            isEmpty()
        }
    }

    override fun onBackPressed() {

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.exit_dialog)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)

        val btnNo = dialog.findViewById<TextView>(R.id.ext_btn_no)
        val btnYes = dialog.findViewById<TextView>(R.id.ext_btn_yes)

        btnNo.setOnClickListener {
            dialog.dismiss()
        }
        btnYes.setOnClickListener {
            super.onBackPressed()
            dialog.dismiss()
        }
        dialog.show()
    }
}