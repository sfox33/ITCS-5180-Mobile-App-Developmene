package edu.uncc.inclass12v2;

import android.content.Intent;
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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private TextView firstName, lastName, email, pass1, pass2;
    private Button cancel, signUp;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

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
                    mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass1.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass1.getText().toString());
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(firstName.getText().toString() + " " + lastName.getText().toString())
                                                .build();
                                        user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Intent intent = new Intent(getBaseContext(), MessagesActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                } else {
                                                    Toast.makeText(SignUpActivity.this, "Profile could not be created.", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                } else {
                    if(!pass1.getText().toString().equals(pass2.getText().toString())) {
                        Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private boolean validateInput() {
        return !firstName.getText().toString().equals("") && !lastName.getText().toString().equals("")
                && !email.getText().toString().equals("") && !pass1.getText().toString().equals("")
                && pass1.getText().toString().equals(pass2.getText().toString());
    }
}
