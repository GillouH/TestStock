package com.example.teststock.controllers;

import com.example.teststock.models.ItemManager;

public class ItemManagerActivity extends LogActivity{
    protected final ItemManager itemManager;

    public ItemManagerActivity(){
        super();
        itemManager = new ItemManager(this);
    }
}
