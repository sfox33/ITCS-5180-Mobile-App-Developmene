package edu.uncc.inclass10;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatroomActivity extends AppCompatActivity {

    private TextView title;
    private EditText chat;
    private ImageButton back, send;
    public static String THREAD = "KEY";
    private final OkHttpClient client = new OkHttpClient();
    private SharedPreferences sharedPref;
    private RecyclerView chatroom;
    private Chatroom chats;
    private ChatroomAdapter adapter;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        title = (TextView) findViewById(R.id.chatroomTitle);
        back = (ImageButton) findViewById(R.id.backButton);
        send = (ImageButton) findViewById(R.id.chatSend);
        chat = (EditText) findViewById(R.id.newChatThread);
        chatroom = (RecyclerView) findViewById(R.id.chatroom);
        sharedPref = getSharedPreferences("InClass10", Context.MODE_PRIVATE);
        send.setEnabled(false);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        chatroom.setLayoutManager(mLayoutManager);
        chatroom.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(chatroom.getContext(), DividerItemDecoration.VERTICAL);
        mDividerItemDecoration.setDrawable(getResources().getDrawable(android.R.drawable.menu_frame));
        chatroom.addItemDecoration(mDividerItemDecoration);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(msg.arg1 == 1){
                    adapter.notifyDataSetChanged();
                    return false;
                }
                chatroom.setAdapter(adapter);
                return false;
            }
        });

        if(getIntent() != null && getIntent().getExtras() != null) {
            final ThreadObject threadObject = (ThreadObject) getIntent().getExtras().getSerializable(this.THREAD);
            title.setText(threadObject.getTitle());

            final Request request = new Request.Builder()
                    .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/messages/" + threadObject.getId())
                    .header("Authorization", "BEARER " + sharedPref.getString(getString(R.string.token), ""))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { e.printStackTrace(); }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String str = response.body().string();
                    Gson gson = new Gson();

                    chats = gson.fromJson(str, Chatroom.class);

                    if(!chats.getStatus().equals("ok")) {
                        Toast.makeText(getBaseContext(), "Error: Could not fetch messages", Toast.LENGTH_LONG).show();
                    } else {
                        adapter = new ChatroomAdapter(sharedPref, chats);
                        handler.sendMessage(new Message());
                    }
                }
            });
            send.setEnabled(true);
            send.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!chat.getText().toString().equals("")) {
                        RequestBody formBody = new FormBody.Builder()
                                .add("message", chat.getText().toString())
                                .add("thread_id", threadObject.getId())
                                .build();

                        final Request request = new Request.Builder()
                                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/message/add")
                                .header("Authorization", "BEARER " + sharedPref.getString(getString(R.string.token), ""))
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
                                Chat chat = gson.fromJson(str, Chat.class);

                                if (!chat.getStatus().equals("ok")) {
                                    //Insert toast
                                } else {
                                    adapter.updateData(chat.getMessage());
                                    Message message = new Message();
                                    message.arg1 = 1;
                                    handler.sendMessage(message);
                                }
                            }
                        });
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
