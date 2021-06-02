package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

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
        ArrayList<String> keys = new ArrayList<>();
        keys.add("username");
        keys.add("name");
        keys.add("password");

        ArrayList<String> values = new ArrayList<>();
        values.add(editTextUsername.getText().toString());
        values.add(editTextName.getText().toString());
        values.add(editTextPassword.getText().toString());

        PHPRequest request = new PHPRequest();
        request.doInsertRequest(SignUp.this, "signup", keys, values, new RequestHandler() {
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
        PHPRequest request = new PHPRequest();
        request.doRequest(SignUp.this, "login", editTextUsername.getText().toString(), new RequestHandler() {
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