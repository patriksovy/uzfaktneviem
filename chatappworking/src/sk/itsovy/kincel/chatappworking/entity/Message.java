package sk.itsovy.kincel.chatappworking.entity;

import java.util.Date;

public class Message {
    private int idMsg;
    private String from;
    private String to;
    private Date date;
    private String msg;

    public Message(int idMsg, String from, String to, Date date, String msg) {
        this.idMsg = idMsg;
        this.from = from;
        this.to = to;
        this.date = date;
        this.msg = msg;
    }
    public int getId() {
        return idMsg;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public Date getDt() {
        return date;
    }

    public String getText() {
        return msg;
    }
}