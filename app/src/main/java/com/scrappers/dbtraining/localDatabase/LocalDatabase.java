package com.scrappers.dbtraining.localDatabase;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import androidx.annotation.NonNull;

public class LocalDatabase {
    private String file;
    private final Context context;
    public LocalDatabase(Context context, @NonNull String file){
        this.file=file;
        this.context=context;
    }
    public String loadJSONFromAsset(String filename) {
        String json;
        try (InputStream is = context.getAssets().open(filename)){
            int size = is.available();
            byte[] buffer = new byte[size];
            System.out.println(is.read(buffer));
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    public JSONArray getArray(String name){
        try {
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset(file));
            return jsonObject.getJSONArray(name);
        }catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
