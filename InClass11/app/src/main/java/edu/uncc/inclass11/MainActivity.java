package edu.uncc.inclass11;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Andrew Lambropoulos on 4/9/2018.
 * Created by Sean Fox
 * MainActivity.java
 * Assignment: InClass11
 */
public class MainActivity extends AppCompatActivity {

    private OkHttpClient client;
    private RecyclerView recycler;
    private EditText search;
    private Button go, reset;
    private String clientId;
    private Handler handler;
    private ImageAdapter imageAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.image_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.refresh:
                search.setText(R.string.empty);
                imageAdapter.posts = new ArrayList<>();
                imageAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        client = new OkHttpClient();
        recycler = (RecyclerView) findViewById(R.id.photos);
        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
        search = (EditText) findViewById(R.id.search);
        go = (Button) findViewById(R.id.button);
        clientId = "049b272e7a4360a";

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                QueryResult queryResult = (QueryResult) msg.obj;
                imageAdapter = new ImageAdapter(queryResult.getData());
                recycler.setItemAnimator(new DefaultItemAnimator());
                recycler.setAdapter(imageAdapter);
                return false;
            }
        });


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!search.getText().toString().equals("")) {
                    final Request request = new Request.Builder()
                            .url("https://api.imgur.com/3/gallery/search/top/all/?q="+search.getText().toString()+"&q_type=png&q_type=jpg")
                            .header("Authorization", "Client-ID " + clientId)
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

                            QueryResult result = gson.fromJson(str, QueryResult.class);
                            Message message = new Message();
                            message.obj = result;
                            handler.sendMessage(message);
                        }
                    });
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a search", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
