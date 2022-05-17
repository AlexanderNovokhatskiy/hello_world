package com.loftschool.helloworld.remote;

public class MoneyRemoteItem {
    private String date;
    private int id;
    private String name;
    private int price;
    private String type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }
}
