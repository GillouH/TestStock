package com.example.teststock.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.teststock.controllers.PersonalActivity;
import com.example.teststock.models.PersonalLog;
import com.example.teststock.models.items.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

// TODO: 18/03/2021 Gestion de compte
// TODO: 18/03/2021 Mise en réseau avec serveur
// TODO: 20/03/2021 Bouton rappel notification
// TODO: 12/04/2021 Liste d'item -> image
// TODO: 12/04/2021 partage de liste

public class MainActivity extends PersonalActivity implements StartDragListener{
    private static final String BUNDLE_STATE_ITEM_LIST = "BUNDLE_STATE_ITEM_LIST";

    private ItemAdapter itemAdapter;
    private List<Item> itemList;
    private ItemTouchHelper touchHelper;
    private FloatingActionButton addButton;
    private ItemMoveCallback callback;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.activityMain_toolbar);
        RecyclerView recyclerView = findViewById(R.id.activityMain_recyclerView);
        addButton = findViewById(R.id.activityMain_floattingButton);

        hasMenu = true;
        subMenuItemToShow = Arrays.asList(R.id.menu_submenu_button_modifyOrder, R.id.menu_submenu_button_removeItem, R.id.menu_submenu_button_dictionary);
        setSupportActionBar(toolbar);

        if(savedInstanceState != null){
            actionMode = ACTION_MODE.valueOf(savedInstanceState.getString(BUNDLE_STATE_MODE));
            itemList = itemManager.convertStringToList(savedInstanceState.getString(BUNDLE_STATE_ITEM_LIST));
        }else{
            actionMode = ACTION_MODE.NORMAL;
            itemList = itemManager.getList();
        }

        ItemAdapter.setImageDrawableMove(ContextCompat.getDrawable(this, R.drawable.ic_baseline_pan_tool_24));
        ItemAdapter.setImageDrawableDelete(ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24));
        ItemAdapter.setContentDescriptionMove(getString(R.string.move));
        ItemAdapter.setContentDescriptionDelete(getString(R.string.delete));

        showSubMenu = actionMode.equals(ACTION_MODE.NORMAL);

        itemAdapter = new ItemAdapter(itemList, actionMode, this);
        callback = new ItemMoveCallback(itemAdapter);
        callback.setLongPressDragEnabled(actionMode.equals(ACTION_MODE.MOVE));
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(itemAdapter);

        addButton.setVisibility(actionMode.equals(ACTION_MODE.NORMAL) ? View.VISIBLE : View.GONE);
        addButton.setOnClickListener(view->addAction());
    }

    private void refresh(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "refresh()");
        showSubMenu = actionMode.equals(ACTION_MODE.NORMAL);
        invalidateOptionsMenu();
        itemAdapter.setItemList(itemList);
        itemAdapter.setMode(actionMode);
        itemAdapter.notifyDataSetChanged();
        callback.setLongPressDragEnabled(actionMode.equals(ACTION_MODE.MOVE));
        addButton.setVisibility(actionMode.equals(ACTION_MODE.NORMAL) ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        itemList = itemManager.getList();
        actionMode = ACTION_MODE.NORMAL;
        refresh();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState){
        outState.putString(BUNDLE_STATE_MODE, actionMode.toString());
        outState.putString(BUNDLE_STATE_ITEM_LIST, itemManager.convertListToString(itemList));
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        boolean result = super.onOptionsItemSelected(item);
        int itemID = item.getItemId();
        if(itemID == R.id.menu_submenu_button_resetButton){
            itemList = itemManager.getList(true);
        }else if(itemID == R.id.menu_button_ok){
            itemManager.updateListOrder(itemList);
            itemManager.saveList(itemList);
        }else if(itemID == R.id.menu_button_cancel){
            itemList = itemManager.getList();
        }
        if(itemID != R.id.menu_submenu_button_dictionary){
            refresh();
        }
        return result;
    }

    private void addAction(){
        personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickAddButton");
        Intent intent = new Intent(this, EditItemActivity.class);
        startActivity(intent);
    }

    @Override
    public void requestDrag(RecyclerView.ViewHolder viewHolder){
        touchHelper.startDrag(viewHolder);
    }
}