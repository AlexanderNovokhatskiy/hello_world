package com.loftschool.helloworld.fragment_budget;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.loftschool.helloworld.LoftApp;
import com.loftschool.helloworld.R;

import com.loftschool.helloworld.presentation.EditModeListener;
import com.loftschool.helloworld.fragment_budget.models.MoneyItem;

public class BudgetFragment extends Fragment implements MoneyEditListener {

    private static final int REQUEST_CODE_ADD_ITEM = 100;
    private static final String COLOR_ID = "colorId";
    public static final String TYPE = "fragmentType";

    private MoneyItemsAdapter moneyCellAdaper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String type;
    private BudgetViewModel budgetViewModel;

    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        budgetViewModel = new ViewModelProvider(this).get(BudgetViewModel.class);
        // Проверяем, не забыли ли положить аргументы при создании фрагмента
        if (getArguments() != null) {
            // Устанавливаем цвет для который положили в аргументы при создании фрагмента
            moneyCellAdaper = new MoneyItemsAdapter(getArguments().getInt(COLOR_ID));
            type = getArguments().getString(TYPE); //getString(getArguments().getInt(TYPE));
        } else {
            moneyCellAdaper = new MoneyItemsAdapter(R.color.purple_500);
        }
        moneyCellAdaper.setMoneyCellAdapterClick(new MoneyItemAdapterClick() {
            @Override
            public void onCellClick(MoneyItem moneyItem) {
                if (budgetViewModel.isEditMode.getValue()) {
                    moneyItem.setSelected(!moneyItem.isSelected());
                    moneyCellAdaper.updateItem(moneyItem);
                    checkSelectedCount();
                }
            }

            @Override
            public void onLongCellClick(MoneyItem moneyItem) {
                if (!budgetViewModel.isEditMode.getValue()) {
                    moneyItem.setSelected(true);
                    moneyCellAdaper.updateItem(moneyItem);
                    budgetViewModel.setEditMode(true);
                    checkSelectedCount();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configureViewModel();

        configureViews(view);

    }

    private void checkSelectedCount() {
        int selectedItemsCount = 0;
        for (MoneyItem moneyItem : moneyCellAdaper.getMoneyItemList()) {
            if (moneyItem.isSelected()) {
                selectedItemsCount++;
            }
        }

        budgetViewModel.setSelectedItemsCount(selectedItemsCount);
    }

    private void configureViews(View view) {
        // Находим контейнер для нашего списка
        RecyclerView recyclerView = view.findViewById(R.id.itemsView);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            loadItems();
        });

        // Устанавливаем адаптер для списка
        recyclerView.setAdapter(moneyCellAdaper);
    }

    private void configureViewModel() {

        budgetViewModel.isEditMode.observe(getViewLifecycleOwner(), isEditMode -> {
            if (getActivity() instanceof EditModeListener) {
                ((EditModeListener) getActivity()).onEditModeChanged(isEditMode);
            }
        });

        budgetViewModel.selectedCounter.observe(getViewLifecycleOwner(), newCount -> {
            if (getActivity() instanceof EditModeListener) {
                ((EditModeListener) getActivity()).onCounterChanged(newCount);
            }
        });

        budgetViewModel.removeItemDoneSuccess.observe(getViewLifecycleOwner(), success -> {
            if (success) {
                loadItems();
            }
        });

        budgetViewModel.moneyItemsList.observe(getViewLifecycleOwner(), moneyItems -> {
            moneyCellAdaper.setData(moneyItems);
        });

        budgetViewModel.messageString.observe(getViewLifecycleOwner(), message -> {
            if (!message.equals("")) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        budgetViewModel.messageInt.observe(getViewLifecycleOwner(), message -> {
            if (message > 0) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });

        budgetViewModel.isRefreshing.observe(getViewLifecycleOwner(), isRefreshing -> {
            mSwipeRefreshLayout.setRefreshing(isRefreshing);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        loadItems();
    }

    private void loadItems() {
        budgetViewModel.loadIncomes(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0),
                type
        );
    }

    @Override
    public void onClearEdit() {
        budgetViewModel.setEditMode(false);
        budgetViewModel.resetSelectedCounter();

        for (MoneyItem moneyItem : moneyCellAdaper.getMoneyItemList()) {
            if (moneyItem.isSelected()) {
                moneyItem.setSelected(false);
                moneyCellAdaper.updateItem(moneyItem);
            }
        }
    }

    @Override
    public void onClearSelectedClick() {
        budgetViewModel.setEditMode(false);
        budgetViewModel.resetSelectedCounter();
        budgetViewModel.removeItem(
                ((LoftApp) getActivity().getApplication()).moneyApi,
                getActivity().getSharedPreferences(getString(R.string.app_name), 0),
                moneyCellAdaper.getMoneyItemList()
        );
    }
}
