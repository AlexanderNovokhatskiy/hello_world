package com.loftschool.helloworld.fragment_balance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.loftschool.helloworld.LoftApp;
import com.loftschool.helloworld.R;

public class BalanceFragment extends Fragment {
    private TextView expensesTextView;
    private TextView balanceTextView;
    private TextView incomesTextView;
    private BalanceView balanceView;

    private BalanceViewModel balanceViewModel;

    public static BalanceFragment newInstance() {
        return new BalanceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_balance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViews(view);
        configureViewModel();
    }

    private void configureViewModel() {
        balanceViewModel = new ViewModelProvider(this).get(BalanceViewModel.class);
        balanceViewModel.balance.observe(getViewLifecycleOwner(), balance -> {
            expensesTextView.setText(String.valueOf(balance.getTotalExpenses()));
            incomesTextView.setText(String.valueOf(balance.getTotalIncomes()));
            balanceTextView.setText(String.valueOf(
                    balance.getTotalIncomes() - balance.getTotalExpenses()
            ));
            if (!expensesTextView.getText().toString().isEmpty()
                    || !incomesTextView.getText().toString().isEmpty()
                    || !balanceTextView.getText().toString().isEmpty()) {
                balanceView.update(Integer.parseInt(expensesTextView.getText().toString()), Integer.parseInt(incomesTextView.getText().toString()));
            }
        });

        balanceViewModel.messageString.observe(getViewLifecycleOwner(), message -> {
            if (!message.equals("")) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        balanceViewModel.messageInt.observe(getViewLifecycleOwner(), message -> {
            if (message > 0) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadBalance();
    }

    private void configureViews(View view) {
        expensesTextView = view.findViewById(R.id.tv_expenses_value);
        incomesTextView = view.findViewById(R.id.tv_incomes_value);
        balanceTextView = view.findViewById(R.id.txtBalanceFinanceValue);
        balanceView = view.findViewById(R.id.balanceView);
    }


    private void loadBalance() {
        balanceViewModel.loadBalance(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0)
        );
    }
}