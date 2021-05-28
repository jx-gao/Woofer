package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Login extends AppCompatActivity {

    TextView inputUsername, inputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void processJSON(String json) {
        inputPassword = (TextView) findViewById(R.id.textViewPassword);
        try {
            JSONArray ja = new JSONArray(json);
            if(ja.length() == 0){
                Toast.makeText(Login.this, "Username incorrect", Toast.LENGTH_LONG).show();
            }else{
                inputPassword.getText().toString();
                JSONObject jo = ja.getJSONObject(0);
                if(jo.getString("Password").equals(inputPassword.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), FriendList.class);
                    intent.putExtra("username", inputUsername.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doLogin(View view) {
        inputUsername = (TextView) findViewById(R.id.textViewUsername);
        PHPRequest request = new PHPRequest();
        request.doRequest(Login.this, "login", inputUsername.getText().toString(), new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
                processJSON(response);
            }
        });
    }

    public void doGoToSignup(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }
}