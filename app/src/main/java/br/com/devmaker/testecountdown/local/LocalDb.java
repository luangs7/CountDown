package br.com.devmaker.testecountdown.local;

import android.content.Context;

import com.google.gson.Gson;


/**
 * Created by Ricardo on 6/7/14.
 */
public class LocalDb {
    private Gson gson;
    private Context context;

    public LocalDb(Context context) {
        gson = new Gson();
        this.context = context;
    }



}
