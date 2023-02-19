package com.esraa.egfwd.loadapp

import android.app.DownloadManager
import android.app.NotificationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val id = intent?.getIntExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

        Log.i("DetailActivity", "THIS ID: $id")

        //get an instance of NotificationManager and call sendNotification
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.cancel(id?:0)
    }
}