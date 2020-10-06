package com.scrappers.dbtraining.mainScreens.ioStreamScreen.contentRecyclerView;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.scrappers.dbtraining.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterHolder extends RecyclerView.ViewHolder {
    public TextView title;
    public TextView illustrations;
    public WebView webView;
    public FrameLayout editor;

    @SuppressLint("SetJavaScriptEnabled")
    public AdapterHolder(@NonNull View itemView) {
        super(itemView);
        webView=itemView.findViewById(R.id.web);
        title =itemView.findViewById(R.id.title);
        illustrations=itemView.findViewById(R.id.data);
        editor=itemView.findViewById(R.id.editor);



    }
}
