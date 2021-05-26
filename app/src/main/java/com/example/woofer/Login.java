package com.example.woofer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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

    public void doLogin(View view) {
        inputUsername = (TextView) findViewById(R.id.textViewUsername);
        inputPassword = (TextView) findViewById(R.id.textViewPassword);

        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("username", inputUsername.getText().toString())
                .add("password", inputPassword.getText().toString())
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s1601812/login.php")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(myResponse.equals("[]")){
                                inputUsername.setText("Failed");
                            }else{
                                Intent intent = new Intent(getApplicationContext(), FriendList.class);
                                intent.putExtra("username", inputUsername.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }
}