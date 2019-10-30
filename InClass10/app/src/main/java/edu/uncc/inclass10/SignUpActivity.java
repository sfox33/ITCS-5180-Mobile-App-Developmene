package edu.uncc.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
 * SignUpActivity.java
 * Assignment: InClass10
 */
public class SignUpActivity extends AppCompatActivity {

    private final OkHttpClient client = new OkHttpClient();
    private TextView firstName, lastName, email, pass1, pass2;
    private Button cancel, signUp;
    private TokenResponse tokenResponse;
    private SharedPreferences sharedPref;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        firstName = (TextView) findViewById(R.id.firstName);
        lastName = (TextView) findViewById(R.id.lastName);
        email = (TextView) findViewById(R.id.signUpEmail);
        pass1 = (TextView) findViewById(R.id.signUpPass1);
        pass2 = (TextView) findViewById(R.id.signUpPass2);
        cancel = (Button) findViewById(R.id.cancelButton);
        signUp = (Button) findViewById(R.id.signUpButton2);
        sharedPref = getSharedPreferences("InClass10", Context.MODE_PRIVATE);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()) {
                    addUser(email.getText().toString(), pass1.getText().toString(), firstName.getText().toString(), lastName.getText().toString());
                } else {
                    if(!pass1.getText().toString().equals(pass2.getText().toString())) {
                        Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what){
                    case 0:
                        Toast.makeText(SignUpActivity.this, "Error: " + (String)msg.obj, Toast.LENGTH_LONG).show();
                        break;
                    case 1:
                        Toast.makeText(SignUpActivity.this, "User successfully created", Toast.LENGTH_LONG).show();
                        break;
                    default:
                }
                return false;
            }
        });
    }

    private boolean validateInput() {
        return !firstName.getText().toString().equals("") && !lastName.getText().toString().equals("")
                && !email.getText().toString().equals("") && !pass1.getText().toString().equals("")
                && pass1.getText().toString().equals(pass2.getText().toString());
    }

    public void addUser(String email, String password, String first, String last){
        RequestBody formBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("fname", first)
                .add("lname", last)
                .build();
        final Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/signup")
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
                Message message = new Message();

                Gson gson = new Gson();
                tokenResponse = gson.fromJson(str, TokenResponse.class);

                if(!tokenResponse.getStatus().equals("ok")) {
                    message.what = 0;
                    message.obj = tokenResponse.getMessage();
                    handler.sendMessage(message);
                } else {
                    message.what = 1;
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(getString(R.string.token), tokenResponse.getToken());
                    editor.putString(getString(R.string.fName), tokenResponse.getUser_fname());
                    editor.putString(getString(R.string.lName), tokenResponse.getUser_lname());
                    editor.putString(getString(R.string.userId), tokenResponse.getUser_id());
                    editor.apply();

                    handler.sendMessage(message);

                    Intent intent = new Intent(getBaseContext(), MessagesActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
