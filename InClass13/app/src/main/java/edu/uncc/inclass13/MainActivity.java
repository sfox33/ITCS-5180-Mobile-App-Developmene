package edu.uncc.inclass13;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author Sean Fox & Andrew Lambropoulos
 * File Name: MainActivity.java
 * Assignment #13
 */
public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private TextView email, password;
    private Button login, signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(MainActivity.this, InboxActivity.class);
            startActivity(intent);
            Toast.makeText(MainActivity.this, "User Already Logged In", Toast.LENGTH_SHORT).show();
        }

        setContentView(R.layout.activity_main);
        setTitle("MessageMe!");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currUser = mAuth.getCurrentUser();
        email = (TextView) findViewById(R.id.loginEmail);
        password = (TextView) findViewById(R.id.loginPass);
        login = (Button) findViewById(R.id.loginButton);
        signUp = (Button) findViewById(R.id.newUserButton);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isConnectedOnline()) {
                    Toast.makeText(MainActivity.this, "No connection found.", Toast.LENGTH_SHORT).show();
                } else {
                    if (!login.getText().toString().equals("") && !password.getText().toString().equals("")) {
                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if(task.isSuccessful()) {
                                            Intent intent = new Intent(getBaseContext(), InboxActivity.class);
                                            startActivity(intent);
                                            finish();
                                            Toast.makeText(MainActivity.this, "Profile found", Toast.LENGTH_LONG).show();
                                        } else {
                                            Toast.makeText(MainActivity.this, "Profile not found", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
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
    }

    private boolean isConnectedOnline() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
}
