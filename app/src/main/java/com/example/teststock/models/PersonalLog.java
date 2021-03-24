package com.example.teststock.models;

import android.util.Log;


public class PersonalLog{
    protected static final String PERSO = "[PERSO]";

    public void write(TYPE type, Class<?> classToLog, String msg){
        String format = "%s %s";
        if(type != null){
            if(type.equals(TYPE.CLICK)){
                format = "%s : %s";
            }else if(type.equals(TYPE.METHOD)){
                format = "%s.%s";
            }
        }
        Log.d(PERSO, String.format(format, classToLog.getSimpleName(), msg));
    }

    public enum TYPE{
        CLICK,
        METHOD
    }
}
