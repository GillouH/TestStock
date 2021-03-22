package com.example.teststock.models;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class BasicItem extends Item{
    private static final String JSON_KEY_QUANTITY = "JSON_KEY_QUANTITY";
    private static final String JSON_KEY_UNIT = "JSON_KEY_UNIT";
    private int quantity;
    private final String unit;

    public BasicItem(int ID, String name, int quantity, String unit, int seuil){
        super(ID, name, seuil);
        this.quantity = quantity;
        this.unit = unit;
    }

    public static @Nullable BasicItem fromJSON(JSONObject json){
        try{
            return new BasicItem(json.getInt(JSON_KEY_ID), json.getString(JSON_KEY_NAME), json.getInt(JSON_KEY_QUANTITY), json.getString(JSON_KEY_UNIT), json.getInt(JSON_KEY_SEUIL));
        }catch(Exception ignored){
        }
        return null;
    }

    public int getQuantity(){
        return quantity;
    }

    public String getUnit(){
        return this.unit;
    }

    public int modifyQuantity(int quantity){
        this.quantity += quantity;
        if(this.quantity > seuil){
            return 0;
        }else{
            if(this.quantity < 0){
                this.quantity = 0;
            }
            return 1;
        }
    }

    @Override
    public String getSeuilFormated(){
        return String.format(Locale.getDefault(), "%d %s", getSeuil(), getUnit());
    }

    public String getQuantityFormated(){
        return String.format(Locale.getDefault(), "%d %s", getQuantity(), getUnit());
    }

    @Override
    public String getQuantityLeftFormated(){
        return getQuantityFormated();
    }

    public JSONObject toJSON(){
        JSONObject json = super.toJSON();
        try{
            json.put(JSON_KEY_QUANTITY, quantity);
            json.put(JSON_KEY_UNIT, unit);
        }catch(JSONException ignored){
        }
        return json;
    }

    @Override
    public String toString(){
        return toJSON().toString();
    }
}