package com.example.woofer;

import android.app.Activity;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PHPRequest {

    public void doRequest(Activity a, String method, String username, RequestHandler rh) {
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s1601812/"+method+".php")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rh.proccessResponse(myResponse);
                        }
                    });
                }
            }
        });
    }

    public void doInsertRequest(Activity a, String method, ArrayList<String> keys, ArrayList<String> values, RequestHandler rh) {

        OkHttpClient client = new OkHttpClient();
        
        String body = "?";
        for(int i = 0; i < keys.size(); i++){
            body += keys.get(i) + "=" + values.get(i) + "&";
        }
        body = body.substring(0, body.length()-1);

        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s1601812/"+method+".php"+body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    a.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            rh.proccessResponse(myResponse);
                        }
                    });
                }
            }
        });
    }
}
