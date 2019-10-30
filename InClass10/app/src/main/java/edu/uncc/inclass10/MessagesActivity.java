package edu.uncc.inclass10;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

/**
 * Created by Andrew Lambropoulos on 2/19/2018.
 * Created by Sean Fox
 * MessagesActivity.java
 * Assignment: InClass10
 */
public class MessagesActivity extends AppCompatActivity {

    private ImageButton logout, addThread;
    private Toolbar toolbar;
    private SharedPreferences sharedPref;
    private TextView name;
    private EditText title;
    private RecyclerView recyclerView;
    private final OkHttpClient client = new OkHttpClient();
    public Handler handler;
    private ThreadsResponse threadsResponse;
    private MessagesAdapter messagesAdapter;
    private CreatedThread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setTitle("Message Threads");

        sharedPref = getSharedPreferences("InClass10", Context.MODE_PRIVATE);

        logout = (ImageButton) findViewById(R.id.backButton);
        name=(TextView)findViewById(R.id.chatroomTitle);
        String str = sharedPref.getString(getString(R.string.fName), "") + " " + sharedPref.getString(getString(R.string.lName), "");
        name.setText(str);
        addThread = (ImageButton) findViewById(R.id.addButton);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MessagesActivity.this));
        title = (EditText) findViewById(R.id.threadTitle);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
            if(msg.what == 0) {
                messagesAdapter = new MessagesAdapter((ThreadsResponse) msg.obj, MessagesActivity.this, new OnThreadClickListener() {
                    @Override
                    public void onThreadClick(ThreadObject threadObject) {
                        Intent intent = new Intent(MessagesActivity.this, ChatroomActivity.class);
                        intent.putExtra(ChatroomActivity.THREAD, threadObject);
                        startActivity(intent);
                    }
                });
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
                mDividerItemDecoration.setDrawable(getResources().getDrawable(android.R.drawable.menu_frame));
                recyclerView.addItemDecoration(mDividerItemDecoration);
                recyclerView.setAdapter(messagesAdapter);
            } else if(msg.what == 1){
                if(!((CreatedThread)msg.obj).getStatus().equals("ok")) {
                    Toast.makeText(MessagesActivity.this, "Error: Could not fetch threads", Toast.LENGTH_LONG).show();
                } else {
                    messagesAdapter.updateData(((CreatedThread)msg.obj).getThreadObject());
                    Toast.makeText(MessagesActivity.this, "Thread has been added", Toast.LENGTH_LONG).show();
                }
            } else {
                if(!((Status) msg.obj).status.equals("ok")) {
                    Toast.makeText(MessagesActivity.this, "Unable to delete thread.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MessagesActivity.this, "Thread deleted.", Toast.LENGTH_LONG).show();
                }
            }
                return false;
            }
        });

        final Request request = new Request.Builder()
                .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread")
                .header("Authorization", "BEARER " + sharedPref.getString(getString(R.string.token), ""))
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
                threadsResponse = gson.fromJson(str, ThreadsResponse.class);

                if(!threadsResponse.getStatus().equals("ok")) {
                    Toast.makeText(getBaseContext(), "Error: Could not fetch threads", Toast.LENGTH_LONG).show();
                } else {
                    Message message = new Message();
                    message.obj = threadsResponse;
                    message.what = 0;
                    handler.sendMessage(message);
                }
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.token));
                editor.remove(getString(R.string.fName));
                editor.remove(getString(R.string.lName));
                editor.remove(getString(R.string.userId));
                editor.apply();
                Intent intent = new Intent(MessagesActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        addThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!title.getText().toString().equals("")) {
                    RequestBody formBody = new FormBody.Builder()
                            .add("title", title.getText().toString())
                            .build();
                    final Request add = new Request.Builder()
                            .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/add")
                            .header("Authorization", "BEARER " + sharedPref.getString(getString(R.string.token), ""))
                            .post(formBody)
                            .build();
                    Toast.makeText(MessagesActivity.this, "Adding thread", Toast.LENGTH_SHORT).show();
                    client.newCall(add).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String str = response.body().string();

                            Gson gson = new Gson();
                            thread = gson.fromJson(str, CreatedThread.class);

                            Message message = new Message();
                            message.what = 1;
                            message.obj = thread;
                            handler.sendMessage(message);
                        }
                    });
                } else {
                    Toast.makeText(MessagesActivity.this, "Please enter a thread name.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
