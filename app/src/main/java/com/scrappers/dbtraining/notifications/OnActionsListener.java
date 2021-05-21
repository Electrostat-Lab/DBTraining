package com.scrappers.dbtraining.notifications;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;
import com.scrappers.dbtraining.HolderActivity;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import static android.widget.Toast.LENGTH_LONG;

public class OnActionsListener extends IntentService {


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public OnActionsListener() {
        super("DB-Training-Service");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent notificationIntent) {
        assert notificationIntent != null;
        if(notificationIntent.getAction().equals(BackgroundTask.ACTION_TEST_JME)){
            //start another explicit intent implicitly from an IntentService which is directed using a PendingIntent from a notification action
            Intent intent = new Intent(getApplication(), HolderActivity.class);
            //start an activity from outside the context flag
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.deleteNotificationChannel(NotificationUtils.CHANNEL_ID);
            notificationManager.cancelAll();
            Toast.makeText(getApplicationContext(), "Powered By Jme", LENGTH_LONG).show();
        }else if(notificationIntent.getAction().equals(BackgroundTask.ACTION_DISMISS)){
            NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.deleteNotificationChannel(NotificationUtils.CHANNEL_ID);
            notificationManager.cancelAll();

        }
    }
}
