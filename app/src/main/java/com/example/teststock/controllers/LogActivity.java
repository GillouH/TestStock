package com.example.teststock.controllers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teststock.models.PersonalLog;

public class LogActivity extends AppCompatActivity{
    protected static final PersonalLog personalLog = new PersonalLog();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp(){
        personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickNavigateUpButton");
        if(!super.onSupportNavigateUp()){
            finish();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
    }
}
