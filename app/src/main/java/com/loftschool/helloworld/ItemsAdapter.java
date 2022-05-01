package com.loftschool.helloworld;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.loftschool.helloworld.models.Item;

import java.util.ArrayList;
import java.util.List;


public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ItemViewHolder> {

    private List<Item> moneyItemList = new ArrayList<>();
    private final int colorId;

    public ItemsAdapter(int colorId) {
        this.colorId = colorId;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new ItemViewHolder(layoutInflater.inflate(R.layout.item, parent, false), colorId);
    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        holder.bind(moneyItemList.get(position));
    }

    @Override
    public int getItemCount() {
        return moneyItemList.size();
    }

    public void setData(List<Item> items) {
        moneyItemList.clear();
        moneyItemList.addAll(items);

        notifyDataSetChanged();
    }

    public void addItem(Item item) {
        moneyItemList.add(item);
        // Говорим адаптеру, что список изменился, чтобы он отобразил актуальный список
        notifyDataSetChanged();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        private View mItemView;
        private TextView nameView;
        private TextView valueView;

        public ItemViewHolder(@NonNull View itemView, int colorId) {
            super(itemView);
            mItemView = itemView;
            nameView = itemView.findViewById(R.id.itemNameView);
            valueView = itemView.findViewById(R.id.itemValueView);
            // Устанавливаем цвет для значения дохода или расхода
            valueView.setTextColor(ContextCompat.getColor(valueView.getContext(), colorId));
        }

        public void bind(final Item item) {
            nameView.setText(item.getName());
            valueView.setText(String.valueOf(item.getPrice()) + " ₽");
        }
    }

}
