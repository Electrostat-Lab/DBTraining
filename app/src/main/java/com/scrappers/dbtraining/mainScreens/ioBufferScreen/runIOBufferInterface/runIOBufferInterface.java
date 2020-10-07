package com.scrappers.dbtraining.mainScreens.ioBufferScreen.runIOBufferInterface;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.mainScreens.ioBufferScreen.runIOBufferInterface.Buffers.BuffersIO;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class runIOBufferInterface extends AppCompatActivity {
    private BuffersIO buffersIO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_i_o_buffer_interface);

        CardView animatorView=findViewById(R.id.animatorView);
        animatorView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_lag));
        /*
         * Define an Instance
         */
        buffersIO=new BuffersIO(runIOBufferInterface.this,"/DB CodeCamp/","codeCamp.dll");
        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
               buffersIO.writeData(((EditText) findViewById(R.id.ID)).getText() + System.lineSeparator()+
                                    ((EditText) findViewById(R.id.name)).getText().toString() + System.lineSeparator(),view);
                ((TextView)findViewById(R.id.goCheck)).setText("GO Check " + new File(buffersIO.getDir(), buffersIO.getFileName()).toString());
            }
        });
        findViewById(R.id.getData).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView)findViewById(R.id.dataFetched)).setText(buffersIO.readData(v));
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode== BuffersIO.getWriteExternalPermissionID()){
            System.out.println(buffersIO.mkDirs());
        }
    }
}