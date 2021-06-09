package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AddFriend extends AppCompatActivity {

    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Toast.makeText(AddFriend.this, username,Toast.LENGTH_LONG).show();
    }
}