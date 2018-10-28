package com.megalatravels.duraivel.cabbooking;

public class ModelHistory {

    private String date;
    private String from;
    private String to;
    private String bid;
    private String cancel;



    public ModelHistory(String date, String from, String to, String bid, String cancel)
    {
       this.date =date;
       this.from =from;
       this.to=to;
       this.bid=bid;
       this.cancel=cancel;
    }
    public String getCancel() {
        return cancel;
    }

    public void setCancel(String cancel) {
        this.cancel = cancel;
    }
    public String getBid()
    {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }


}
