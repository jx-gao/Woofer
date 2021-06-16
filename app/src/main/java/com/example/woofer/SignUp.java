package com.example.woofer;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class SignUp extends AppCompatActivity {

    private EditText editTextUsername, editTextConfirmPassword, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void createNewUser() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        ContentValues cv = new ContentValues();
        cv.put("username", editTextUsername.getText().toString());
        cv.put("password", Login.computeHash(editTextPassword.getText().toString()));

        PHPRequest request = new PHPRequest();
        request.doRequest(SignUp.this, "signup", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                proccessOutcome(response);
            }
        });
    }

    public boolean checkFieldsEmpty(){
        return editTextUsername.getText().toString().equals("")
                || editTextPassword.getText().toString().equals("")
                || editTextConfirmPassword.getText().toString().equals("");
    }

    public void proccessOutcome(String response){
        if(response.equals("New record created successfully")){
            Toast.makeText(SignUp.this, "Successfully created account!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }else if(response.equals("User already exists")){
            Toast.makeText(SignUp.this, "Username already taken", Toast.LENGTH_LONG).show();
            return;
        }
        //when error occurs in signup php
        Toast.makeText(SignUp.this, "Error trying to create user, try again later", Toast.LENGTH_LONG).show();
    }

    public void doSignup(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        editTextUsername = findViewById(R.id.editTextSignupUsername);
        editTextPassword = findViewById(R.id.editTextSignupPassword);
        editTextConfirmPassword = findViewById(R.id.editTextSignupConfirmPassword);

        if(checkFieldsEmpty()){
            Toast.makeText(SignUp.this, "One or more fields are empty", Toast.LENGTH_LONG).show();
            return;
        }

        if (!editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString())){
            Toast.makeText(SignUp.this, "Passwords don't match", Toast.LENGTH_LONG).show();
            return;
        }

        createNewUser();
    }

    public void doGoToLogin(View view) {
        finish();
    }
}