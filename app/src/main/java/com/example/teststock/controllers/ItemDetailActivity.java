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

public class ItemDetailActivity extends OneItemManagerActivity{
    private LinearLayout linearLayout_itemBasic, linearLayout_itemPack;
    private TextView quantityText;
    private TextView quantityOutText;
    private TextView quantityPackText;
    private QuantityManager basicItemQuantityManager, packItemQuantityOutQuantityManager, packItemNbPackQuantityManager;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = findViewById(R.id.activityItemDetail_toolbar);
        TextView nameText = findViewById(R.id.activityItemDetail_textView_itemName);
        linearLayout_itemBasic = findViewById(R.id.activityItemDetail_linearLayout_itemBasic);
        quantityText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitValue);
        basicItemQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_basicItem);
        linearLayout_itemPack = findViewById(R.id.activityItemDetail_linearLayout_itemPack);
        quantityOutText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitOutValue);
        packItemQuantityOutQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_packItem_quantityOut);
        quantityPackText = findViewById(R.id.activityItemDetail_textView_nbPackValue);
        packItemNbPackQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_packItem_nbPack);
        TextView notificationTresholdText = findViewById(R.id.activityItemDetail_notificationThreshold);
        FloatingActionButton editButton = findViewById(R.id.activityItemDetail_floattingButton);

        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            itemID = getIntent().getIntExtra(ItemManagerActivity.INTENT_EXTRA_DATA_KEY_ID, -1);
            Item item = getItem(itemID);
            nameText.setText(item.getName());
            if(item.getClass().equals(BasicItem.class)){
                fillBasicItemForm((BasicItem)item);
            }else if(item.getClass().equals(PackItem.class)){
                fillPackItemForm((PackItem)item);
            }
            notificationTresholdText.setText(item.getSeuilFormated());
            linearLayout_itemBasic.setVisibility(item.getClass().equals(BasicItem.class) ? View.VISIBLE : View.GONE);
            linearLayout_itemPack.setVisibility(item.getClass().equals(PackItem.class) ? View.VISIBLE : View.GONE);
        }

        editButton.setOnClickListener(this::editAction);
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState){
        String itemType = BUNDLE_VALUE_ITEM_TYPE_UNDEFINED;
        if(linearLayout_itemBasic.getVisibility() == View.VISIBLE){
            itemType = BUNDLE_VALUE_ITEM_TYPE_BASIC;
        }else if(linearLayout_itemPack.getVisibility() == View.VISIBLE){
            itemType = BUNDLE_VALUE_ITEM_TYPE_PACKED;
        }
        outState.putString(BUNDLE_KEY_ITEM_TYPE, itemType);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String itemType = savedInstanceState.getString(BUNDLE_KEY_ITEM_TYPE);
        linearLayout_itemBasic.setVisibility(itemType.equals(BUNDLE_VALUE_ITEM_TYPE_BASIC) ? View.VISIBLE : View.GONE);
        linearLayout_itemPack.setVisibility(itemType.equals(BUNDLE_VALUE_ITEM_TYPE_PACKED) ? View.VISIBLE : View.GONE);
    }

    private void fillBasicItemForm(@NotNull BasicItem item){
        quantityText.setText(item.getQuantityFormated());
        basicItemQuantityManager.setMin(-item.getQuantity());
        basicItemQuantityManager.setOnValidationClickListener(v->{
            if(item.modifyQuantity(basicItemQuantityManager.getNumber()) == 1){
                sendNotification(item);
            }
            modifyItemInItemList(item);
            basicItemQuantityManager.setMin(-item.getQuantity());
            quantityText.setText(item.getQuantityFormated());
        });
    }

    private void fillPackItemForm(@NotNull PackItem item){
        quantityOutText.setText(item.getQuantityOutFormated());
        packItemQuantityOutQuantityManager.setMin(-item.getQuantityOut());
        packItemQuantityOutQuantityManager.setOnValidationClickListener(v->{
            if(item.modifyQuantityOut(packItemQuantityOutQuantityManager.getNumber()) == 1){
                sendNotification(item);
            }
            modifyItemInItemList(item);
            packItemQuantityOutQuantityManager.setMin(-item.getQuantityOut());
            updateQuantityPackItemText(item);
        });

        quantityPackText.setText(item.getNbPackFullFormated());
        packItemNbPackQuantityManager.setMin(-item.getNbPackFull());
        packItemNbPackQuantityManager.setOnValidationClickListener(v->{
            if(item.modifyNbPack(packItemNbPackQuantityManager.getNumber()) == 1){
                sendNotification(item);
            }
            modifyItemInItemList(item);
            packItemNbPackQuantityManager.setMin(-item.getNbPackFull());
            updateQuantityPackItemText(item);
        });
    }

    private void updateQuantityPackItemText(PackItem packItem){
        if(packItem != null){
            quantityOutText.setText(packItem.getQuantityOutFormated());
            quantityPackText.setText(packItem.getNbPackFullFormated());
        }
    }

    private void editAction(View view){
        log(TYPE.CLICK, "clickEditButton");
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(ItemManagerActivity.INTENT_EXTRA_DATA_KEY_ID, itemID);
        startActivity(intent);
    }
}
