package com.scrappers.dbtraining.mainScreens.ioBufferScreen.runIOBufferInterface.Buffers;

import android.Manifest;
import android.os.Environment;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/**
 * @author PAVLY G.
 * @see{https://github.com/Scrappers-glitch/DBTraining/tree/master}
 */
public class BuffersIO {
    private static final int WRITE_EXTERNAL_PERMISSION = 22;
    private final AppCompatActivity context;
    private String dir;
    private String fileName;

    /**
     * Creates a data writer/Reader instance to the ExternalStorage based on a file System
     * @param context the activity holder context
     * @param dir the directory to write in
     * @param fileName the terminal fileName
     */
    public BuffersIO(AppCompatActivity context, String dir, String fileName){
        this.context=context;
        this.setDir(Environment.getExternalStorageDirectory()+dir);
        this.setFileName(fileName);
        this.getPermissions();
    }

    /**
     * make the directory
     * @return true if the directory isMade successfully otherwise false
     */
    public boolean mkDirs(){
        return new File(dir).mkdirs();
    }
    /**
     * set the directory
     * @param dir directory to use
     */
    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getDir() {
        return dir;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * sets the terminal fileName
     * @param fileName file
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * get WRITE_EXTERNAL_STORAGE permission
     */
    private void getPermissions(){
        ActivityCompat.requestPermissions(context,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_PERMISSION);
    }

    /**
     * get permission ID
     * @return int
     */
    public static int getWriteExternalPermissionID() {
        return WRITE_EXTERNAL_PERMISSION;
    }

    /**
     * Writes data to the specified dir
     * @param data data to write in String
     */
    public void writeData(String data, View view){
        try(BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(new File(dir,fileName)))){
            bufferedWriter.write(data);
            Snackbar.make(view,"Data Write Established",Snackbar.LENGTH_LONG).show();
        }catch (IOException e){
            System.err.println(e.getMessage());
            Snackbar.make(view,"Data Write Failed",Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Read the data => pass it into a StringBuilder & show it up as a String
     * @return data in string
     */
    public String readData(View view){
        StringBuilder dataRead=new StringBuilder();
        try(BufferedReader bufferedReader=new BufferedReader(new FileReader(new File(dir,fileName)))){
            while (bufferedReader.ready()){
                dataRead.append(bufferedReader.readLine()).append(System.lineSeparator());
            }
            Snackbar.make(view,"Data Read Established",Snackbar.LENGTH_LONG).show();
            return String.valueOf(dataRead);
        }catch (IOException e){
            e.printStackTrace();
            Snackbar.make(view,"Data Read Failed",Snackbar.LENGTH_LONG).show();
            return "";
        }
    }
}
