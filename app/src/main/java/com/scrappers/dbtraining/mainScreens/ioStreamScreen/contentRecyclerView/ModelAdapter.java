package com.scrappers.dbtraining.mainScreens.ioStreamScreen.contentRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.mainScreens.ioStreamScreen.javaScriptInterface.CodeEditorInterface;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ModelAdapter extends RecyclerView.Adapter<AdapterHolder> {
    private final Context context;
    private ArrayList<DataModel> models;
    public ModelAdapter(Context context, ArrayList<DataModel> models) {
        this.context=context;
        this.models=models;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public AdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdapterHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.datamodel_iostream,parent,false));
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onBindViewHolder(@NonNull AdapterHolder holder, int position) {
        holder.title.setText(models.get(position).getTitle());
        holder.illustrations.setText(models.get(position).getIllustrations());

        WebSettings webSettings=holder.webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(true);
        holder.editor.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,models.get(position).getEditorHeight()));
        holder.webView.loadUrl("file:///android_asset/code Editor.html");

        CodeEditorInterface codeEditorInterface=new CodeEditorInterface(context);
        codeEditorInterface.setCode(models.get(position).getCode());
        codeEditorInterface.setMode(models.get(position).getMode());
        holder.webView.addJavascriptInterface(codeEditorInterface,"CodeEditor");

    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
