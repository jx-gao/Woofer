package com.example.woofer;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddFriend extends AppCompatActivity {

    private String username;
    LinearLayout fofContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        fofContainer = findViewById(R.id.fofView);

        doGetFOF();
    }

    private void doGetFOF() {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        PHPRequest request = new PHPRequest();
        request.doRequest(AddFriend.this, "fof", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                displayFOF(response);
            }
        });
    }


    private void displayContent(String friendName, String fof){
        ConstraintLayout fofWidget = (ConstraintLayout) LayoutInflater.from(AddFriend.this).inflate(R.layout.component_fof, null);
        TextView textViewFOF = fofWidget.findViewById(R.id.textViewFOF);
        TextView textViewFriend = fofWidget.findViewById(R.id.textViewFriend) ;
        textViewFOF.setText(fof);
        textViewFriend.setText(friendName);
        fofWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddFriend.this);
                builder.setCancelable(true);
                builder.setTitle("Add "+fof+" as a Friend?");
                builder.setMessage("You will receive all of "+ fof+"'s status updates");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                addFriend(fof);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        fofContainer.addView(fofWidget);
    }



    private void displayFOF(String myResponse) {
        try {
            JSONArray jr = new JSONArray(myResponse);

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb = (JSONObject) jr.get(i);
                String fof = jb.getString("USERNAME2");
                String friend = jb.getString("USERNAME1");
                displayContent(friend, fof);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addFriend(String friendUsername){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("friend", friendUsername);

        PHPRequest request = new PHPRequest();
        request.doRequest(AddFriend.this, "addfriend", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                Toast.makeText(AddFriend.this, response, Toast.LENGTH_LONG).show();
                fofContainer.removeAllViews();
                doGetFOF();
            }
        });
    }

    public void doAddFriend(View view) {
        EditText editTextFriendUsername = findViewById(R.id.editTextFriendUsername);
        if(editTextFriendUsername.getText().toString().equals("")){
            Toast.makeText(AddFriend.this, "Enter friend username", Toast.LENGTH_LONG).show();
            return;
        }
        addFriend(editTextFriendUsername.getText().toString());
    }

    public void doRefreshFOF(View view) {
        fofContainer.removeAllViews();
        doGetFOF();
    }
}