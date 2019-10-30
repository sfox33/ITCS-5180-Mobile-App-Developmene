package edu.uncc.inclass11;

import java.util.ArrayList;

/**
 * Created by Andrew Lambropoulos on 4/9/2018.
 * Created by Sean Fox
 * Post.java
 * Assignment: InClass11
 */
public class Post {
    ArrayList<Image> images = new ArrayList<>();
    String link;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }
}
