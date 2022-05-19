package com.loftschool.helloworld.fragment_budget;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.helloworld.R;
import com.loftschool.helloworld.fragment_budget.models.MoneyItem;

import java.util.ArrayList;
import java.util.List;

// Это адаптер. Он нужен для RecyclerView.
// Он управляет списком. Т.е. добавляет, удаляем элементы.
public class MoneyItemsAdapter extends RecyclerView.Adapter<MoneyItemsAdapter.MoneyViewHolder> {
    // Этим списком управляет адаптер
    private final List<MoneyItem> itemsList = new ArrayList<>();

    // Цвет "стоимости" дохода расхода
    private final int colorId;

    private MoneyItemAdapterClick moneyCellAdapterClick;

    public MoneyItemsAdapter(int colorId) {
        this.colorId = colorId;
    }

    public List<MoneyItem> getMoneyItemList() {
        return itemsList;
    }

    public void updateItem(MoneyItem moneyItem) {
        int itemPosition = itemsList.indexOf(moneyItem);
        itemsList.set(itemPosition, moneyItem);
        notifyItemChanged(itemPosition);
    }

    // Просто добавляем наши элементы в список, которым управляет адаптер
    public void setData(List<MoneyItem> items) {
        itemsList.clear();
        itemsList.addAll(items);
        // Говорим адаптеру, что список изменился, чтобы он отобразил актуальный список
        notifyDataSetChanged();
    }

    public void deleteSelectedItems() {
        List<MoneyItem> selectedItems = new ArrayList<>();
        for (MoneyItem moneyItem : itemsList) {
            if (moneyItem.isSelected()) {
                selectedItems.add(moneyItem);
            }
        }

        itemsList.removeAll(selectedItems);
        notifyDataSetChanged();
    }

    public void setMoneyCellAdapterClick(MoneyItemAdapterClick moneyCellAdapterClick) {
        this.moneyCellAdapterClick = moneyCellAdapterClick;
    }

    // "Показываем" адаптеру как должна выглядить разметка одного элемента списка
    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View itemView = View.inflate(parent.getContext(), R.layout.item_money, null);
        return new MoneyViewHolder(itemView, colorId, moneyCellAdapterClick);
    }

    // Вносим данные из кода в элемент, который будет отображаться на экрана
    @Override
    public void onBindViewHolder(@NonNull final MoneyViewHolder holder, final int position) {
        // Вносим данные из кода в элемент, который будет отображаться на экрана
        holder.bindItem(itemsList.get(position));
    }


    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    // Класс, который содержит элементы разметки нашего одного элемента,
    // в нем устанавливаются реальные данные
    static class MoneyViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;
        private final TextView mAmountView;
        private MoneyItemAdapterClick moneyItemAdapterClick;

        public MoneyViewHolder(@NonNull final View itemView, int colorId, MoneyItemAdapterClick moneyItemAdapterClick) {
            super(itemView);
            // Находим элементы
            mTextView = itemView.findViewById(R.id.itemNameView);
            mAmountView = itemView.findViewById(R.id.itemValueView);
            // Устанавливаем цвет для значения дохода или расхода
            mAmountView.setTextColor(ContextCompat.getColor(mAmountView.getContext(), colorId));

            this.moneyItemAdapterClick = moneyItemAdapterClick;
        }

        public void bindItem(@NonNull final MoneyItem item) {
            // Вносим данные в элементы
            mTextView.setText(item.getName());
            mAmountView.setText(String.valueOf(item.getPrice()) + " ₽");

            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                    item.isSelected() ? R.color.primary_color_second : android.R.color.white));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (moneyItemAdapterClick != null) {
                        moneyItemAdapterClick.onCellClick(item);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (moneyItemAdapterClick != null) {
                        moneyItemAdapterClick.onLongCellClick(item);
                    }
                    return true;
                }
            });
        }
    }
}