package edu.uncc.inclass13;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Sean Fox & Andrew Lambropoulos
 * File Name: ComposeMsgActivity.java
 * Assignment #13
 */
public class ComposeMsgActivity extends AppCompatActivity {

    private TextView user, body;
    private ImageButton pickUser;
    private Button send;
    private AlertDialog.Builder dialog;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("mailboxes");
    private DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
    static private String destination = "";
    private final ArrayList<String> userNames = new ArrayList<>();
    private final ArrayList<String> userIds = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose_msg);
        setTitle("Compose Message");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = (TextView) findViewById(R.id.composeUser);
        body = (TextView) findViewById(R.id.composeBody);
        pickUser = (ImageButton) findViewById(R.id.composePickUser);
        send = (Button) findViewById(R.id.composeSend);
        dialog = new AlertDialog.Builder(ComposeMsgActivity.this);

        if(getIntent() != null && getIntent().getExtras() != null) {
            pickUser.setEnabled(false);
            user.setText(getIntent().getExtras().getString(ReadMsgActivity.REPLY));
        }

        mDatabaseUsers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo", "Listener worked");
                Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();
                for (Map.Entry<String, Object> entry : users.entrySet()) {
                    Log.d("demo", "Inside for loop.");
                    userNames.add((String)entry.getValue());
                    userIds.add((String)entry.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        pickUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setTitle("Users")
                        .setCancelable(false)
                        .setItems(userNames.toArray(new String[userNames.size()]), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                destination = userIds.get(which);
                                user.setText(userNames.get(which));
                            }
                        });
                final AlertDialog simpleAlert = dialog.create();
                simpleAlert.show();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(body.getText().toString().equals("")){
                    Toast.makeText(ComposeMsgActivity.this, "Please write an email", Toast.LENGTH_LONG).show();
                } else if(user.getText().toString().equals("")) {
                    Toast.makeText(ComposeMsgActivity.this, "Please select a recipient", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Email email = new Email(mAuth.getCurrentUser().getDisplayName(), body.getText().toString());
                        int index = -1;
                        Log.d("demo", "Sending email.  User is " + user.getText().toString());
                        Log.d("demo", "Size of array is " + userNames.size());
                        for(int i = 0; i < userNames.size(); i++) {
                            Log.d("demo", "For comparison: found " + userNames.get(i) +", and user is " + user.getText().toString());
                            if(userNames.get(i).equals(user.getText().toString())) {
                                index = i;
                                break;
                            }
                        }
                        //add email to database
                        DatabaseReference ref = mDatabase.child(userIds.get(index)).push();
                        email.setEmailId(ref.getKey());
                        ref.setValue(email);
                        Toast.makeText(ComposeMsgActivity.this, "Message has been sent", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ComposeMsgActivity.this, InboxActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
