package com.loftschool.helloworld.remote;


import com.google.gson.annotations.SerializedName;

//{"status":"success","total_expenses":"123.23", “total_income”:”400”}
public class BalanceResponse {
    private String status;
    @SerializedName("total_expenses")
    private Float totalExpenses;
    @SerializedName("total_income")
    private Float totalIncomes;

    public String getStatus() {
        return status;
    }

    public Float getTotalExpenses() {
        return totalExpenses;
    }

    public Float getTotalIncomes() {
        return totalIncomes;
    }
}
