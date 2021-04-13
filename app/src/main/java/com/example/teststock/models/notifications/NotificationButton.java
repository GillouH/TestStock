package com.example.teststock.models.notifications;

import android.content.Intent;

public class NotificationButton{
    private final Intent intent;
    private final String buttonName;
    private final int drawable;

    public NotificationButton(Intent intent, String buttonName, int drawable){
        this.intent = intent;
        this.buttonName = buttonName;
        this.drawable = drawable;
    }

    public Intent getIntent(){
        return intent;
    }

    public String getButtonName(){
        return buttonName;
    }

    public int getDrawable(){
        return drawable;
    }
}
