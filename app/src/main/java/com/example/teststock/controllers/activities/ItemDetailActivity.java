package com.example.teststock.controllers.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.teststock.R;
import com.example.teststock.controllers.OneItemManagerActivity;
import com.example.teststock.controllers.QuantityManager;
import com.example.teststock.models.CustomLog;
import com.example.teststock.models.items.BasicItem;
import com.example.teststock.models.items.Item;
import com.example.teststock.models.items.PackItem;
import com.example.teststock.models.managers.ItemManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

public class ItemDetailActivity extends OneItemManagerActivity{
    private TextView nameText, notificationTresholdText;
    private LinearLayout linearLayout_itemBasic, linearLayout_itemPack;
    private TextView quantityText;
    private TextView quantityOutText;
    private TextView quantityPackText;
    private QuantityManager basicItemQuantityManager, packItemQuantityOutQuantityManager, packItemNbPackQuantityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        Toolbar toolbar = findViewById(R.id.activityItemDetail_toolbar);
        nameText = findViewById(R.id.activityItemDetail_textView_itemName);
        linearLayout_itemBasic = findViewById(R.id.activityItemDetail_linearLayout_itemBasic);
        quantityText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitValue);
        basicItemQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_basicItem);
        linearLayout_itemPack = findViewById(R.id.activityItemDetail_linearLayout_itemPack);
        quantityOutText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitOutValue);
        packItemQuantityOutQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_packItem_quantityOut);
        quantityPackText = findViewById(R.id.activityItemDetail_textView_nbPackValue);
        packItemNbPackQuantityManager = findViewById(R.id.activityItemDetail_quantityManager_packItem_nbPack);
        notificationTresholdText = findViewById(R.id.activityItemDetail_notificationThreshold);
        FloatingActionButton editButton = findViewById(R.id.activityItemDetail_floattingButton);

        setSupportActionBar(toolbar);

        if(savedInstanceState == null){
            itemID = getIntent().getIntExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, -1);
            fillItemForm(itemID);
        }

        editButton.setOnClickListener(this::editAction);
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        fillItemForm(itemID);
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
        if(itemType != null){
            linearLayout_itemBasic.setVisibility(itemType.equals(BUNDLE_VALUE_ITEM_TYPE_BASIC) ? View.VISIBLE : View.GONE);
            linearLayout_itemPack.setVisibility(itemType.equals(BUNDLE_VALUE_ITEM_TYPE_PACKED) ? View.VISIBLE : View.GONE);
        }else{
            linearLayout_itemBasic.setVisibility(View.GONE);
            linearLayout_itemPack.setVisibility(View.GONE);
        }
    }

    protected void fillItemForm(int itemID){
        Item item = itemManager.get(itemID);
        nameText.setText(item.getName());
        if(item.getClass().equals(BasicItem.class)){
            fillBasicItemForm((BasicItem)item);
        }else if(item.getClass().equals(PackItem.class)){
            fillPackItemForm((PackItem)item);
        }
        notificationTresholdText.setText(item.getSeuilFormated());
    }

    private void fillBasicItemForm(@NotNull BasicItem item){
        quantityText.setText(item.getQuantityFormated());
        basicItemQuantityManager.setMin(-item.getQuantity());
        basicItemQuantityManager.setOnValidationClickListener(v->{
            if(item.modifyQuantity(basicItemQuantityManager.getNumber()).equals(Item.SEND_NOTIFICATION.YES)){
                itemManager.sendNotification(item);
            }
            itemManager.replaceInList(item);
            basicItemQuantityManager.setMin(-item.getQuantity());
            quantityText.setText(item.getQuantityFormated());
        });

        linearLayout_itemBasic.setVisibility(View.VISIBLE);
        linearLayout_itemPack.setVisibility(View.GONE);
    }

    private void fillPackItemForm(@NotNull PackItem item){
        quantityOutText.setText(item.getQuantityOutFormated());
        packItemQuantityOutQuantityManager.setMin(-item.getQuantityLeft());
        packItemQuantityOutQuantityManager.setOnValidationClickListener(v->{
            if(item.modifyQuantityOut(packItemQuantityOutQuantityManager.getNumber()).equals(Item.SEND_NOTIFICATION.YES)){
                itemManager.sendNotification(item);
            }
            itemManager.replaceInList(item);
            packItemQuantityOutQuantityManager.setMin(-item.getQuantityLeft());
            packItemNbPackQuantityManager.setMin(-item.getNbPackFull());
            updateQuantityPackItemText(item);
        });

        quantityPackText.setText(item.getNbPackFullFormated());
        packItemNbPackQuantityManager.setMin(-item.getNbPackFull());
        packItemNbPackQuantityManager.setOnValidationClickListener(v->{
            if(item.modifyNbPack(packItemNbPackQuantityManager.getNumber()).equals(Item.SEND_NOTIFICATION.YES)){
                itemManager.sendNotification(item);
            }
            itemManager.replaceInList(item);
            packItemQuantityOutQuantityManager.setMin(-item.getQuantityLeft());
            packItemNbPackQuantityManager.setMin(-item.getNbPackFull());
            updateQuantityPackItemText(item);
        });

        linearLayout_itemBasic.setVisibility(View.GONE);
        linearLayout_itemPack.setVisibility(View.VISIBLE);
    }

    private void updateQuantityPackItemText(PackItem packItem){
        if(packItem != null){
            quantityOutText.setText(packItem.getQuantityOutFormated());
            quantityPackText.setText(packItem.getNbPackFullFormated());
        }
    }

    private void editAction(View view){
        customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickEditButton");
        Intent intent = new Intent(this, EditItemActivity.class);
        intent.putExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, itemID);
        startActivity(intent);
    }
}
