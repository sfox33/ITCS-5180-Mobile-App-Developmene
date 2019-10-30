package edu.uncc.inclass10;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Sean on 4/7/2018.
 */

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ViewHolder> {
    private SharedPreferences sharedPref;
    private Handler handler;
    private Chatroom mDataset;
    private PrettyTime pt;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, time, body;
        public ImageButton deleteButton;
        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.author);
            time = (TextView) v.findViewById(R.id.time);
            body = (TextView) v.findViewById(R.id.chat);
            deleteButton = (ImageButton) v.findViewById(R.id.garbageButton);
        }
    }

    public ChatroomAdapter(SharedPreferences pref, Chatroom chat) {
        this.sharedPref = pref;
        this.mDataset = chat;
        this.pt = new PrettyTime();
        pt.setReference(new Date());
    }

    @Override
    public ChatroomAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_layout, parent, false);
        ChatroomAdapter.ViewHolder vh = new ChatroomAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mDataset.getMessages().remove(msg.arg1);
                notifyDataSetChanged();
                return false;
            }
        });

                //Determines if the thread gets a "Delete" button
        if(!sharedPref.getString("UserID", "").equals(mDataset.getMessages().get(pos).getUser_id())) {
            holder.deleteButton.setEnabled(false);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.deleteButton.setEnabled(true);
            holder.deleteButton.setVisibility(View.VISIBLE);
            //If the thread gets a delete button, set a listener
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Request remove = new Request.Builder()
                           .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/message/delete/" + mDataset.getMessages().get(pos).getId())
                           .header("Authorization", "BEARER " + sharedPref.getString("Token", ""))
                           .build();

                    new OkHttpClient().newCall(remove).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            String str = response.body().string();

                            Gson gson = new Gson();
                            Status status = gson.fromJson(str, Status.class);

                            if(status.status.equals("ok")) {
                                Message msg = new Message();
                                msg.arg1 = pos;
                                handler.sendMessage(msg);
                            }
                        }
                    });
                }
            });
        }
        holder.name.setText(mDataset.getMessages().get(pos).getUser_fname() + " " + mDataset.getMessages().get(pos).getUser_lname());
        holder.body.setText(mDataset.getMessages().get(pos).getMessage());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try{
            Date date = df.parse(mDataset.getMessages().get(pos).getCreated_at());
            holder.time.setText(pt.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.getMessages().size();
    }

    public void updateData(MessageObject msg) {
        mDataset.getMessages().add(0, msg);
    }
}
