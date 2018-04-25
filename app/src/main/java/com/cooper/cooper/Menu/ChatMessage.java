package com.cooper.cooper.Menu;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Javi on 3/28/2018.
 */

public class ChatMessage {

    private String author;
    private String id;
    private String message;
    private String time;
    private int userid;

    public ChatMessage(String author, String id, String message, int userid) {
        this.author = author;
        this.id = id;
        this.message = message;
        this.userid = userid;

        // Initialize to current time
        //time = new Date().getTime();
        //time = DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
        //message.getTime()));
        time = new SimpleDateFormat("d/M/yyyy H:mm:ss").format(new Date());
        //time = DateFormat.format("dd-MM-yyyy (HH:mm:ss)", new Date().getTime());
    }

    public ChatMessage(){

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

}
