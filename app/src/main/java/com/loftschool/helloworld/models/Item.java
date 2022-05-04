package com.loftschool.helloworld.models;

import com.loftschool.helloworld.remote.MoneyRemoteItem;

public class Item {
    private String name;
    private int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public static Item getInstance(MoneyRemoteItem moneyRemoteItem) {
        int price = (int) moneyRemoteItem.getPrice();
        return new Item(moneyRemoteItem.getName(), price);
    }
}
