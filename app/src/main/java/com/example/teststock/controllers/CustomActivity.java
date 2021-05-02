package com.example.teststock.controllers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teststock.models.CustomLog;
import com.example.teststock.models.managers.DictionaryManager;
import com.example.teststock.models.managers.ItemManager;

public class CustomActivity extends AppCompatActivity{
    protected static final CustomLog customLog = new CustomLog();
    protected final ItemManager itemManager;
    protected final DictionaryManager dictionaryManager;

    public CustomActivity(){
        super();
        itemManager = new ItemManager(this);
        dictionaryManager = new DictionaryManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume(){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause(){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop(){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart(){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy(){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp(){
        customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickNavigateUpButton");
        if(!super.onSupportNavigateUp()){
            finish();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
