package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    private EditText editTextUsername, editTextName, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void checkIfUserExists(String json) {
        if(json.equals("[]")){
            createNewUser();
            return;
        }
        Toast.makeText(SignUp.this, "Username already taken", Toast.LENGTH_LONG).show();
    }

    public void createNewUser(){
        ContentValues cv = new ContentValues();
        cv.put("username", editTextUsername.getText().toString());
        cv.put("name", editTextName.getText().toString());
        cv.put("password", editTextPassword.getText().toString());

        PHPRequest request = new PHPRequest();
        request.doRequest(SignUp.this, "signup", cv, new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
//                System.out.println(response);
            }
        });
        Toast.makeText(SignUp.this, "Successfully created account!", Toast.LENGTH_LONG).show();
        finish();
    }

    public boolean checkFieldsEmpty(){
        if(editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals("") || editTextName.getText().toString().equals("")){
            return true;
        }
        return false;
    }

    public void doSignup(View view) {
        editTextUsername = (EditText) findViewById(R.id.editTextSignupUsername);
        editTextName = (EditText) findViewById(R.id.editTextSignupName);
        editTextPassword = (EditText) findViewById(R.id.editTextSignupPassword);
        if(checkFieldsEmpty()){
            Toast.makeText(SignUp.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues cv = new ContentValues();
        cv.put("username", editTextUsername.getText().toString());
        PHPRequest request = new PHPRequest();
        request.doRequest(SignUp.this, "login", cv, new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
                checkIfUserExists(response);
            }
        });
    }

    public void doGoToLogin(View view) {
        finish();
    }
}