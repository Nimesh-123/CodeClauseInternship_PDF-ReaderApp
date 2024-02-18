package com.example.codeclauseinternship.pdfreader

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.codeclauseinternship.pdfreader.Adapter.QuestionTabAdapter
import com.example.codeclauseinternship.pdfreader.Fragment.BookMarkFragment
import com.example.codeclauseinternship.pdfreader.Fragment.DocumentFragment
import com.example.codeclauseinternship.pdfreader.Fragment.RecentFragment
import com.example.codeclauseinternship.pdfreader.databinding.ActivityMainBinding
import com.google.android.material.card.MaterialCardView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var fragments = arrayOf(DocumentFragment(), BookMarkFragment(), RecentFragment())

    var permission = arrayOf(
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE"
    )
    var isDeleted = true
    var isdialog = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    private fun init() {

        val questionTabAdapter = QuestionTabAdapter(supportFragmentManager, fragments)
        binding.viewpager.adapter = questionTabAdapter

        binding.llHome.setOnClickListener {
            binding.viewpager.currentItem = 0
        }
        binding.llBookMark.setOnClickListener {
            binding.viewpager.currentItem = 1
        }
        binding.llHistory.setOnClickListener {
            binding.viewpager.currentItem = 2
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
            init()
        }

    }


    override fun onBackPressed() {

        if (binding.viewpager.currentItem == 0) {
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
        } else {
            binding.viewpager.currentItem = binding.viewpager.currentItem - 1
        }
    }
}