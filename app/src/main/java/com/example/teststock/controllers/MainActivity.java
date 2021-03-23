package com.example.teststock.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teststock.R;
import com.example.teststock.adapters.ItemAdapter;
import com.example.teststock.adapters.ItemMoveCallback;
import com.example.teststock.adapters.StartDragListener;
import com.example.teststock.models.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.List;

// TODO: 18/03/2021 Gestion de compte ?
// TODO: 18/03/2021 Mise en réseau avec serveur
// TODO: 20/03/2021 Bouton rappel notification
// TODO: 20/03/2021 dictionnaire pluriel-singuliers
// TODO: 21/03/2021 Tableau, comment fonctionne streched

public class MainActivity extends ItemManagerActivity implements StartDragListener{
    private static final String BUNDLE_STATE_MODE = "BUNDLE_STATE_MODE";
    private static final String BUNDLE_STATE_ITEM_LIST = "BUNDLE_STATE_ITEM_LIST";

    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private ItemTouchHelper touchHelper;
    private boolean showMenu;
    private FloatingActionButton addButton;
    private ItemMoveCallback callback;
    private MODE mode;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.activityMain_toolbar);
        RecyclerView recyclerView = findViewById(R.id.activityMain_recyclerView);
        addButton = findViewById(R.id.activityMain_floattingButton);


        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            mode = MODE.valueOf(savedInstanceState.getString(BUNDLE_STATE_MODE));
            itemList = convertJSONArrayToItemList(convertJSONArrayAsStringToJSONArray(savedInstanceState.getString(BUNDLE_STATE_ITEM_LIST)));
        }else{
            mode = MODE.NORMAL;
            itemList = getItemList(false);
        }

        ItemAdapter.setMoveDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_pan_tool_24));
        ItemAdapter.setDeleteDrawable(ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24));

        showMenu = mode == MODE.NORMAL;

        itemAdapter = new ItemAdapter(itemList, mode, this);
        callback = new ItemMoveCallback(itemAdapter);
        callback.setLongPressDragEnabled(mode == MODE.MOVE);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(itemAdapter);

        addButton.setVisibility(mode == MODE.NORMAL ? View.VISIBLE : View.GONE);
        addButton.setOnClickListener(view->addAction());
    }

    private void refresh(){
        log(TYPE.METHOD, "refresh()");
        showMenu = mode == MODE.NORMAL;
        this.invalidateOptionsMenu();
        itemAdapter.setItemList(itemList);
        itemAdapter.setMode(mode);
        itemAdapter.notifyDataSetChanged();
        callback.setLongPressDragEnabled(mode == MODE.MOVE);
        addButton.setVisibility(mode == MODE.NORMAL ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        itemList = getItemList(false);
        mode = MODE.NORMAL;
        refresh();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState){
        outState.putString(BUNDLE_STATE_MODE, mode.toString());
        outState.putString(BUNDLE_STATE_ITEM_LIST, convertItemListToJSONArray(itemList).toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        menu.setGroupVisible(R.id.action_bar_menu, showMenu);
        menu.setGroupVisible(R.id.action_bar_actionButton, !showMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemId = item.getItemId();
        if(itemId == R.id.actionBar_menu_modifyOrder){
            log(TYPE.CLICK, "clickModifyOrderButton");
            mode = MODE.MOVE;
            refresh();
        }else if(itemId == R.id.actionBar_menu_removeItem){
            log(TYPE.CLICK, "clickRemoveItemButton");
            mode = MODE.DELETE;
            refresh();
        }else if(itemId == R.id.actionBar_menu_razButton){
            log(TYPE.CLICK, "clickRazButton");
            itemList = getItemList(true);
            mode = MODE.NORMAL;
            refresh();
        }else if(itemId == R.id.action_bar_actionButton_ok){
            log(TYPE.CLICK, "clickOkButton");
            updateListOrder(itemList);
            saveItemList(itemList);
            mode = MODE.NORMAL;
            refresh();
        }else if(itemId == R.id.action_bar_actionButton_cancel){
            log(TYPE.CLICK, "clickCancelButton");
            itemList = getItemList(false);
            mode = MODE.NORMAL;
            refresh();
        }else{
            log(TYPE.CLICK, "clickAnyMenuButton");
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    private void addAction(){
        log(TYPE.CLICK, "clickAddButton");
        Intent intent = new Intent(this, EditItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder){
        touchHelper.startDrag(viewHolder);
    }

    public enum MODE{
        NORMAL,
        MOVE,
        DELETE
    }
}