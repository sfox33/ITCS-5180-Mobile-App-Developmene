package edu.uncc.inclass13;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * @author Sean Fox & Andrew Lambropoulos
 * File Name: EmailAdapter.java
 * Assignment #13
 */
public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.ViewHolder> {
    ArrayList<Email> emails;
    private FirebaseAuth mAuth;
    private OnEmailClickListener listener;

    public EmailAdapter(ArrayList<Email> myDataset, FirebaseAuth auth, OnEmailClickListener emailClickListener) {
        this.emails = myDataset;
        this.mAuth = auth;
        this.listener = emailClickListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView emailAuthor, emailTime, emailBody;
        public ImageButton seentItButton;
        public ViewHolder(View v) {
            super(v);
            Log.d("demo", "Creating view holder");
            emailAuthor = (TextView) v.findViewById(R.id.author);
            emailTime = (TextView) v.findViewById(R.id.time);
            emailBody = (TextView) v.findViewById(R.id.body);
            seentItButton = (ImageButton) v.findViewById(R.id.seenButton);
        }

        public void bind(final Email email, final OnEmailClickListener onThreadClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onThreadClickListener.onEmailClick(email);
                }
            });
        }
    }

    @Override
    public EmailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.email_layout, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int pos = position;
        holder.bind(emails.get(position), listener);

        //Determines if the thread gets a "Delete" button
        if(emails.get(pos).getIsRead()) {
            Log.d("demo", "Has been read");
            holder.seentItButton.setColorFilter(Color.GRAY);
        } else {
            Log.d("demo", "Has not been read");
            holder.seentItButton.setColorFilter(Color.BLUE);
        }
        holder.seentItButton.setBackgroundColor(Color.WHITE);
        Log.d("demo", "Should be setting email info.  Example: Author is " + emails.get(pos).getAuthor());
        holder.emailAuthor.setText(emails.get(pos).getAuthor());
        holder.emailBody.setText(emails.get(pos).getBody());
        //"Wed Apr 25 16:31:06 EDT 2018"
        DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
        try {
            Log.d("demo", "PARSING DATE: " + emails.get(pos).getCreatedAt());
            Date date = df.parse(emails.get(pos).getCreatedAt());
            Log.d("demo", date.toString());
            df = new SimpleDateFormat("MM/d/yy, h:mm a");
            holder.emailTime.setText(df.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return emails.size();
    }
}
