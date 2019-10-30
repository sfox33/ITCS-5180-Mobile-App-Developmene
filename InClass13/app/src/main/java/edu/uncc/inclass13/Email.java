package edu.uncc.inclass13;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author Sean Fox & Andrew Lambropoulos
 * File Name: Email.java
 * Assignment #13
 */
public class Email implements Serializable, Comparable<Email> {
    private String author, body, emailId, createdAt;
    private boolean isRead;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Email() {
        author = body = emailId = "";
        createdAt = new Date().toString();
        isRead = false;
    }

    @Override
    public String toString() {
        return "Email{" +
                "author='" + author + '\'' +
                ", body='" + body + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", emailId='" + emailId + '\'' +
                ", isRead=" + isRead +
                '}';
    }

    public Email(String author, String body, Date createdAt, String emailId, boolean isRead) {
        this.author = author;
        this.body = body;
        this.createdAt = createdAt.toString();
        this.emailId = emailId;
        this.isRead = isRead;
    }

    public Email(String author, String body) {
        this.author = author;
        this.body = body;
        this.createdAt = new Date().toString();
        this.isRead = false;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() { return createdAt; }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getDateForm() {
        DateFormat df = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy", Locale.US);
        try {
            return df.parse(createdAt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    @Override
    public int compareTo(@NonNull Email o) {
        return this.getDateForm().compareTo(o.getDateForm());
    }
}
