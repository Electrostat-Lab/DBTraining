package com.scrappers.dbtraining.notifications;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.service.notification.StatusBarNotification;
import com.google.common.util.concurrent.ListenableFuture;
import com.scrappers.dbtraining.R;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.futures.SettableFuture;

public class BackgroundTask extends ListenableWorker {
    public static final String ACTION_TEST_JME = "ActionTestJme";
    public static final String ACTION_DISMISS = "ActionDismiss";
    public static final int NOTIFICATION_REQUEST = 209;
    private final WorkerParameters workerParameters;
    private final List<Result> results = new ArrayList<>();
    /**
     * @param appContext   The application {@link Context}
     * @param workerParams Parameters to setup the internal state of this worker
     */
    public BackgroundTask(@NonNull Context appContext, @NonNull WorkerParameters workerParams) {
        super(appContext, workerParams);
        this.workerParameters = workerParams;
    }

    @NonNull
    @Override
    @SuppressLint("RestrictedApi")
    public ListenableFuture<Result> startWork() {
       final SettableFuture<Result> finalResult = SettableFuture.create();
       synchronized(this) {
           try {
               return Executors.newSingleThreadExecutor().submit(() -> {
                   Notification builder = NotificationUtils.initializeNow(getApplicationContext())
                        // .setLargeIcon(Bitmap.createBitmap())
                           .setSmallIcon(R.mipmap.db_server_layer)
                           .setContentTitle("Time to test jme3-beta")
                           .setContentText("Hi, we are happy you are here to test our engine beta version.....")
                           .addAction(R.drawable.ic_baseline_videogame_asset_24, "Test Jme3-Beta", createPendingIntent(ACTION_TEST_JME, NOTIFICATION_REQUEST, OnActionsListener.class))
                           .addAction(R.drawable.ic_baseline_close_24, "Dismiss", createPendingIntent(ACTION_DISMISS, NOTIFICATION_REQUEST, OnActionsListener.class))
                           .setAutoCancel(true)
                           .setPriority(NotificationCompat.PRIORITY_MAX)
                           .setCategory(NotificationCompat.CATEGORY_PROMO)
                           .setChannelId(NotificationUtils.CHANNEL_ID)
                           .build();
                   //notify using the NotificationManager system service binding to the packageManager
                   NotificationManager notificationManager =
                           ((NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE));
                   notificationManager.notify(NOTIFICATION_REQUEST, builder);

                   //loop over status bar notifications
                   for (StatusBarNotification notification : notificationManager.getActiveNotifications()) {
                       if ( notification.getPackageName().equals(getApplicationContext().getPackageName()) ){
                           //add result success & break it off
                           results.add(Result.success());
                           break;
                       } else {
                           //retry if the notification is still not there
                           results.add(Result.retry());
                       }
                   }
                   if ( results.contains(Result.success()) ){
                       finalResult.set(Result.success());
                   } else {
                       finalResult.set(Result.retry());
                   }
               }, finalResult).get();
           } catch (Exception e) {
               e.printStackTrace();
               finalResult.set(Result.failure());
               return finalResult;
           }
       }
    }

    /**
     * Create a launch-able pending intent
     * @param action action name
     * @param requestCode req code name
     * @param clazz the foreground task class, ie Service Class that uses Intents (intended mapped actions)
     * @return a pending intent instance
     */
    private PendingIntent createPendingIntent(String action, int requestCode, Class<? extends Service> clazz){
        Intent actionState = new Intent(getApplicationContext(), clazz);
        actionState.setAction(action);
        return PendingIntent.getService(getApplicationContext(),
                requestCode, actionState, PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
