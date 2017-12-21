package com.example.maxterminatorx.ramilevyhelpapp;

/**
 * Created by maxterminatorx on 07-Sep-17.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;


public class NetworkConnector {

    private String url;
    private boolean sended;
    private boolean success;

    public NetworkConnector(String url){
        this.url=url;
        this.sended = false;
        this.success = false;
    }

    public void httpPost(HashMap<String,String> params){



    }

    public void httpGet(HashMap<String,String> params){
        try {

            sended = true;

            String qString = "";

            Set<String> st = params.keySet();

            for(String key:st) {

                String value = URLEncoder.encode(params.get(key),"UTF-8");
                qString += key + '=' + value + '&';

            }

            URL url = new URL(this.url+'?'+qString);
            HttpsURLConnection httpsRequest = (HttpsURLConnection)url.openConnection();
            httpsRequest.connect();

            Log.i("maxim",httpsRequest.getResponseMessage());

            success = true;

        }catch(IOException ioex){
            Log.i("maxim",ioex.toString());
            success = false;
        }


    }

    public boolean isSuccess(){
        return success;
    }

    public boolean isSended(){
        return sended;
    }

}
