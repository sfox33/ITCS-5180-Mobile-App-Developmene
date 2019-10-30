package edu.uncc.inclass13;

import android.app.ActionBar;
import android.content.Intent;
import android.media.Image;
import android.provider.Telephony;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * @author Sean Fox & Andrew Lambropoulos
 * File Name: ReadMsgActivity.java
 * Assignment #13
 */
public class ReadMsgActivity extends AppCompatActivity {

    private TextView body,sender;
    private ImageButton reply;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("mailboxes");
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static Email email = new Email();
    public static String REPLY = "reply";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.read_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_msg);
        setTitle("Read Message");
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();

        body = (TextView) findViewById(R.id.readBody);
        sender = (TextView) findViewById(R.id.readSender);
        reply = (ImageButton) findViewById(R.id.readReply);

        if(getIntent() != null && getIntent().getExtras() != null) {
            email = (Email) getIntent().getExtras().get(InboxActivity.EMAIL);
            body.setText(email.getBody());
            sender.setText(email.getAuthor());
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.readReply:
                Intent intent = new Intent(ReadMsgActivity.this, ComposeMsgActivity.class);
                intent.putExtra(ReadMsgActivity.REPLY, email.getAuthor());
                startActivity(intent);
                finish();
                return true;
            case R.id.trashEmail:
                mDatabase.child(mAuth.getUid()).child(email.getEmailId()).removeValue();
                intent = new Intent(ReadMsgActivity.this, InboxActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
