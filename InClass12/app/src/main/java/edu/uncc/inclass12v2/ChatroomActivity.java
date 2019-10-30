package edu.uncc.inclass12v2;

import android.content.Context;
import android.provider.ContactsContract;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatroomActivity extends AppCompatActivity {

    private TextView title;
    private EditText chat;
    private ImageButton back, send;
    public static String THREAD = "KEY";
    private RecyclerView chatroom;
    private ChatroomAdapter adapter;
    private MessageObject tempMessage;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("threads");
    private DatabaseReference ref;
    private Map<String, Object> setIdUpdates = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        title = (TextView) findViewById(R.id.chatroomTitle);
        back = (ImageButton) findViewById(R.id.backButton);
        send = (ImageButton) findViewById(R.id.chatSend);
        chat = (EditText) findViewById(R.id.newChatThread);
        chatroom = (RecyclerView) findViewById(R.id.chatroom);
        send.setEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatroom.setLayoutManager(mLayoutManager);
        chatroom.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(chatroom.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(android.R.drawable.menu_frame));
        chatroom.addItemDecoration(mDividerItemDecoration);

        if(getIntent() != null && getIntent().getExtras() != null) {
            final ThreadObject threadObject = (ThreadObject) getIntent().getExtras().getSerializable(this.THREAD);
            title.setText(threadObject.getTitle());
            Log.d("demo", threadObject.toString());
            adapter = new ChatroomAdapter(threadObject, FirebaseAuth.getInstance(), threadObject.getId());
            chatroom.setAdapter(adapter);

            send.setEnabled(true);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!chat.getText().toString().equals("")) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                        Date date = new Date();
                        tempMessage = new MessageObject(user.getDisplayName(), user.getUid(), chat.getText().toString(), dateFormat.format(date).toString());
                        ref = FirebaseDatabase.getInstance().getReference("threads/" + threadObject.getId() + "/messages");
                        ref = ref.push();
                        ref.setValue(tempMessage);
                        tempMessage.setId(ref.getKey());
                        adapter.updateData(tempMessage);
                        threadObject.messages.put(tempMessage.getId(), tempMessage);
                        setIdUpdates.put("id", tempMessage.getId());
                        ref.updateChildren(setIdUpdates, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                //Do stuff
                            }
                        });
                        setIdUpdates.clear();
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(ChatroomActivity.this, "Enter chat information", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(ChatroomActivity.this, "Error finding chat logs.", Toast.LENGTH_LONG).show();
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}