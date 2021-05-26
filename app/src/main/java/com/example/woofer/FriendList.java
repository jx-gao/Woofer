package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class FriendList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        System.out.println();
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        TextView output = (TextView) findViewById(R.id.textViewOutput);
        output.setText(username);
    }
}