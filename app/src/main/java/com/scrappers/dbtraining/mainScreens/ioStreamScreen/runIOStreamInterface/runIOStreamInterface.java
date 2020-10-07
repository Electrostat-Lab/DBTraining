package com.scrappers.dbtraining.mainScreens.ioStreamScreen.runIOStreamInterface;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.scrappers.dbtraining.R;
import com.scrappers.dbtraining.mainScreens.ioStreamScreen.runIOStreamInterface.FileIO.FileIO;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class runIOStreamInterface extends AppCompatActivity {

    private FileIO fileIO;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_run_i_o_stream_interface);

        CardView animatorView=findViewById(R.id.animatorView);
        animatorView.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(),R.anim.scale_lag));


        /*
         * define an Instance of your class
         */
        fileIO=new FileIO(this,"/user","user.txt");

        Button submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * save data
                 */
                fileIO.writeData(((EditText)findViewById(R.id.ID)).getText().toString()+"\n" +
                        ((EditText)findViewById(R.id.name)).getText().toString()+"\n");
            }
        });

        Button getData=findViewById(R.id.getData);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * getData
                 */
                ((TextView)findViewById(R.id.dataFetched)).setText(fileIO.readData());
            }
        });

    }
}