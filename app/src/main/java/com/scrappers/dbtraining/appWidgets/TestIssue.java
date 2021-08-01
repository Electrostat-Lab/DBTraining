package com.scrappers.dbtraining.appWidgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.RemoteViews;

import com.scrappers.superiorExtendedEngine.menuStates.uiPager.UiPager;
@RemoteViews.RemoteView
public class TestIssue extends GridLayout {

    public TestIssue(Context context) {
        super(context);
    }

    public TestIssue(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestIssue(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TestIssue(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
