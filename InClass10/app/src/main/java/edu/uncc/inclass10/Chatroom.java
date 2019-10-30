package edu.uncc.inclass10;

import java.util.ArrayList;

import edu.uncc.inclass10.MessageObject;
import edu.uncc.inclass10.ThreadObject;

/**
 * Created by Sean on 4/7/2018.
 */

public class Chatroom {
    String status;
    ArrayList<MessageObject> messages = new ArrayList<>();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MessageObject> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<MessageObject> messages) {
        this.messages = messages;
    }
}
