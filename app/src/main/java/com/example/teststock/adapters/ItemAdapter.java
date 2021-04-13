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
import com.example.teststock.controllers.PersonalActivity.ACTION_MODE;
import com.example.teststock.controllers.activities.ItemDetailActivity;
import com.example.teststock.models.items.Item;
import com.example.teststock.models.managers.ItemManager;

import java.util.Collections;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements ItemMoveCallback.ItemTouchHelperContract{
    private List<Item> itemList;
    private static Drawable imageDrawableMove, imageDrawableDelete;
    private final StartDragListener startDragListener;
    private ACTION_MODE mode;
    private static String contentDescriptionMove, contentDescriptionDelete;

    public ItemAdapter(java.util.List<Item> itemList, ACTION_MODE mode, StartDragListener startDragListener){
        this.itemList = itemList;
        this.mode = mode;
        this.startDragListener = startDragListener;
    }

    public static void setImageDrawableMove(Drawable move){
        ItemAdapter.imageDrawableMove = move;
    }

    public static void setImageDrawableDelete(Drawable delete){
        ItemAdapter.imageDrawableDelete = delete;
    }

    public static void setContentDescriptionMove(String contentDescriptionMove){
        ItemAdapter.contentDescriptionMove = contentDescriptionMove;
    }

    public static void setContentDescriptionDelete(String contentDescriptionDelete){
        ItemAdapter.contentDescriptionDelete = contentDescriptionDelete;
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
        holder.imageView.setVisibility(mode.equals(ACTION_MODE.NORMAL) ? View.GONE : View.VISIBLE);

        switch(mode){
            case NORMAL:
                holder.itemView.setOnClickListener(v->{
                    Intent intent = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
                    intent.putExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, item.getID());
                    startActivity(holder.itemView.getContext(), intent, null);
                });
                break;
            case MOVE:
                holder.imageView.setImageDrawable(imageDrawableMove);
                holder.imageView.setContentDescription(contentDescriptionMove);
                holder.imageView.setOnTouchListener((v, event)->{
                    v.performClick();
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        startDragListener.requestDrag(holder);
                    }
                    return false;
                });
                break;
            case DELETE:
                holder.imageView.setImageDrawable(imageDrawableDelete);
                holder.imageView.setContentDescription(contentDescriptionDelete);
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

    public void setMode(ACTION_MODE mode){
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
