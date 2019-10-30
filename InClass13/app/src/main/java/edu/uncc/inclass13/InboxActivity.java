package edu.uncc.inclass13;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItem;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sean Fox & Andrew Lambropoulos
 * File Name: InboxActivity.java
 * Assignment #13
 */
public class InboxActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MenuItem newEmail;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("mailboxes");
    private DatabaseReference mDatabaseUsers = FirebaseDatabase.getInstance().getReference("users");
    private EmailAdapter messagesAdapter;
    private DatabaseReference ref;
    private Map<String, Object> userIdUpdates = new HashMap<>();

    public static String EMAIL = "email";
    public static String ID = "id";
    public static String NAME = "name";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.inbox_menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE | ActionBar.DISPLAY_USE_LOGO);
        getSupportActionBar().setIcon(R.drawable.ic_launcher);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = (RecyclerView) findViewById(R.id.Inbox);
        //newEmail = (MenuItem) findViewById(R.id.createEmail);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(android.R.drawable.menu_frame));
        recyclerView.addItemDecoration(mDividerItemDecoration);

        final ArrayList<Email> array = new ArrayList<>();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Email temp;
                array.clear();
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    if(userSnapshot.getKey().equals(mAuth.getUid())) {
                        for(DataSnapshot emailSnapshot : userSnapshot.getChildren()) {
                            temp = emailSnapshot.getValue(Email.class);
                            Log.d("demo", "Pulling email: " + temp.toString());
                            array.add(temp);
                        }
                    }
                }
                Collections.sort(array);
                messagesAdapter = new EmailAdapter(array, mAuth, new OnEmailClickListener() {
                    @Override
                    public void onEmailClick(Email email) {
                        Intent intent = new Intent(InboxActivity.this, ReadMsgActivity.class);
                        intent.putExtra(InboxActivity.EMAIL, email);
                        mDatabase.child(mAuth.getUid()).child(email.getEmailId()).child("isRead").setValue(true);
                        startActivity(intent);
                        //finish();
                    }
                });
                recyclerView.setAdapter(messagesAdapter);
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Do something
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.createEmail:
                Log.d("demo", "Inside of the switch");
                Intent intent = new Intent(InboxActivity.this, ComposeMsgActivity.class);
                startActivity(intent);
                //finish();
                return true;
            case R.id.logout:
                intent = new Intent(InboxActivity.this, MainActivity.class);
                mAuth.signOut();
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
