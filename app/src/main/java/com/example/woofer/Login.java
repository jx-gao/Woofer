package com.example.woofer;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public boolean checkFieldsEmpty(){
        return editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals("");
    }

    public void processJSON(String json) {
        try {
            JSONArray ja = new JSONArray(json);
            if(ja.length() == 0){
                Toast.makeText(Login.this, "Username or password incorrect", Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject jo = ja.getJSONObject(0);
            if(jo.getString("Password").equals(editTextPassword.getText().toString())){
                Intent intent = new Intent(getApplicationContext(), Status.class);
                intent.putExtra("username", jo.getString("Username"));
                startActivity(intent);

                finish();
            }else{
                Toast.makeText(Login.this, "Username or password incorrect", Toast.LENGTH_LONG).show();
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
            public void processResponse(String response) {
                processJSON(response);
            }
        });
    }

    public void doGoToSignup(View view) {
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void doLogin(View view) {
        editTextUsername = findViewById(R.id.editTextLoginUsername);
        editTextPassword = findViewById(R.id.editTextLoginPassword);

        login();
    }
}