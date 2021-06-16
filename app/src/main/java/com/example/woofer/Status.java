package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
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
        statusView = findViewById(R.id.friendListView);
        newStatus = findViewById(R.id.messageEdit);

        doGetStatuses();
    }

    private void doGetStatuses() {  //get status json string from db
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        PHPRequest request = new PHPRequest();
        request.doRequest(Status.this, "status", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                displayStatuses(response);
            }
        });
    }

    private void displayContent(String author, String content, String date){ //create status layout from component xml and set values
        ConstraintLayout statusWidget = (ConstraintLayout) LayoutInflater.from(Status.this).inflate(R.layout.component_status, null);
        TextView auth = statusWidget.findViewById(R.id.friendName) ;
        TextView cont = statusWidget.findViewById(R.id.statusPostContent);
        TextView time = statusWidget.findViewById(R.id.friendInfo);
        auth.setText(author);
        cont.setText(content);
        time.setText("at "+date);
        statusView.addView(statusWidget);

    }



    private void displayStatuses(String myResponse) { //for each status in the json string, create and add a status layout
        try {
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
    private void createNewStatus(){ //get content from the message field and send http request to PHP API to insert into db
        EditText newStatus = findViewById(R.id.messageEdit);

        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("content", newStatus.getText().toString());

        PHPRequest request = new PHPRequest();
        newStatus.setText("");
        request.doRequest(Status.this, "poststatus", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                Toast.makeText(Status.this, response, Toast.LENGTH_LONG).show();
                statusView.removeAllViews();
                doGetStatuses();
            }
        });
    }

    public void doShowFriend(View view){ //open friendlist screen
        Intent intent = new Intent(getApplicationContext(), FriendList.class);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    public void doRefreshStatus(View view){ //clear statuses on screen and reload the layout
        statusView.removeAllViews();
        doGetStatuses();
    }

    private boolean checkFieldsEmpty(){
        return newStatus.getText().toString().equals("");
    }


    public void doStatus(View view) { //checks if the message field is empty and displays message
        if(checkFieldsEmpty()){
            Toast.makeText(Status.this, "Status content is empty", Toast.LENGTH_LONG).show();
            return;
        }

        createNewStatus();
    }
}