package com.example.teststock.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teststock.R;
import com.example.teststock.controllers.activities.ItemDetailActivity;
import com.example.teststock.controllers.activities.MenuActivity.ACTION_MODE;
import com.example.teststock.models.items.Item;
import com.example.teststock.models.managers.ItemManager;

import java.util.Collections;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> implements ItemMoveCallback.ItemTouchHelperContract{
    private List<Item> itemList;
    private static final int
            drawableIDMove = R.drawable.ic_baseline_pan_tool_24,
            drawableIDDelete = R.drawable.ic_baseline_delete_24,
            contentDescriptionMoveID = R.string.move,
            contentDescriptionDeleteID = R.string.delete;
    private final Drawable imageDrawableDefaultItemPicture, imageDrawableMove, imageDrawableDelete;
    private final StartDragListener startDragListener;
    private ACTION_MODE mode;
    private static String contentDescriptionMove, contentDescriptionDelete;

    public ItemAdapter(Context context, java.util.List<Item> itemList, ACTION_MODE mode, StartDragListener startDragListener){
        imageDrawableDefaultItemPicture = ContextCompat.getDrawable(context, Item.drawableIDDefaultItemPicture);
        imageDrawableMove = ContextCompat.getDrawable(context, drawableIDMove);
        contentDescriptionMove = context.getString(contentDescriptionMoveID);
        imageDrawableDelete = ContextCompat.getDrawable(context, drawableIDDelete);
        contentDescriptionDelete = context.getString(contentDescriptionDeleteID);
        this.itemList = itemList;
        this.mode = mode;
        this.startDragListener = startDragListener;
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
        String imageStr = item.getImage();
        if(imageStr.equals(Item.JSON_VALUE_IMAGE_NULL)){
            holder.imageViewItemPicture.setImageDrawable(imageDrawableDefaultItemPicture);
        }else{
            try{
                holder.imageViewItemPicture.setImageURI(Uri.parse(imageStr));
            }catch(SecurityException e){
                e.printStackTrace();
                holder.imageViewItemPicture.setImageDrawable(imageDrawableDefaultItemPicture);
            }
        }
        holder.name.setText(item.getName());
        holder.itemView.setClickable(false);
        holder.quantity.setVisibility(mode.equals(ACTION_MODE.NORMAL) ? View.VISIBLE : View.GONE);
        holder.imageViewAction.setVisibility(mode.equals(ACTION_MODE.NORMAL) ? View.GONE : View.VISIBLE);

        switch(mode){
            case NORMAL:
                holder.quantity.setText(String.valueOf(item.getQuantityLeft()));
                holder.itemView.setOnClickListener(v->{
                    Intent intent = new Intent(holder.itemView.getContext(), ItemDetailActivity.class);
                    intent.putExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, item.getID());
                    startActivity(holder.itemView.getContext(), intent, null);
                });
                break;
            case MOVE:
                holder.imageViewAction.setImageDrawable(imageDrawableMove);
                holder.imageViewAction.setContentDescription(contentDescriptionMove);
                holder.imageViewAction.setOnTouchListener((v, event)->{
                    v.performClick();
                    if(event.getAction() == MotionEvent.ACTION_DOWN){
                        startDragListener.requestDrag(holder);
                    }
                    return false;
                });
                break;
            case DELETE:
                holder.imageViewAction.setImageDrawable(imageDrawableDelete);
                holder.imageViewAction.setContentDescription(contentDescriptionDelete);
                holder.imageViewAction.setOnClickListener(v->{
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
        private final TextView name, quantity;
        private final ImageView imageViewItemPicture, imageViewAction;
        private final ColorStateList cardViewBackgroundColorStateList;

        private ItemViewHolder(@NonNull View itemView){
            super(itemView);
            cardView = itemView.findViewById(R.id.itemAdapter_cardView);
            imageViewItemPicture = itemView.findViewById(R.id.itemAdapter_imageView_itemPicture);
            name = itemView.findViewById(R.id.itemAdapter_textView_name);
            quantity = itemView.findViewById(R.id.item_Adapter_textView_quantity);
            imageViewAction = itemView.findViewById(R.id.item_Adapter_imageView_action);

            cardViewBackgroundColorStateList = cardView.getCardBackgroundColor();
        }
    }
}