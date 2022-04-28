package com.loftschool.helloworld;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView itemsView;
    private ItemsAdapter moneyCellAdaper = new ItemsAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configureRecyclerView();

/*        Intent intent = new Intent(this, AddItemActivity.class);
        startActivity(intent);*/

        generateMoney();
    }

    private void generateMoney() {
        List<Item> moneyItems = new ArrayList<>();
        moneyItems.add(new Item("PS4", 20000));
        moneyItems.add(new Item("PS5", 30000));

        moneyCellAdaper.setData(moneyItems);
    }

    private void configureRecyclerView() {
        itemsView = findViewById(R.id.itemsView);
        itemsView.setAdapter(moneyCellAdaper);
        itemsView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false));
    }
}