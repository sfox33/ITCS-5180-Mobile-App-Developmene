package edu.uncc.inclass12v2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean on 4/17/2018.
 */

public class ChatroomAdapter extends RecyclerView.Adapter<ChatroomAdapter.ViewHolder> {
    private ThreadObject mDataset;
    private FirebaseAuth mAuth;
    private PrettyTime pt;
    private String parentThreadId;

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

    public ChatroomAdapter(ThreadObject chat, FirebaseAuth auth, String id) {
        this.mDataset = chat;
        mDataset.updateArray();
        this.mAuth = auth;
        this.parentThreadId = id;
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
        Log.d("demo", "From ChatAdap: " + mDataset.toString());
        //Determines if the thread gets a "Delete" button
        if(!mAuth.getCurrentUser().getUid().equals(mDataset.messagesArray.get(pos).getUser_id())) {
            holder.deleteButton.setEnabled(false);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.deleteButton.setEnabled(true);
            holder.deleteButton.setVisibility(View.VISIBLE);
            //If the thread gets a delete button, set a listener
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Log.d("demo", "threads/" + parentThreadId + "/" + "messages/" + pos);
                    //Log.d("demo", "Specifics: " + mDataset.get(pos).toString());
                    FirebaseDatabase.getInstance().getReference("threads/" + parentThreadId + "/messages/" + mDataset.messagesArray.get(pos).getId()).removeValue();
                    mDataset.messages.remove(mDataset.messagesArray.get(pos).getId());
                    mDataset.messagesArray.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }
        holder.name.setText(mDataset.messagesArray.get(pos).getUser_name());
        holder.body.setText(mDataset.messagesArray.get(pos).getMessage());
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try{
            Date date = df.parse(mDataset.messagesArray.get(pos).getCreated_at());
            holder.time.setText(pt.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.messages.size();
    }

    public void updateData(MessageObject msg) {
        mDataset.messages.put(msg.getId(), msg);
        mDataset.messagesArray.add(0, msg);
    }
}

