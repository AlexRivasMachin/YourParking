package com.lksnext.arivas.domain;

public class Reservation {
    private String uid;
    private String slotId;
    private String date;
    private String in;
    private String out;
    private String type;

    public Reservation() {
    }

    public Reservation(String uid, String slotId, String date, String in, String out, String type) {
        this.uid = uid;
        this.slotId = slotId;
        this.date = date;
        this.in = in;
        this.out = out;
        this.type = type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSlotId() {
        return slotId;
    }

    public void setSlotId(String slotId) {
        this.slotId = slotId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn() {
        return in;
    }

    public void setIn(String in) {
        this.in = in;
    }

    public String getOut() {
        return out;
    }

    public void setOut(String out) {
        this.out = out;
    }

    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
}