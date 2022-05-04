package com.loftschool.helloworld;

import static android.app.Activity.RESULT_OK;

import static com.loftschool.helloworld.AddItemActivity.KEY_AMOUNT;
import static com.loftschool.helloworld.AddItemActivity.KEY_NAME;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loftschool.helloworld.models.Item;
import com.loftschool.helloworld.remote.MoneyRemoteItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class BudgetFragment extends Fragment {

    private static final int REQUEST_CODE_ADD_ITEM = 100;
    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";

    private ItemsAdapter moneyCellAdaper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadItems();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        FloatingActionButton addButton = view.findViewById(R.id.add_button);
        addButton.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(getActivity(), AddItemActivity.class), REQUEST_CODE_ADD_ITEM));

        // Находим контейнер для нашего списка
        RecyclerView recyclerView = view.findViewById(R.id.itemsView);

        // Проверяем, не забыли ли положить аргументы при создании фрагмента
        if (getArguments() != null) {
            // Устанавливаем цвет для который положили в аргументы при создании фрагмента
            moneyCellAdaper = new ItemsAdapter(getArguments().getInt(COLOR_ID));
        } else {
            //
            moneyCellAdaper = new ItemsAdapter(R.color.purple_500);
        }
        // Устанавливаем адаптер для списка
        recyclerView.setAdapter(moneyCellAdaper);


        // Тестовые данные
/*        moneyCellAdaper.addItem(new Item("Coffee", 300));
        moneyCellAdaper.addItem(new Item("Tea", 100));*/

    }

    // Обрабатываем результат из AddItemActivity
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Проверяем код успеха и ADD_ITEM_REQUEST_CODE (по коду этого запроса понимаем что именно добавление обрабатываем в данном if)
        if (requestCode == REQUEST_CODE_ADD_ITEM && resultCode == RESULT_OK) {
            if (data == null) return;
            // Добавляем в адаптер элемент который только что заполняли в AddItemActivity
/*                moneyCellAdaper.addItem(
                        new Item(
                                data.getStringExtra(KEY_NAME),
                                Integer.parseInt(data.getStringExtra(KEY_AMOUNT))
                        )
                );*/
            Context appContext = getActivity().getApplicationContext();
            Disposable disposable = ((LoftApp) getActivity().getApplication()).moneyApi.postMoney(
                    Integer.parseInt(data.getStringExtra(KEY_AMOUNT)),
                    data.getStringExtra(KEY_NAME),
                    "income"
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        Toast.makeText(appContext, getString(R.string.success_added), Toast.LENGTH_LONG).show();
                    }, throwable -> {
                        Toast.makeText(appContext, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }

    private void loadItems() {
        Context appContext = getActivity().getApplicationContext();
        Disposable disposable = ((LoftApp) getActivity().getApplication()).moneyApi.getMoneyItems("income")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(moneyResponse -> {
                    if (moneyResponse.getStatus().equals("success")) {
                        List<Item> moneyItems = new ArrayList<>();

                        for (MoneyRemoteItem moneyRemoteItem : moneyResponse.getMoneyItemsList()) {
                            moneyItems.add(Item.getInstance(moneyRemoteItem));
                        }

                        moneyCellAdaper.setData(moneyItems);
                    } else {
                        Toast.makeText(appContext, getString(R.string.connection_lost), Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Toast.makeText(appContext, throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });

        compositeDisposable.add(disposable);
    }
}
