package com.example.myapplication.Usermodel;

public class Chat {

    private String sender_id;
    private String sender_nick;
    private String receiver;
    private String msg;
    private String time;
    private String participant;
    private String view_time;

    public Chat(String sender_id,String sender_nick, String receiver, String msg,String time,String participant,String view_time) {
        this.sender_id = sender_id;
        this.sender_nick = sender_nick;
        this.receiver = receiver;
        this.msg = msg;
        this.time = time;
        this.participant = participant;
        this.view_time = view_time;
    }
    public Chat() {
    }

    public String getSender_id() {return sender_id;}

    public String getParticipant(){return participant;}

    public void setSender_id(String sender) {
        this.sender_id = sender;
    }

    public String getSender_nick() {
        return sender_nick;
    }

    public void setSender_nick(String sender) {
        this.sender_nick = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime(){return time;}

    public String getView_time(){return view_time;}
}
