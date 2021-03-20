package com.example.teststock.controllers;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.teststock.R;
import com.example.teststock.models.BasicItem;
import com.example.teststock.models.Item;
import com.example.teststock.models.PackItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ItemDetailActivity extends ItemManagerActivity{
    private static final String BUNDLE_STATE_ITEM_ID = "BUNDLE_STATE_ITEM_ID";
    private static final String BUNDLE_BASIC_ITEM_QUANTITY_MANAGER = "BUNDLE_BASIC_ITEM_QUANTITY_MANAGER";
    private static final String BUNDLE_PACK_ITEM_QUANTITY_OUT_QUANTITY_MANAGER = "BUNDLE_PACK_ITEM_QUANTITY_OUT_QUANTITY_MANAGER";
    private Item item;
    private static final String BUNDLE_PACK_ITEM_NB_PACK_QUANTITY_MANAGER = "BUNDLE_PACK_ITEM_NB_PACK_QUANTITY_MANAGER";
    private TextView quantityText;
    private TextView quantityOutText;
    private TextView quantityPackText;
    private int itemID;
    private QuantityManager basicItemQuantityManager, packItemQuantityOutQuantityManager, packItemNbPackQuantityManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = findViewById(R.id.activityItemDetail_toolbar);
        TextView nameText = findViewById(R.id.activityItemDetail_textView_itemName);
        LinearLayout linearLayout_itemBasic = findViewById(R.id.activityItemDetail_linearLayout_itemBasic);
        quantityText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitValue);
        basicItemQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_basicItem);
        LinearLayout linearLayout_itemPack = findViewById(R.id.activityItemDetail_linearLayout_itemPack);
        quantityOutText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitOutValue);
        packItemQuantityOutQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_packItem_quantityOut);
        quantityPackText = findViewById(R.id.activityItemDetail_textView_nbPackValue);
        packItemNbPackQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_packItem_nbPack);
        TextView notificationTresholdText = findViewById(R.id.activityItemDetail_notificationThreshold);
        FloatingActionButton editButton = findViewById(R.id.activityItemDetail_floattingButton);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Get cliked item and his class in the last activity.
        if(savedInstanceState == null){
            itemID = getIntent().getIntExtra(ItemManagerActivity.INTENT_EXTRA_DATA_KEY_ID, -1);
        }else{
            itemID = savedInstanceState.getInt(BUNDLE_STATE_ITEM_ID);
        }
        if(itemID != -1){
            item = getItem(itemID);
            if(item != null){
                nameText.setText(item.getName());
                if(item.getClass().equals(BasicItem.class)){
                    BasicItem basicItem = (BasicItem)item;
                    quantityText.setText(basicItem.getQuantityFormated());
                    basicItemQuantityManager.setOnClickListener(v->{
                        if(basicItem.modifyQuantity(basicItemQuantityManager.getQuantity()) == 1){
                            sendNotification(basicItem);
                        }
                        modifyItemInItemList(basicItem);
                        quantityText.setText(basicItem.getQuantityFormated());
                    });
                    notificationTresholdText.setText(basicItem.getSeuilFormated());
                }else if(item.getClass().equals(PackItem.class)){
                    PackItem packItem = (PackItem)item;
                    quantityOutText.setText(packItem.getQuantityOutFormated());
                    packItemQuantityOutQuantityManager.setOnClickListener(v->{
                        if(packItem.modifyQuantityOut(packItemQuantityOutQuantityManager.getQuantity()) == 1){
                            sendNotification(packItem);
                        }
                        modifyItemInItemList(packItem);
                        updateQuantityPackItemText(packItem);
                    });
                    quantityPackText.setText(packItem.getNbPackFullFormated());
                    packItemNbPackQuantityManager.setOnClickListener(v->{
                        if(packItem.modifyNbPack(packItemNbPackQuantityManager.getQuantity()) == 1){
                            sendNotification(packItem);
                        }
                        modifyItemInItemList(packItem);
                        updateQuantityPackItemText(packItem);
                    });
                    notificationTresholdText.setText(packItem.getSeuilFormated());
                }
                linearLayout_itemBasic.setVisibility(item.getClass().equals(BasicItem.class) ? View.VISIBLE : View.GONE);
                linearLayout_itemPack.setVisibility(item.getClass().equals(PackItem.class) ? View.VISIBLE : View.GONE);
            }
        }

        editButton.setOnClickListener(this::editAction);
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState){
        outState.putInt(BUNDLE_STATE_ITEM_ID, itemID);
        outState.putString(BUNDLE_BASIC_ITEM_QUANTITY_MANAGER, String.valueOf(basicItemQuantityManager.getQuantity()));
        outState.putString(BUNDLE_PACK_ITEM_QUANTITY_OUT_QUANTITY_MANAGER, String.valueOf(packItemQuantityOutQuantityManager.getQuantity()));
        outState.putString(BUNDLE_PACK_ITEM_NB_PACK_QUANTITY_MANAGER, String.valueOf(packItemNbPackQuantityManager.getQuantity()));
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        basicItemQuantityManager.setText(savedInstanceState.getString(BUNDLE_BASIC_ITEM_QUANTITY_MANAGER));
        packItemQuantityOutQuantityManager.setText(savedInstanceState.getString(BUNDLE_PACK_ITEM_QUANTITY_OUT_QUANTITY_MANAGER));
        packItemNbPackQuantityManager.setText(savedInstanceState.getString(BUNDLE_PACK_ITEM_NB_PACK_QUANTITY_MANAGER));
    }

    private void updateQuantityPackItemText(PackItem packItem){
        if(packItem != null){
            quantityOutText.setText(packItem.getQuantityOutFormated());
            quantityPackText.setText(packItem.getNbPackFullFormated());
        }
    }

    private void editAction(View view){
        log(TYPE.CLICK, "clickEditButton");
        Intent editItemActivity = new Intent(ItemDetailActivity.this, EditItemActivity.class);
        editItemActivity.putExtra(ItemManagerActivity.INTENT_EXTRA_DATA_KEY_ID, item.getID());
        startActivity(editItemActivity);
    }
}
