package com.esraa.egfwd.loadapp

import android.R
import android.app.DownloadManager
import android.app.NotificationManager
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import com.esraa.egfwd.loadapp.databinding.ActivityDetailBinding


class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        Log.i("DetailActivity", "THIS ID: $id")

        //get an instance of NotificationManager and call sendNotification
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancelAll()

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val query = DownloadManager.Query()
            .setFilterById(id?:0)
        val cursor = downloadManager.query(query)
        setStatusText(cursor)

        binding.okButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }


    private fun setStatusText(cursor: Cursor) {
        var status = "no status"
        var fileName = "no title"
        val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
        val titleColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)

        if (cursor.moveToFirst()) {
            when (cursor.getInt(statusColumnIndex)) {

                DownloadManager.STATUS_SUCCESSFUL -> {
                    status = "Sucess"
                    binding.detailContent.downloadStatus.setTextColor(Color.GREEN)
                    fileName = cursor.getString(titleColumnIndex)
                }
                DownloadManager.STATUS_FAILED -> {
                    status = "Fail"
                    binding.detailContent.downloadStatus.setTextColor(Color.RED)
                    fileName = cursor.getString(titleColumnIndex)
                }
            }
        }

        binding.detailContent.downloadStatus.text = status
        binding.detailContent.fileName.text = fileName

    }
}