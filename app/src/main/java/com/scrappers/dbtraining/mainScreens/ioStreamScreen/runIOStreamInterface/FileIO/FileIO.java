package com.scrappers.dbtraining.mainScreens.ioStreamScreen.runIOStreamInterface.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @author twisted Metal AKA Pavly G.
 */
public class FileIO {

    private final AppCompatActivity context;
    private String fileName;
    private String dir;

    /**
     * Read & Write data using Java File IO
     * @param context activity context
     * @param dir directory to which data is saved to & read from
     * @param fileName the name of the terminal file in that location
     */
    public FileIO(AppCompatActivity context,String dir,String fileName){
        this.context=context;
        this.dir=dir;
        this.fileName=fileName;
        this.mkDirs();
    }

    /**
     * mkdirs(); create a new Dir if it doesn't exist
     */
    private void mkDirs(){
        System.out.println(new File(context.getFilesDir()+"/"+dir).mkdirs());
    }

    /**
     * writes data to that file
     * @param data data to write in String
     */
    public void writeData(String data){
        try(FileOutputStream fos=new FileOutputStream(new File(context.getFilesDir()+"/"+dir,fileName))){
            fos.write(data.getBytes());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Reads data
     * @return data in String
     */
    public String readData(){
        StringBuilder resultString= new StringBuilder();
        try(FileInputStream fis=new FileInputStream(new File(context.getFilesDir()+"/"+dir,fileName))){
            int size=fis.available();
            for(int charPosition=0; charPosition<size; charPosition++){
                resultString.append((char) fis.read());
            }
            return resultString.toString();
        }catch (IOException e){
            e.printStackTrace();
            return "";
        }
    }
}
