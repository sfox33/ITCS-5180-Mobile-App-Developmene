package edu.uncc.inclass12v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sean on 4/16/2018.
 */

public class ThreadObject implements Serializable {
    String user_name;
    String user_id;
    String id;
    String title;
    Map<String, MessageObject> messages;      //Turn into a hashmap?
    ArrayList<MessageObject> messagesArray;

    @Override
    public String toString() {
        return "ThreadObject{" +
                "user_name='" + user_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", title='" + title + '\'' +
                ", messages=" + messages +
                '}';
    }

    public ThreadObject() {
        user_name = user_id = title = "";
        messages = new HashMap<>();
        messagesArray = this.toArray();
    }

    public ThreadObject(String user_name, String user_id, String title, HashMap<String, MessageObject> messages) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.title = title;
        this.messages = messages;
        this.messagesArray = this.toArray();
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void updateArray() {
        messagesArray = this.toArray();
    }

    public ArrayList<MessageObject> toArray() {
        ArrayList<MessageObject> val = new ArrayList<>();
        for(MessageObject msg : messages.values()) {
            val.add(msg);
        }
        return val;
    }
}
