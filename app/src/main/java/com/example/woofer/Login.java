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

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Login extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private String username, password,hashedPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public boolean checkFieldsEmpty(){ //boolean to check if any fields are empty
        return editTextUsername.getText().toString().equals("") || editTextPassword.getText().toString().equals("");
    }

    public void processJSON(String json) { //checks password hash content against received hash, if valid opens status screen, else shows incorrect error
        try {
            JSONArray ja = new JSONArray(json);
            if(ja.length() == 0){
                Toast.makeText(Login.this, "Username or password incorrect", Toast.LENGTH_LONG).show();
                return;
            }
            JSONObject jo = ja.getJSONObject(0);
            if(jo.getString("HASHEDPASS").equals(hashedPassword)){
                Intent intent = new Intent(getApplicationContext(), Status.class);
                intent.putExtra("username", jo.getString("USERNAME").toString());
                startActivity(intent);

                finish();
            }else{
                Toast.makeText(Login.this, "Username or password incorrect", Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //hashes string to SHA-256
    public static String computeHash(String input) throws NoSuchAlgorithmException, UnsupportedEncodingException { //https://stackoverflow.com/questions/9661008/compute-sha256-hash-in-android-java-and-c-sharp
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();

        byte[] byteData = digest.digest(input.getBytes("UTF-8"));
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < byteData.length; i++){
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public void login() throws UnsupportedEncodingException, NoSuchAlgorithmException { //gets data from the fields and uses them in http get request
        ContentValues cv = new ContentValues();
        if (checkFieldsEmpty()) {
            Toast.makeText(Login.this, "Username or password needs to be entered", Toast.LENGTH_LONG).show();
            return;
        }
        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();
        hashedPassword = computeHash(password);
        cv.put("username", username);
        PHPRequest request = new PHPRequest();
        request.doRequest(Login.this, "login", cv, new RequestHandler() {
            @Override
            public void processResponse(String response) {
                processJSON(response);
            }
        });
    }

    public void doGoToSignup(View view) { //opens sign up screen
        Intent intent = new Intent(getApplicationContext(), SignUp.class);
        startActivity(intent);
    }

    public void doLogin(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        editTextUsername = findViewById(R.id.editTextLoginUsername);
        editTextPassword = findViewById(R.id.editTextLoginPassword);

        login();
    }
}