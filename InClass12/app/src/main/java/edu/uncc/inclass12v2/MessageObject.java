package edu.uncc.inclass12v2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sean on 4/16/2018.
 */

public class MessageObject implements Serializable {
    String user_name;
    String user_id;
    String id;
    String message;
    String created_at;

    public MessageObject() {
        user_name = user_id = id = message = created_at = "";
        //created_at = Calendar.getInstance().getTime();
    }

    public MessageObject(String user_name, String user_id, String message, String created_at) {
        this.user_name = user_name;
        this.user_id = user_id;
        this.message = message;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "MessageObject{" +
                "user_name='" + user_name + '\'' +
                ", user_id='" + user_id + '\'' +
                ", id='" + id + '\'' +
                ", message='" + message + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
