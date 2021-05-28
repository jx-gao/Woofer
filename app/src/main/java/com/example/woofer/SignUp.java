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

    EditText editTextUsername, editTextName, editTextPassword;
    boolean doesUserExist = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }

    public void createNewUser(String json) {
        try {
            JSONArray ja = new JSONArray(json);
            if(ja.length() == 0){
                createUserJSON();
            }else{
                Toast.makeText(SignUp.this, "Username already taken!", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void createUserJSON(){
        editTextUsername = (EditText) findViewById(R.id.editTextSignupUsername);
        editTextName = (EditText) findViewById(R.id.editTextSignupName);
        editTextPassword = (EditText) findViewById(R.id.editTextSignupPassword);

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

    public void doSignup(View view) {
        editTextUsername = (EditText) findViewById(R.id.editTextSignupUsername);
        PHPRequest request = new PHPRequest();
        request.doRequest(SignUp.this, "login", editTextUsername.getText().toString(), new RequestHandler() {
            @Override
            public void proccessResponse(String response) {
                createNewUser(response);
            }
        });
    }

    public void doGoToLogin(View view) {
        finish();
    }
}