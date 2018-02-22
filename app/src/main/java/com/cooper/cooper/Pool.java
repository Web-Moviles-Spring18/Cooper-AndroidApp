package com.cooper.cooper;

import java.util.ArrayList;

/**
 * Created by ASUS on 22/02/2018.
 */

public class Pool {

    private String name;
    private double total;
    private boolean is_private;
    private String payment_method;
    private String currency;
    private String start_date;
    private String end_date;
    private ArrayList<String> members;

    public Pool(String name, double total, boolean is_private, String payment_method, String currency, String start_date, String end_date, ArrayList<String> members) {
        this.name = name;
        this.total = total;
        this.is_private = is_private;
        this.payment_method = payment_method;
        this.currency = currency;
        this.start_date = start_date;
        this.end_date = end_date;
        this.members = members;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public boolean isIs_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public int getQuantityMember(){
        return this.members.size();
    }
}
