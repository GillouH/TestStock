package com.example.teststock.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

public class ItemMoveCallback extends ItemTouchHelper.Callback{
    private final ItemTouchHelperContract mAdapter;
    private boolean longPressDragEnabled = false;

    public ItemMoveCallback(ItemTouchHelperContract mAdapter){
        this.mAdapter = mAdapter;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder){
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target){
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction){
    }

    @Override
    public boolean isLongPressDragEnabled(){
        return longPressDragEnabled;
    }

    public void setLongPressDragEnabled(boolean longPressDragEnabled){
        this.longPressDragEnabled = longPressDragEnabled;
    }

    @Override
    public boolean isItemViewSwipeEnabled(){
        return false;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState){
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            if(viewHolder instanceof ItemAdapter.ItemViewHolder){
                ItemAdapter.ItemViewHolder myViewHolder = (ItemAdapter.ItemViewHolder)viewHolder;
                mAdapter.onRowSelected(myViewHolder);
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(@NotNull RecyclerView recyclerView, RecyclerView.@NotNull ViewHolder viewHolder){
        super.clearView(recyclerView, viewHolder);
        if(viewHolder instanceof ItemAdapter.ItemViewHolder){
            ItemAdapter.ItemViewHolder myViewHolder = (ItemAdapter.ItemViewHolder)viewHolder;
            mAdapter.onRowClear(myViewHolder);
        }
    }

    public interface ItemTouchHelperContract{
        void onRowMoved(int fromPosition, int toPosition);

        void onRowSelected(ItemAdapter.ItemViewHolder myViewHolder);

        void onRowClear(ItemAdapter.ItemViewHolder myViewHolder);
    }
}
