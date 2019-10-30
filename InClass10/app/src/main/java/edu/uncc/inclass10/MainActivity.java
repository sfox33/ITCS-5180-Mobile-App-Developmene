package edu.uncc.inclass10;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.TokenWatcher;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Andrew Lambropoulos on 2/19/2018.
 * Created by Sean Fox
 * MainActivity.java
 * Assignment: InClass10
 */
public class MainActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();

    private TextView email, password;
    private Button login, signUp;
    private Context context;
    private TokenResponse tokenResponse;
    private SharedPreferences sharedPref;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getSharedPreferences("InClass10", Context.MODE_PRIVATE);

        if(sharedPref.contains(getString(R.string.token)) && !sharedPref.getString(getString(R.string.token), "").equals("")) {
            moveOn();
        }

        setContentView(R.layout.activity_main);

        setTitle("Chat Room");
        email = (TextView) findViewById(R.id.loginEmail);
        password = (TextView) findViewById(R.id.loginPass);
        login = (Button) findViewById(R.id.loginButton);
        signUp = (Button) findViewById(R.id.signUpButton);
        context = getApplicationContext();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnectedOnline()) {
                    Toast.makeText(MainActivity.this, "No connection found.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!login.getText().toString().equals("") && !password.getText().toString().equals("")) {
                        validateLogin(email.getText().toString(), password.getText().toString());
                    } else {
                        Toast.makeText(MainActivity.this, "Please enter valid information", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    public void validateLogin(String username, String password){
        RequestBody formBody = new FormBody.Builder()
                .add("email", username)
                .add("password", password)
                .build();
        final Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/login")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();

                Gson gson = new Gson();
                tokenResponse = gson.fromJson(str, TokenResponse.class);

                if(!tokenResponse.getStatus().equals("ok")) {
                    Message message = new Message();
                    handler.sendMessage(message);
                } else {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.token), tokenResponse.getToken());
                    editor.putString(getString(R.string.fName), tokenResponse.getUser_fname());
                    editor.putString(getString(R.string.lName), tokenResponse.getUser_lname());
                    editor.putString(getString(R.string.userId), tokenResponse.getUser_id());
                    editor.apply();
                    MainActivity.this.moveOn();
                }
            }
        });
    }

    private void moveOn() {
        Intent intent = new Intent(getBaseContext(), MessagesActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
