package com.scrappers.dbtraining.appWidgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;

import com.scrappers.dbtraining.HolderActivity;
import com.scrappers.dbtraining.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShortHandApp extends AppWidgetProvider {
    private static final int reqCode = 20222;
    private RemoteViews remoteViews;
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        if(remoteViews != null){
            appWidgetManager.updateAppWidget(new ComponentName(context, getClass()), remoteViews);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_shorthand);
            WidgetListModel[] model = new WidgetListModel[]{
                    new WidgetListModel(R.layout.menu_trigger, "Test Test Test"),
                    new WidgetListModel(R.layout.menu_trigger, "Test Test Test"),
                    new WidgetListModel(R.layout.menu_trigger, "Test Test Test"),
            };
            ArrayList<RemoteViews> remoteViewsList = new ArrayList<>();
            for(WidgetListModel m : model){
                RemoteViews remoteView = new RemoteViews(context.getPackageName(), m.getId());
                remoteViewsList.add(remoteView);
                remoteView.setOnClickPendingIntent(R.id.trigger, PendingIntent.getActivity(context, reqCode, new Intent(context, HolderActivity.class), 0));
            }
//            remoteViews.setRemoteAdapter(R.id.pager, remoteViewsList, remoteViewsList.size());
            appWidgetManager.updateAppWidget(new ComponentName(context, getClass()), remoteViews);
    }
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }
    private class WidgetListModel {
        private int id;
        private String text;
        public WidgetListModel(@LayoutRes int id, String text){
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        public String getText() {
            return text;
        }
    }
    private class Service extends RemoteViewsService{

        @Override
        public RemoteViewsFactory onGetViewFactory(Intent intent) {
            return new Factory();
        }
        private class Factory implements RemoteViewsFactory{

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

            }

            @Override
            public void onDestroy() {

            }

            @Override
            public int getCount() {
                return 0;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                return null;
            }

            @Override
            public RemoteViews getLoadingView() {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 0;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public boolean hasStableIds() {
                return false;
            }
        }

    }
}
