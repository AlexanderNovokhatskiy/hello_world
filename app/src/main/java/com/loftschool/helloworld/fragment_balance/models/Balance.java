package com.loftschool.helloworld.fragment_balance.models;


import com.loftschool.helloworld.remote.BalanceResponse;

public class Balance {
    private final int totalExpenses;
    private final int totalIncomes;
    private final int availableAmount;

    public Balance(int totalExpenses, int totalIncomes, int availableAmount) {
        this.totalExpenses = totalExpenses;
        this.totalIncomes = totalIncomes;
        this.availableAmount = availableAmount;
    }

    public int getTotalExpenses() {
        return totalExpenses;
    }

    public int getTotalIncomes() {
        return totalIncomes;
    }

    public int getAvailableAmount() {
        return availableAmount;
    }

    public static Balance getInstance(BalanceResponse balanceResponse) {
        int expenses = Math.round(balanceResponse.getTotalExpenses());
        int incomes = Math.round(balanceResponse.getTotalIncomes());
        int available = incomes - expenses;
        return new Balance(expenses, incomes, available);
    }
}
