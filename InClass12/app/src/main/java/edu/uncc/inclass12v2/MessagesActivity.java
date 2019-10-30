package edu.uncc.inclass12v2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessagesActivity extends AppCompatActivity {

    private ImageButton logout, addThread;
    private TextView name;
    private EditText title;
    private RecyclerView recyclerView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("threads");
    private MessagesAdapter messagesAdapter;
    private DatabaseReference ref;
    private Map<String, Object> userIdUpdates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        logout = (ImageButton) findViewById(R.id.backButton);
        name=(TextView)findViewById(R.id.chatroomTitle);
        addThread = (ImageButton) findViewById(R.id.addButton);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
        title = (EditText) findViewById(R.id.threadTitle);
        String nameText = mAuth.getCurrentUser().getDisplayName();

        final ArrayList<ThreadObject> array = new ArrayList<>();

        if(nameText != null && !nameText.equals("")) {
            name.setText(nameText);
        } else {
            name.setText("Error");
        }

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(android.R.drawable.menu_frame));
        recyclerView.addItemDecoration(mDividerItemDecoration);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("demo", "Inside single value event (MessagesActivity).");
                ThreadObject temp;
                array.clear();
                for(DataSnapshot threadSnapshot : dataSnapshot.getChildren()) {
                    temp = threadSnapshot.getValue(ThreadObject.class);
                    array.add(temp);
                }
                messagesAdapter = new MessagesAdapter(array, mAuth, new OnThreadClickListener() {
                    @Override
                    public void onThreadClick(ThreadObject threadObject) {
                        Intent intent = new Intent(MessagesActivity.this, ChatroomActivity.class);
                        intent.putExtra(ChatroomActivity.THREAD, threadObject);
                        startActivity(intent);
                    }
                });
                recyclerView.setAdapter(messagesAdapter);
                messagesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MessagesActivity.this, "Threads could not be retrieved.", Toast.LENGTH_LONG).show();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MessagesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().equals("")) {
                    FirebaseUser currUser = mAuth.getCurrentUser();
                    ThreadObject thread = new ThreadObject(currUser.getDisplayName(), currUser.getUid(), title.getText().toString(), new HashMap<String, MessageObject>());
                    ref = mDatabase.push();
                    ref.setValue(thread);
                    thread.setId(ref.getKey());
                    userIdUpdates.put("id", ref.getKey());
                    ref.updateChildren(userIdUpdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) { /*Look this up to put things here later*/ }
                    });
                    messagesAdapter.updateData(thread);
                    userIdUpdates.clear();
                } else {
                    Toast.makeText(MessagesActivity.this, "Please enter a thread name.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
