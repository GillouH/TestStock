package com.example.teststock.models;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item{
    public static final String JSON_KEY_CLASS = "JSON_KEY_CLASS";
    public static final String JSON_KEY_ID = "JSON_KEY_ID";
    protected static final String JSON_KEY_NAME = "JSON_KEY_NAME";
    protected static final String JSON_KEY_SEUIL = "JSON_KEY_SEUIL";
    protected final int seuil;
    private int ID;
    private final String name;

    public Item(int ID, String name, int seuil){
        this.ID = ID;
        this.name = name;
        this.seuil = seuil;
    }

    public static @Nullable Item fromJSON(JSONObject json){
        String itemClass;
        try{
            itemClass = json.getString(JSON_KEY_CLASS);
        }catch(Exception e){
            return null;
        }
        if(itemClass.equals(BasicItem.class.getSimpleName())){
            return BasicItem.fromJSON(json);
        }else if(itemClass.equals(PackItem.class.getSimpleName())){
            return PackItem.fromJSON(json);
        }else{
            return null;
        }
    }

    public int getID(){
        return ID;
    }

    public String getName(){
        return name;
    }

    public void setID(int ID){
        this.ID = ID;
    }

    public int getSeuil(){
        return seuil;
    }

    public abstract String getSeuilFormated();

    public abstract String getQuantityLeftFormated();

    public JSONObject toJSON(){
        JSONObject json = new JSONObject();
        try{
            json.put(JSON_KEY_CLASS, getClass().getSimpleName());
            json.put(JSON_KEY_ID, ID);
            json.put(JSON_KEY_NAME, name);
            json.put(JSON_KEY_SEUIL, seuil);
        }catch(JSONException ignored){
        }
        return json;
    }

    @Override
    public String toString(){
        return toJSON().toString();
    }
}
