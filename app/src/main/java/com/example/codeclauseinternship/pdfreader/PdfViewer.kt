package com.example.codeclauseinternship.pdfreader

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.codeclauseinternship.pdfreader.databinding.ActivityPdfViewerBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import java.io.File

class PdfViewer : AppCompatActivity() {

    private lateinit var binding: ActivityPdfViewerBinding

    private var pageNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val fileName: String = intent.getStringExtra("fileName").toString()
        val filePath: String = intent.getStringExtra("filePath").toString()

        binding.toolbarText.text = fileName

        binding.idPDFView.fromFile(File(filePath)).enableSwipe(true).swipeHorizontal(false)
            .enableAnnotationRendering(true).onLoad { }.scrollHandle(DefaultScrollHandle(this))
            .onPageError { _, _ -> }
            .onPageChange { i, i2 ->
                pageNumber = i
                title = String.format(
                    "%s %s / %s",
                    File(filePath),
                    Integer.valueOf(i + 1),
                    Integer.valueOf(i2)
                )
            }.defaultPage(pageNumber).load()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        finish()
    }
}