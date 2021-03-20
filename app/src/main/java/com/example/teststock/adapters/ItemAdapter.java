package com.example.teststock.adapters;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teststock.R;
import com.example.teststock.controllers.ItemDetailActivity;
import com.example.teststock.controllers.ItemManagerActivity;
import com.example.teststock.controllers.MainActivity;
import com.example.teststock.models.Item;

import java.util.Collections;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements ItemMoveCallback.ItemTouchHelperContract{
    private List<Item> itemList;
    private static Drawable move, delete;
    private final StartDragListener startDragListener;
    private MainActivity.MODE mode;

    public ItemAdapter(java.util.List<Item> itemList, MainActivity.MODE mode, StartDragListener startDragListener){
        this.itemList = itemList;
        this.mode = mode;
        this.startDragListener = startDragListener;
    }

    public static void setMoveDrawable(Drawable move){
        ItemAdapter.move = move;
    }

    public static void setDeleteDrawable(Drawable delete){
        ItemAdapter.delete = delete;
    }

    @NonNull
    @Override
    public final ItemViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public final int getItemCount(){
        return itemList.size();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public final void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position){
        final Item item = itemList.get(position);
        holder.name.setText(item.getName());
        holder.itemView.setClickable(false);
        holder.imageView.setVisibility(mode == MainActivity.MODE.NORMAL ? View.GONE : View.VISIBLE);

        switch(mode){
            case NORMAL:
                holder.itemView.setOnClickListener(v->{
                    Intent itemDetailActivity = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
                    itemDetailActivity.putExtra(ItemManagerActivity.INTENT_EXTRA_DATA_KEY_ID, item.getID());
                    startActivity(holder.itemView.getContext(), itemDetailActivity, null);
                });
                break;
            case MOVE:
                holder.imageView.setImageDrawable(move);
                holder.imageView.setOnTouchListener((v, event)->{
                    v.performClick();
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        startDragListener.requestDrag(holder);
                    }
                    return false;
                });
                break;
            case DELETE:
                holder.imageView.setImageDrawable(delete);
                holder.imageView.setOnClickListener(v->{
                    int index = itemList.indexOf(item);
                    itemList.remove(index);
                    notifyItemRemoved(index);
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onRowMoved(int fromPosition, int toPosition){
        if(fromPosition < toPosition){
            for(int i = fromPosition; i < toPosition; i++){
                Collections.swap(itemList, i, i + 1);
            }
        }else{
            for(int i = fromPosition; i > toPosition; i--){
                Collections.swap(itemList, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onRowSelected(ItemViewHolder itemViewHolder){
        itemViewHolder.cardView.setCardBackgroundColor(Color.GRAY);
    }

    @Override
    public void onRowClear(ItemViewHolder itemViewHolder){
        itemViewHolder.cardView.setCardBackgroundColor(itemViewHolder.cardViewBackgroundColorStateList);
    }

    public void setMode(MainActivity.MODE mode){
        this.mode = mode;
    }

    public void setItemList(List<Item> itemList){
        this.itemList = itemList;
    }

    public final static class ItemViewHolder extends RecyclerView.ViewHolder{
        private final CardView cardView;
        private final TextView name;
        private final ImageView imageView;
        private final ColorStateList cardViewBackgroundColorStateList;

        private ItemViewHolder(@NonNull View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.itemAdapter_cardView);
            name = itemView.findViewById(R.id.itemAdapter_textView);
            imageView = itemView.findViewById(R.id.item_Adapter_imageView);

            cardViewBackgroundColorStateList = cardView.getCardBackgroundColor();
        }
    }
}
