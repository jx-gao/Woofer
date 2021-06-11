package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Status extends AppCompatActivity {
    String username;
    LinearLayout statusView ;
    EditText newStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        TextView usernameLbl = (TextView) findViewById(R.id.textViewUsername);
        usernameLbl.setText(username);
        statusView = (LinearLayout) findViewById(R.id.statusView);
        newStatus = (EditText)findViewById(R.id.messageEdit);

        doGetStatuses();
    }

    private void doGetStatuses() {
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

    private void displayContent(String author, String content, String date){
        LinearLayout status = new LinearLayout(this);
        status.setOrientation(LinearLayout.HORIZONTAL);
        TextView authorView = new TextView(this);
        TextView contentView = new TextView(this);
        TextView dateView = new TextView(this);
        authorView.setText(author);
        contentView.setText(content);
        dateView.setText(date);
        status.addView(authorView);
        status.addView(contentView);
        status.addView(dateView);
        statusView.addView(status);
    }



    private void displayStatuses(String myResponse) {
        try {
            String out = "";
            JSONArray jr = new JSONArray(myResponse);

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb = (JSONObject) jr.get(i);
                String author = jb.getString("USERNAME");
                String content = jb.getString("CONTENT");
                String date = jb.getString("DATE");
                displayContent(author,content,date);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//Post status
    public void createNewStatus(){
        EditText newStatus = (EditText)findViewById(R.id.messageEdit);

        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("content", newStatus.getText().toString());

        PHPRequest request = new PHPRequest();
        request.doRequest(Status.this, "poststatus", cv, new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
                proccessOutcome(response);
            }
        });
    }

    public boolean checkFieldsEmpty(){
        if(newStatus.getText().toString().equals("")){return true;}
        return false;
    }

    public void proccessOutcome(String response){
        if(response.equals("New record created successfully")){
            Toast.makeText(Status.this, "Successfully posted status!", Toast.LENGTH_LONG).show();
            return;
        }else if(response.equals("Error")){
            Toast.makeText(Status.this, "Error posting status", Toast.LENGTH_LONG).show();
            return;
        }
        //when error occurs in signup php
        Toast.makeText(Status.this, "Error trying to posting status, try again later", Toast.LENGTH_LONG).show();
    }

    public void doStatus(View view) {
        if(checkFieldsEmpty()){
            Toast.makeText(Status.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
            return;
        }

        createNewStatus();
    }
}