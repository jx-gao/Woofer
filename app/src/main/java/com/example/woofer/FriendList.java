package com.example.woofer;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FriendList extends AppCompatActivity {

    String username;
    LinearLayout friendLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);
        friendLayout = findViewById(R.id.friendListView);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        doGetFriends();
    }

    private void displayContent(String friend){
        ConstraintLayout friendListWidget = (ConstraintLayout) LayoutInflater.from(FriendList.this).inflate(R.layout.component_friend, null);
        TextView name = friendListWidget.findViewById(R.id.friendName) ;
        name.setText(friend);
        friendListWidget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doInteract(friend);
            }
        });
        friendLayout.addView(friendListWidget);

    }


    public void doInteract(String name){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Friend Options");
        alertDialog.setMessage("Would you like to remove "+name+" as a friend?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Remove Friend", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
                doRemoveFriend(name);
            } });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {
            }});

        alertDialog.show();
    }

    private void doRemoveFriend(String friend){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("friend",friend);
        PHPRequest request = new PHPRequest();
        request.doRequest(FriendList.this, "removefriend", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                Toast.makeText(FriendList.this, response, Toast.LENGTH_LONG).show();
                friendLayout.removeAllViews();
                doGetFriends();
            }
        });

    }



    public void doGetFriends(){
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        PHPRequest request = new PHPRequest();
        request.doRequest(FriendList.this, "friendlist", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                processJSON(response);
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

        try {
            JSONArray jr = new JSONArray(myResponse);

            for (int i = 0; i < jr.length(); i++) {
                JSONObject jb = (JSONObject) jr.get(i);
                String friendUName = jb.getString("USERNAME");
                displayContent(friendUName);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void displayFail(){
        LinearLayout friendListLayout = findViewById(R.id.friendListView);
        TextView output = new TextView(this);
        output.setText("You have no friends lmao");
        friendListLayout.addView(output);
    }

    public void doAddFriends(View v){
        Intent intent = new Intent(getApplicationContext(), AddFriend.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void doRefreshFriends(View v){
        friendLayout.removeAllViews();
        doGetFriends();
    }

}