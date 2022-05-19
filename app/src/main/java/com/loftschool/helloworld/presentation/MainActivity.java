package com.loftschool.helloworld.presentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.loftschool.helloworld.AddItemActivity;
import com.loftschool.helloworld.R;
import com.loftschool.helloworld.fragment_balance.BalanceFragment;
import com.loftschool.helloworld.fragment_budget.BudgetFragment;
import com.loftschool.helloworld.fragment_budget.MoneyEditListener;
import com.loftschool.helloworld.presentation.EditModeListener;

public class MainActivity extends AppCompatActivity implements EditModeListener {

    private int currentFragmentPosition = 0;

    private Toolbar toolbar;
    private ImageView actionBack;
    private ImageView actionDelete;
    private TabLayout tabLayout;
    private TextView actionTitle;
    private ViewPager2 viewPager;
    private FloatingActionButton addButton;

    private static final int incomeFragmentPosition = 0;
    private static final int expenseFragmentPosition = 1;
    private static final int balanceFragmentPosition = 2;

    // Один из методов жц активити
    // Здесь находим элементы из нашей верстки и навешиваем всяких setOnClickListener и подобное
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // устанавливаем нашу разметку для этой активити
        setContentView(R.layout.activity_main);

        // находим вью наших "вкладок"
        TabLayout tabLayout = findViewById(R.id.tabs);

        // инициализуруем пейджер, с помощью него будет листать фрагменты
        viewPager = findViewById(R.id.viewpager);
        // Устанавливаем адаптер, он будет управлять списком наших фрагментов
        viewPager.setAdapter(new ViewPagerFragmentAdapter(this));


        configureActionMode();
        // Находим кнопку
        addButton = findViewById(R.id.add_button);
        // Навешиваем на кнопку листенера для запуска активити добавления элемента в список
        Intent intent = new Intent(this, AddItemActivity.class);
        addButton.setOnClickListener(v -> {
            String type = "0";
            switch (currentFragmentPosition) {
                case incomeFragmentPosition:
                    type = "income";
                    break;
                case expenseFragmentPosition:
                    type = "expense";
                    break;
            }
            intent.putExtra(BudgetFragment.TYPE, type);
            startActivity(intent);
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                closeEditMode();
                currentFragmentPosition = position;
                if (position == balanceFragmentPosition) {
                    addButton.hide();
                } else {
                    addButton.show();
                }
            }
        });

        //Здесь просто перечислим наши вкладки
        final String[] fragmentsTitles = new String[]{getString(R.string.incomes), getString(R.string.expenses), getString(R.string.balance)};

        // Настраиваем наши вкладки, устанавливаем в них текст
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(fragmentsTitles[position]);
            }
        }).attach();

        viewPager.setOffscreenPageLimit(2);
    }

    private void configureActionMode() {
        toolbar = findViewById(R.id.toolbarView);
        actionBack = findViewById(R.id.backButtonView);
        actionDelete = findViewById(R.id.dashboardActionView);
        actionTitle = findViewById(R.id.dashboardTitleView);

        actionBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeEditMode();
            }
        });

        actionDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle(getString(R.string.delete_items_title))
                        .setMessage(getString(R.string.delete_items_message))
                        .setNegativeButton(R.string.action_no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setPositiveButton(R.string.action_yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                clearSelectedItems();
                            }
                        })
                        .show();
            }
        });


        // находим вью наших "вкладок"
        tabLayout = findViewById(R.id.tabs);
    }

    @Override
    public void onEditModeChanged(boolean status) {
        if (status) {
            addButton.hide();
        } else {
            addButton.show();
        }

        toolbar.setBackgroundColor(ContextCompat.getColor(this,
                status ? R.color.primary_color_second : R.color.primary_color));
        actionDelete.setVisibility(status ? View.VISIBLE : View.GONE);
        actionBack.setVisibility(status ? View.VISIBLE : View.GONE);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this,
                status ? R.color.primary_color_second : R.color.primary_color));

        Window window = getWindow();

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, status ? R.color.primary_color_second : R.color.primary_color));
    }

    @Override
    public void onCounterChanged(int newCount) {
        if (newCount >= 0) {
            actionTitle.setText(getString(R.string.selected, newCount));
        } else {
            actionTitle.setText(getString(R.string.budget_accounting));
        }
    }

    private void closeEditMode() {
        Fragment fragment = getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
        if (fragment instanceof MoneyEditListener) {
            ((MoneyEditListener) fragment).onClearEdit();
        }
    }

    private void clearSelectedItems() {
        Fragment fragment = getSupportFragmentManager().getFragments().get(viewPager.getCurrentItem());
        if (fragment instanceof MoneyEditListener) {
            ((MoneyEditListener) fragment).onClearSelectedClick();
        }
    }

    // Это обычный адаптер для управления списком, мы создавали адаптер раньше для RecyclerView
    private class ViewPagerFragmentAdapter extends FragmentStateAdapter {

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
                case incomeFragmentPosition:
                    return BudgetFragment.newInstance(R.color.income_color, getString(R.string.income));
                case expenseFragmentPosition:
                    return BudgetFragment.newInstance(R.color.expense_color, getString(R.string.expense));
                case balanceFragmentPosition:
                    return BalanceFragment.newInstance();
                default:
                    return null;
            }
        }


        @Override
        public int getItemCount() {
            return 3; // здесь указываем сколько у нас фрагментов
        }
    }

}