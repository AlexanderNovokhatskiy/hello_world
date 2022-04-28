package com.loftschool.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView itemsView;
    private ItemsAdapter moneyCellAdaper = new ItemsAdapter();
    public static final int REQUEST_CODE_ADD_ITEM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureRecyclerView();

        Intent intent = new Intent(this, AddItemActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD_ITEM);

        //generateMoney();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_ITEM && resultCode == Activity.RESULT_OK) {
            int expenseAmount = 0;
            try {
                expenseAmount = Integer.parseInt(data.getStringExtra("expense_amount"));
            } catch (NumberFormatException e) {
                expenseAmount = 0;
            }
            final String expenseName = data.getStringExtra("expense_name");
            generateMoney(expenseName, expenseAmount);
        }
    }

    private void generateMoney(String name, int price) {
        List<Item> moneyItems = new ArrayList<>();
        moneyItems.add(new Item("PS4", 20000));
        moneyItems.add(new Item("PS5", 30000));
        moneyItems.add(new Item(name, price));
        moneyCellAdaper.setData(moneyItems);
    }

    private void configureRecyclerView() {
        itemsView = findViewById(R.id.itemsView);
        itemsView.setAdapter(moneyCellAdaper);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        itemsView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(itemsView.getContext(), linearLayoutManager.getOrientation());
        itemsView.addItemDecoration(dividerItemDecoration);
    }
}