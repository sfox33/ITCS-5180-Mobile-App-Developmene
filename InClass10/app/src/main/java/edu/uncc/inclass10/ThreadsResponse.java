package edu.uncc.inclass10;

import java.util.ArrayList;

/**
 * Created by Andrew Lambropoulos on 2/19/2018.
 * Created by Sean Fox
 * MainActivity.java
 * Assignment: InClass10
 */

public class ThreadsResponse {
    ArrayList<ThreadObject> threads = new ArrayList<>();
    String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
