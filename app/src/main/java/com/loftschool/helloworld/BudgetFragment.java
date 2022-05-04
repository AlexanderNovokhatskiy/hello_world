package com.loftschool.helloworld;

import static android.app.Activity.RESULT_OK;

import static com.loftschool.helloworld.AddItemActivity.KEY_AMOUNT;
import static com.loftschool.helloworld.AddItemActivity.KEY_NAME;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.loftschool.helloworld.models.Item;


public class BudgetFragment extends Fragment {

    private static final int REQUEST_CODE_ADD_ITEM = 100;
    private static final String COLOR_ID = "colorId";
    private static final String TYPE = "fragmentType";

    private ItemsAdapter moneyCellAdaper;

    public static BudgetFragment newInstance(final int colorId, final String type) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(COLOR_ID, colorId);
        bundle.putString(TYPE, type);
        budgetFragment.setArguments(bundle);
        return budgetFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        moneyCellAdaper.addItem(new Item("Coffee", 300));
        moneyCellAdaper.addItem(new Item("Tea", 100));

    }


    // Обрабатываем результат из AddItemActivity
    @Override
    public void onActivityResult(final int requestCode, final int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Проверяем код успеха и ADD_ITEM_REQUEST_CODE (по коду этого запроса понимаем что именно добавление обрабатываем в данном if)
        if (requestCode == REQUEST_CODE_ADD_ITEM && resultCode == RESULT_OK) {
            if (data != null)
                // Добавляем в адаптер элемент который только что заполняли в AddItemActivity
                moneyCellAdaper.addItem(
                        new Item(
                                data.getStringExtra(KEY_NAME),
                                Integer.parseInt(data.getStringExtra(KEY_AMOUNT))
                        )
                );
        }
    }
}
