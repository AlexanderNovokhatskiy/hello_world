package com.loftschool.helloworld;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.loftschool.helloworld.models.Item;
import com.loftschool.helloworld.remote.MoneyRemoteItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    //    private RecyclerView itemsView;
//    private ItemsAdapter moneyCellAdaper = new ItemsAdapter(R.color.purple_500);
//    public static final int REQUEST_CODE_ADD_ITEM = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // находим вью наших "вкладок"
        TabLayout tabLayout = findViewById(R.id.tabs);

        // инициализуруем пейджер, с помощью него будет листать фрагменты
        ViewPager2 viewPager = findViewById(R.id.viewpager);
        // Устанавливаем адаптер, он будет управлять списком наших фрагментов
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));

        //Здесь просто перечислим наши вкладки
        final String[] fragmentsTitles = new String[]{getString(R.string.incomes), getString(R.string.expenses)};

        // Настраиваем наши вкладки, устанавливаем в них текст
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(fragmentsTitles[position]);
            }
        }).attach();
    }

/*    @Override
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
    }*/

    // Это обычный адаптер для управления списком, мы создавали адаптер раньше для RecyclerView
    public class ViewPagerFragmentAdapter extends FragmentStateAdapter {

        // Указываем конструктор для нашего адаптера
        public ViewPagerFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        // Этот метод будет вызываться каждый раз когда мы будем переключать вкладки.
        // Тут мы указываем на какой фрагмент нам стоит переключиться на i-ой вкладке(счёт с нуля)
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return BudgetFragment.newInstance(R.color.income_color, getString(R.string.incomes));
                case 1:
                    return BudgetFragment.newInstance(R.color.expense_color, getString(R.string.expenses));
                case 2:
                    // Тут будет ещё фрагмент
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() {
            return 2; // здесь указываем сколько у нас фрагментов
        }
    }
}