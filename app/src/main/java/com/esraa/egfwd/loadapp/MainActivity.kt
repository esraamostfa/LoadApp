package com.esraa.egfwd.loadapp

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.esraa.egfwd.loadapp.databinding.ActivityMainBinding
import com.esraa.egfwd.loadapp.utils.sendNotification


class MainActivity : AppCompatActivity() {

     lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createChannel(
            getString(R.string.notification_channel_id),
            getString(R.string.notification_channel_id)
        )

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentMain.customButton
            .setOnClickListener {
                if(getUrl()==""){
                    Toast.makeText(this, resources.getString(R.string.download_toast), Toast.LENGTH_LONG)
                        .show()
                } else {
                    download()
                }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val id = intent?.getIntExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            //get an instance of NotificationManager and call sendNotification
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.sendNotification(context.getString(R.string.notification_description), context, id?:0)

        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(getUrl()))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    private fun getUrl() : String {
        var url = ""

        val radioGroup = binding.contentMain.radioGroup
        val checkedRBId = radioGroup.checkedRadioButtonId
        val checkedRadioButton : RadioButton? = radioGroup.findViewById(checkedRBId)
        when(checkedRadioButton?.text) {
            resources.getString(R.string.glide_radio_button) -> url = resources.getString(R.string.glide_radio_button_url)
            resources.getString(R.string.loadApp_radio_button) -> url = resources.getString(R.string.loadApp_radio_button_url)
            resources.getString(R.string.retrofit_radio_button) -> url = resources.getString(R.string.retrofit_radio_button_url)
        }

        return url
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(channelId: String, channelName: String) {
        val notificationChannel = NotificationChannel(
            channelId,
            channelName,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            setShowBadge(false)
        }

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "Time for breakfast"

        val notificationManager = this.getSystemService(
            NotificationManager::class.java
        )
        notificationManager.createNotificationChannel(notificationChannel)


    }

}