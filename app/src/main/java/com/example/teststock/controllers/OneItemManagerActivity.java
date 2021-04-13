package com.example.teststock.controllers;

import android.os.Bundle;

import androidx.annotation.NonNull;

public class OneItemManagerActivity extends PersonalActivity{
    protected static final String BUNDLE_KEY_ITEM_ID = "BUNDLE_STATE_ITEM_ID";
    protected static final String BUNDLE_KEY_ITEM_TYPE = "BUNDLE_STATE_ITEM_TYPE";
    protected static final String BUNDLE_VALUE_ITEM_TYPE_UNDEFINED = "BUNDLE_STATE_ITEM_TYPE_UNDEFINED";
    protected static final String BUNDLE_VALUE_ITEM_TYPE_BASIC = "BUNDLE_STATE_ITEM_TYPE_BASIC";
    protected static final String BUNDLE_VALUE_ITEM_TYPE_PACKED = "BUNDLE_STATE_ITEM_TYPE_PACKED";

    protected int itemID;

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        outState.putInt(BUNDLE_KEY_ITEM_ID, itemID);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        itemID = savedInstanceState.getInt(BUNDLE_KEY_ITEM_ID);
    }
}
