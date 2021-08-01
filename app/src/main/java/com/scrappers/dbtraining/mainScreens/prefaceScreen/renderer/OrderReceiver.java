package com.scrappers.dbtraining.mainScreens.prefaceScreen.renderer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.scrappers.dbtraining.HolderActivity;

public class OrderReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent flaggedIntent = new Intent(context, HolderActivity.class);
        flaggedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(flaggedIntent);
    }
}
