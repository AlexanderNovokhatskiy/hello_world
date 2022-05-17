package com.loftschool.helloworld;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.loftschool.helloworld.fragment_budget.BudgetFragment;

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
                return BudgetFragment.newInstance(R.color.income_color, "income");
            case 1:
                return BudgetFragment.newInstance(R.color.expense_color, "expense");
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