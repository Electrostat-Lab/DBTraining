package com.scrappers.dbtraining.mainScreens.ioStreamScreen;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.contentRecyclerView.DataModel;
import com.scrappers.dbtraining.contentRecyclerView.ModelAdapter;
import com.scrappers.dbtraining.localDatabase.LocalDatabase;
import com.scrappers.dbtraining.mainScreens.ioStreamScreen.runIOStreamInterface.runIOStreamInterface;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.scrappers.dbtraining.navigation.Navigation.drawerLayout;

public class IOStreamScreen extends Fragment {

    @SuppressLint("SetJavaScriptEnabled")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_iostreams,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View viewInflater, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar=viewInflater.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        ((TextView)viewInflater.findViewById(R.id.pageTitle)).setText(Objects.requireNonNull(getContext()).getResources().getText(R.string.java_io_streams));
        RecyclerView recyclerView=viewInflater.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<DataModel> models=new ArrayList<>();
        LocalDatabase localDatabase=new LocalDatabase(getContext(),"code.json");
        try {
            JSONArray streams=localDatabase.getArray("streams");
            models.add(new DataModel("FileOutputStreams/FileInputStreams \n\n\t XML Preface",streams.getJSONObject(0).getString("preface xml"),
                    streams.getJSONObject(0).getString("code xml"),"xml",3500));

            models.add(new DataModel("READ/WRITE Class :",streams.getJSONObject(1).getString("preface java"),
                    streams.getJSONObject(1).getString("code java"),"java",3200));

            models.add(new DataModel("Create an Activity Holder Class",streams.getJSONObject(1).getString("preface java activity"),
                    streams.getJSONObject(1).getString("code java activity"),"java",2500));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ModelAdapter modelAdapter=new ModelAdapter(getContext(),models);
        recyclerView.setAdapter(modelAdapter);


        ImageButton runButton=viewInflater.findViewById(R.id.runBtn);
        runButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), runIOStreamInterface.class));
            }
        });
    }
}
