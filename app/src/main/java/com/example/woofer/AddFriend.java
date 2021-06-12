package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class AddFriend extends AppCompatActivity {

    private String username;
    private EditText editTextFriendUsername;
    LinearLayout fofContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        Intent intent = getIntent();

        username = intent.getStringExtra("username");
        fofContainer = (LinearLayout) findViewById(R.id.fofLinearView);

        doGetFOF();
    }

    private void createLabels() {
        TextView friend = new TextView(this), fof = new TextView(this);
        friend.setText("FRIEND");
        friend.setTextSize(15);
        friend.setPadding(20,20,50,20);
        fof.setText("THEIR FRIEND");
        fof.setTextSize(15);
        fof.setPadding(20,20,20,20);

        LinearLayout labelLayout = new LinearLayout(this);
        labelLayout.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = (float) 0.5;
        labelLayout.addView(friend,lp);
        labelLayout.addView(fof);

        labelLayout.setBackgroundColor(Color.parseColor("#ca9b52"));
        fofContainer.addView(labelLayout);
    }

    private void doGetFOF() {
        createLabels();
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        PHPRequest request = new PHPRequest();
        request.doRequest(AddFriend.this, "fof", cv, new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
                processJSON(response);
            }
        });
    }

    public void processJSON(String json){
        try {
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                FOFLayout fofL = new FOFLayout(this);
                fofL.populate(jo);
                if(i%2==0){
                    fofL.setBackgroundColor(Color.parseColor("#EEEEFF"));
                }
                fofContainer.addView(fofL);

                fofL.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            addFriend(jo.getString("USERNAME2"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
            public void proccessResponse(String response) {
                Toast.makeText(AddFriend.this, response, Toast.LENGTH_LONG).show();
                fofContainer.removeAllViews();
                doGetFOF();
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

    public void doRefreshFOF(View view) {
        fofContainer.removeAllViews();
        doGetFOF();
    }
}