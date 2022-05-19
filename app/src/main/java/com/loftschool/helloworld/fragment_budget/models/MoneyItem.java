package com.loftschool.helloworld.fragment_budget.models;

import com.loftschool.helloworld.remote.MoneyRemoteItem;

public class MoneyItem {
    private String name;
    private boolean isSelected;
    private int price;
    private int id;

    public MoneyItem(String name, int price, int id) {
        this.name = name;
        this.price = price;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public static MoneyItem getInstance(MoneyRemoteItem moneyRemoteItem) {
        int price = (int) moneyRemoteItem.getPrice();
        return new MoneyItem(moneyRemoteItem.getName(), price, moneyRemoteItem.getId());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
