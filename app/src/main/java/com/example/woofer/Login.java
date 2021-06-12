package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public boolean checkFieldsEmpty(){
        if(editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    public void processJSON(String json) {
        try {
            JSONArray ja = new JSONArray(json);
            if(ja.length() == 0){
                Toast.makeText(Login.this, "Username or password incorrect", Toast.LENGTH_LONG).show();
            }else{
                JSONObject jo = ja.getJSONObject(0);
                if(jo.getString("Password").equals(editTextPassword.getText().toString())){
                    Intent intent = new Intent(getApplicationContext(), Status.class);
                    intent.putExtra("username", editTextUsername.getText().toString());

                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(Login.this, "Username or password incorrect", Toast.LENGTH_LONG).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void login() {
        ContentValues cv = new ContentValues();
        cv.put("username", editTextUsername.getText().toString());
        if (checkFieldsEmpty()) {
            Toast.makeText(Login.this, "Username or password needs to be entered", Toast.LENGTH_LONG).show();
            return;
        }
        PHPRequest request = new PHPRequest();
        request.doRequest(Login.this, "login", cv, new RequestHandler() {
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

    public void doLogin(View view) {
        editTextUsername = (EditText) findViewById(R.id.editTextLoginUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextLoginPassword);

        login();
    }
}