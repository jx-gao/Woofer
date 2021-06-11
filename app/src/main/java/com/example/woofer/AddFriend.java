package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddFriend extends AppCompatActivity {

    private String username;
    private EditText editTextFriendUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
    }

    public void addFriend(String friendUsername){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("friend", friendUsername);

        PHPRequest request = new PHPRequest();
        request.doRequest(AddFriend.this, "addfriend", cv, new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
                Toast.makeText(AddFriend.this, response, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void doAddFriend(View view) {
        editTextFriendUsername = (EditText) findViewById(R.id.editTextFriendUsername);
        if(editTextFriendUsername.getText().toString().equals("")){
            Toast.makeText(AddFriend.this, "Enter friend username", Toast.LENGTH_LONG).show();
            return;
        }
        addFriend(editTextFriendUsername.getText().toString());
    }
}