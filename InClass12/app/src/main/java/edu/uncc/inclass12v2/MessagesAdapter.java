package edu.uncc.inclass12v2;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Sean on 4/16/2018.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private ArrayList<ThreadObject> mDataset;
    private FirebaseAuth mAuth;
    private OnThreadClickListener listener;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("threads");

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ImageButton deleteButton;
        public ViewHolder(View v) {
            super(v);
            Log.d("demo", "Creating view holder");
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
    public MessagesAdapter(ArrayList<ThreadObject> myDataset, FirebaseAuth auth, OnThreadClickListener listener) {
        Log.d("demo", "Adapter creation. Dataset has size " + myDataset.size());
        mDataset = myDataset;
        this.mAuth = auth;
        this.listener = listener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("demo", "Adapter create view holder");
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        holder.bind(mDataset.get(position), listener);

        //Determines if the thread gets a "Delete" button
        Log.d("demo", "Comparing values: " + mAuth.getCurrentUser().getUid() + " and " + mDataset.get(pos).getUser_id() + " at position " + pos);
        if(!mAuth.getCurrentUser().getUid().equals(mDataset.get(pos).getUser_id())) {
            holder.deleteButton.setEnabled(false);
            holder.deleteButton.setVisibility(View.GONE);
        } else {
            holder.deleteButton.setEnabled(true);
            holder.deleteButton.setVisibility(View.VISIBLE);
            //If the thread gets a delete button, set a listener
            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseDatabase.getInstance().getReference("threads/"+ mDataset.get(pos).getId()).removeValue();
                    mDataset.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }
        holder.mTextView.setText(mDataset.get(position).getTitle());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public void updateData(ThreadObject to) {
        mDataset.add(0, to);
        notifyDataSetChanged();
    }
}
