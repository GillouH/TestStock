package com.example.teststock.models;

import com.example.teststock.models.items.BasicItem;
import com.example.teststock.models.items.PackItem;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONable{
    protected static final String JSON_KEY_CLASS = "JSON_KEY_CLASS";
    protected static final String JSON_KEY_ID = "JSON_KEY_ID";

    private int ID;

    protected JSONable(int ID){
        this.ID = ID;
    }

    public static @Nullable JSONable fromJSON(JSONObject jsonObject){
        String itemClass;
        try{
            itemClass = jsonObject.getString(JSON_KEY_CLASS);
        }catch(Exception e){
            return null;
        }

        if(itemClass.equals(BasicItem.class.getSimpleName())){
            return BasicItem.fromJSON(jsonObject);
        }else if(itemClass.equals(PackItem.class.getSimpleName())){
            return PackItem.fromJSON(jsonObject);
        }else if(itemClass.equals(Couple.class.getSimpleName())){
            return Couple.fromJSON(jsonObject);
        }else{
            return null;
        }
    }

    public int getID(){
        return ID;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put(JSON_KEY_CLASS, getClass().getSimpleName());
            jsonObject.put(JSON_KEY_ID, ID);
        }catch(JSONException ignored){
        }
        return jsonObject;
    }
}
