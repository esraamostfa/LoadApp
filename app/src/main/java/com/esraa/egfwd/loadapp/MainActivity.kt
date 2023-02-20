package com.esraa.egfwd.loadapp

import android.animation.ObjectAnimator
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.core.content.ContextCompat
import com.esraa.egfwd.loadapp.databinding.ActivityMainBinding
import com.esraa.egfwd.loadapp.utils.sendNotification
import kotlin.math.abs


class MainActivity : AppCompatActivity() {

     lateinit var binding: ActivityMainBinding

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager

    private lateinit var downloadManager: DownloadManager

    private  var downloading = false


    private var  checkedRadioButton : RadioButton? = null
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
            notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.sendNotification(context.getString(R.string.notification_description), context, id?:0, downloadID, DownloadManager.EXTRA_DOWNLOAD_ID)

            binding.contentMain.customButton.setButtonStatus(ButtonState.COMPLETED)
            downloading = false
        }
    }

    private fun download() {

        val request =
            DownloadManager.Request(Uri.parse(getUrl()))
                .setTitle(checkedRadioButton?.text)
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

         downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request) // enqueue puts the download request in the queue.

        binding.contentMain.customButton.setButtonStatus(ButtonState.LOADING)
        downloading = true

        getDownloadProgress()
    }

    private fun getDownloadProgress() {

        var totalBytes = 0

        Thread {

            while (downloading) {

                val query = DownloadManager.Query()
                    .setFilterById(downloadID)
                val cursor = downloadManager.query(query)
                cursor.moveToFirst()

                val statusColumnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                val bytesDownloadedColumnIndex =
                    cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                val totalBytesColumnIndex =
                    cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)


                val downloadedBytes = cursor.getInt(bytesDownloadedColumnIndex)
                 if(totalBytes<=0) {
                     totalBytes = cursor.getInt(totalBytesColumnIndex)
                 }

                val progress = abs((downloadedBytes.toFloat() / totalBytes))
                Log.i("LoadingDownload", totalBytes.toString())

                runOnUiThread {
                     animateLoading(progress)
                    animateProgressWidth(progress)
                }

                if (cursor.getInt(statusColumnIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                    downloading = false
                     }
                cursor.close()
            }
             }.start()

    }


    private fun getUrl() : String {
        var url = ""

        val radioGroup = binding.contentMain.radioGroup
        val checkedRBId = radioGroup.checkedRadioButtonId
         checkedRadioButton= radioGroup.findViewById(checkedRBId)
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

    private fun animateLoading(angle: Float) {
        val loadingView = binding.contentMain.customButton
        val  animator = ObjectAnimator.ofFloat(loadingView, "loadingAngle", 0f, angle*360f)
        animator.duration = 3000
        animator.start()
    }
    private fun animateProgressWidth(progress: Float) {
        val loadingView = binding.contentMain.customButton
        val  animator = ObjectAnimator.ofFloat(loadingView, "progressWidth", 0f, progress)
        animator.duration = 3000
        animator.start()
    }
}