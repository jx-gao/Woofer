package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.sql.Date;
public class Status extends AppCompatActivity {
    String username;
    LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        layout = (LinearLayout)findViewById(R.id.statusView);


        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        doGetStatuses();
    }

    private void doGetStatuses(){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        PHPRequest request = new PHPRequest();
        request.doRequest(Status.this, "status", cv, new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
                displayStatuses(response);
            }
        });
    }

    private void displayStatuses(String myResponse){
        try {
            String out="";
            JSONArray jr = new JSONArray(myResponse);

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb = (JSONObject) jr.get(i);
                String username = jb.getString("USERNAME");
                String content = jb.getString("CONTENT");
                String date = jb.getString("DATE");
                Button friend = new Button(this);
                friend.setText(content);
                layout.addView(friend);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}