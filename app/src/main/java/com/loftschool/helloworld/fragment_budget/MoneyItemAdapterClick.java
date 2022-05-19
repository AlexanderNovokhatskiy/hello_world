package com.loftschool.helloworld.fragment_budget;

import com.loftschool.helloworld.fragment_budget.models.MoneyItem;

public interface MoneyItemAdapterClick {
    void onCellClick(MoneyItem moneyItem);
    void onLongCellClick(MoneyItem moneyItem);
}
