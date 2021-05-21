package com.scrappers.dbtraining.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import com.scrappers.dbtraining.R;

import java.time.Duration;
import java.util.concurrent.TimeUnit;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class NotificationUtils {
    public static final String CHANNEL_ID = "DB-Training";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static PeriodicWorkRequest initializeInBackground(Context context){
            //set work-request flags
            //the work-request flags are the conditions in which the background thread would be executed & upon it the notification display
            Constraints constraints = new Constraints.Builder()
                    .setRequiresBatteryNotLow(false)
                    .build();
            Data.Builder dataBuilder = new Data.Builder()
                    .putString("Notification-Test", CHANNEL_ID);
            //set the periodic request to a time, that will display notifications every day
            PeriodicWorkRequest workRequest = new PeriodicWorkRequest.Builder(BackgroundTask.class, 24, TimeUnit.HOURS)
                    .setInitialDelay(Duration.ofDays(1))
                    //the backoff criteria is used to set how you would want to retry the operation, only if the conditions aren't met(if the periodic work isn't executed by the WorkManager upon request from the WorkRequest)
                    // , whether the previous task is a success or not
                    .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS, TimeUnit.MILLISECONDS)
                    //the conditions in which the background thread would be executed & upon it the notification display
                    .setConstraints(constraints)
                    .setInputData(dataBuilder.build())
                    .addTag("Notification-Test")
                    .build();
            //register work request to the workManager to be unique, replace itself if it's still running
            WorkManager.getInstance(context)
                    .enqueueUniquePeriodicWork("Notification-Test", ExistingPeriodicWorkPolicy.REPLACE, workRequest);
        return workRequest;
    }
    public static NotificationCompat.Builder initializeNow(Context context){
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();
            Uri soundUri =
                    Uri.parse("file:///android_assets/"+"AssetsForRenderer/Audio/shocks.wav");

            if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID,  NotificationManager.IMPORTANCE_HIGH);
                channel.enableLights(true);
                channel.enableVibration(true);
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q ){
                    channel.setAllowBubbles(true);
                }
                notificationManager.createNotificationChannel(channel);
                channel.setSound(soundUri, audioAttributes);

            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            builder.setSmallIcon(R.mipmap.db_server_layer);
            builder.setAutoCancel(false);
            builder.setSound(soundUri);
        return builder;
    }
}
