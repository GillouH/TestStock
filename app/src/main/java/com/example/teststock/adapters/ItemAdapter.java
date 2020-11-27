package com.example.teststock.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teststock.R;
import com.example.teststock.controllers.ItemDetailActivity;
import com.example.teststock.models.Item;

import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder>{
    private List<Item> itemList;

    public final static class ItemViewHolder extends RecyclerView.ViewHolder{
        private final TextView name;

        private ItemViewHolder(@NonNull View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.activityMainItemAdapter_textView_itemName);
        }
    }

    public ItemAdapter(java.util.List<Item> itemList){
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public final ItemViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_item_adapter, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }
    @Override
    public final void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position){
        final Item item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemDetailActivity = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
                itemDetailActivity.putExtra("item", item);
                startActivity(holder.itemView.getContext(), itemDetailActivity, null);
            }
        });
    }
    @Override
    public final int getItemCount(){
        return itemList.size();
    }
}
