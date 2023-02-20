package com.esraa.egfwd.loadapp.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.core.app.NotificationCompat
import com.esraa.egfwd.loadapp.DetailActivity
import com.esraa.egfwd.loadapp.MainActivity
import com.esraa.egfwd.loadapp.R


private const val REQUEST_CODE = 0

fun NotificationManager.sendNotification(messageBody: String, applicationContext: Context, id: Int, downloadId: Long, downloadIdExtra: String) {
    // Create the content intent for the notification, which launches
    // this activity
    val contentIntent = Intent(applicationContext, MainActivity::class.java)

    //create PendingIntent to open app from notification
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        id,
        contentIntent,
        FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
    )

    //add style
    //load image source
    val eggImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.ic_launcher_foreground
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(eggImage)
        .bigLargeIcon(null)


    //add action
    val changesIntent = Intent(applicationContext, DetailActivity::class.java)
    changesIntent.putExtra(downloadIdExtra, downloadId)
    val changesPendingIntent: PendingIntent = PendingIntent.getActivity(
        applicationContext,
        REQUEST_CODE,
        changesIntent,
        FLAG_IMMUTABLE or FLAG_ONE_SHOT
    )


     val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )

        //set title, text and icon to builder
        .setSmallIcon(R.drawable.ic_launcher_foreground)
        .setContentTitle(applicationContext
            .getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        //add style
        .setStyle(bigPicStyle)
        .setLargeIcon(eggImage)
        //add action
        .addAction(
            R.drawable.ic_launcher_foreground,
            applicationContext.getString(R.string.notification_button),
            changesPendingIntent
        )
        //To support priority on devices running API level 25 or lower
        .setPriority(NotificationCompat.PRIORITY_HIGH)


    // Deliver the notification

    //id represents the current notification instance
    // and is needed for updating or canceling this notification.
    // Since this app will only have one active notification at a given time,
    // we can use the same id for all your notifications.
    notify(id, builder.build())

}
