package edu.uncc.inclass10;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Andrew Lambropoulos on 2/19/2018.
 * Created by Sean Fox
 * MainActivity.java
 * Assignment: InClass10
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private ThreadsResponse mDataset;
    private SharedPreferences sharedPref;
    public MessagesActivity act;
    private Handler handler;
    private OnThreadClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageButton deleteButton;
        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.messageText);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
        }

        public void bind(final ThreadObject threadObject, final OnThreadClickListener onThreadClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onThreadClickListener.onThreadClick(threadObject);
                }
            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MessagesAdapter(ThreadsResponse myDataset, MessagesActivity act, OnThreadClickListener listener) {
        mDataset = myDataset;
        sharedPref = act.getSharedPreferences("InClass10", Context.MODE_PRIVATE);
        this.act = act;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;

        holder.bind(mDataset.threads.get(position), listener);

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                mDataset.threads.remove(msg.arg1);
                notifyDataSetChanged();
                return false;
            }
        });

        //Determines if the thread gets a "Delete" button
        if(!sharedPref.getString("UserID", "").equals(mDataset.threads.get(pos).getUser_id())) {
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
                            .url("http://ec2-54-91-96-147.compute-1.amazonaws.com/api/thread/delete/" + mDataset.threads.get(pos).getId())
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

                            Message message = new Message();
                            message.what = 2;
                            message.obj = status;
                            message.arg1 = pos;
                            if(status.status.equals("ok")) {
                                Message msg = new Message();
                                msg.arg1 = pos;
                                handler.sendMessage(msg);
                            }
                            act.handler.sendMessage(message);
                        }
                    });
                }
            });
        }
        holder.mTextView.setText(mDataset.threads.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.threads.size();
    }

    public void updateData(ThreadObject to) {
        Log.d("demo", "About to updated.  Check: " + to.getUser_id());
        mDataset.threads.add(0, to);
        //notifyItemRangeChanged(0, mDataset.threads.size()-1);
        notifyDataSetChanged();
        Log.d("demo", "Yo, this piece of shit is working?");
    }
}
