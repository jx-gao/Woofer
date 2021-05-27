package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class FriendList extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        System.out.println();
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        doGetFriends();
    }

    public void doGetFriends(){

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("username", username)
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s1601812/friendlist.php")
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
                    FriendList.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(myResponse.equals("[]")){
                                displayFail();
                            }else{
                                displayFriends(myResponse);
                            }
                        }
                    });
                }
            }
        });
    }


    private void processJSON(String json){
        if(json.equals("[]")){
            displayFail();
        }else{
            displayFriends(json);
        }

    }

    private void displayFriends(String myResponse){
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainView);
        try {
            String out="";
            JSONArray jr = new JSONArray(myResponse);

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb = (JSONObject) jr.get(i);
                String id = jb.getString("Username");
                Button friend = new Button(this);

                friend.setText(id);
                layout.addView(friend);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void displayFail(){
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainView);
        TextView output = new TextView(this);
        output.setText("You have no friends lmao");
        layout.addView(output);
    }
}