package com.finix.kisiiattend;

public class Attend {
    String id, date, name;

    public Attend() {
    }


    public Attend(String id, String date) {
        this.id = id;
        this.date = date;
    }

    public Attend(String id, String date, String name) {
        this.id = id;
        this.date = date;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
