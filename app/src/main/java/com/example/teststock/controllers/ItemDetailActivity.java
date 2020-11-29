package com.example.teststock.controllers;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.teststock.R;
import com.example.teststock.models.BasicItem;
import com.example.teststock.models.Item;
import com.example.teststock.models.PackItem;

import java.io.Serializable;

public class ItemDetailActivity extends AppCompatActivity{
    private TextView nameText, quantityText, quantityOutText, quantityPackText;
    private LinearLayout linearLayout_itemBasic, linearLayout_itemPack;
    private Toolbar toolbar;
    private Item item;
    private Class<? extends Serializable> itemClass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        toolbar = findViewById(R.id.activityItemDetail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.itemDetailActivity_title);

        // Get cliked item and his class in the last activity.
        item = (Item)getIntent().getSerializableExtra("item");
        itemClass = item.getClass();

        // Print détails
        if(itemClass.equals(BasicItem.class) || itemClass.equals(PackItem.class)){
            // Get back view element.
            nameText = findViewById(R.id.activityItemDetail_textView_itemName);
            linearLayout_itemBasic = findViewById(R.id.activityItemDetail_linearLayout_itemBasic);
            linearLayout_itemPack = findViewById(R.id.activityItemDetail_linearLayout_itemPack);

            nameText.setText(item.getName());

            if(itemClass.equals(BasicItem.class)){
                linearLayout_itemBasic.setVisibility(View.VISIBLE);
                linearLayout_itemPack.setVisibility(View.GONE);

                BasicItem basicItem = (BasicItem)item;
                quantityText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitValue);
                quantityText.setText(basicItem.getQuantity() + " " + basicItem.getUnit());
            }else if(itemClass.equals(PackItem.class)){
                linearLayout_itemBasic.setVisibility(View.GONE);
                linearLayout_itemPack.setVisibility(View.VISIBLE);

                PackItem packItem = (PackItem)item;
                quantityOutText = findViewById(R.id.activityItemDetail_textView_itemQuantityUnitOutValue);
                quantityPackText = findViewById(R.id.activityItemDetail_textView_nbPackValue);
                quantityOutText.setText(packItem.getQuantityOut() + " " + packItem.getUnitInPack());
                quantityPackText.setText(packItem.getNbPackFull() + " " + packItem.getPackUnit() + " de " + packItem.getQuantityMaxInPack() + " " + packItem.getUnitInPack());
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
