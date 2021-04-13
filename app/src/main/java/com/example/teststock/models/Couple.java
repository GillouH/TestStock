package com.example.teststock.models;

import com.example.teststock.models.managers.DictionaryManager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Couple extends JSONable{
    private static final String JSON_KEY_SINGULAR = "JSON_KEY_SINGULAR";
    private static final String JSON_KEY_PLURAL = "JSON_KEY_PLURAL";

    private String singular, plural;

    public Couple(int ID, @NotNull String singular, @NotNull String plural){
        super(ID);
        this.singular = singular.trim();
        this.plural = plural.trim();
    }

    public static @Nullable Couple fromJSON(JSONObject json){
        try{
            return new Couple(
                    json.getInt(JSON_KEY_ID),
                    json.getString(JSON_KEY_SINGULAR),
                    json.getString(JSON_KEY_PLURAL)
            );
        }catch(Exception ignored){
        }
        return null;
    }

    public String get(DictionaryManager.NOMBRE nombre){
        if(nombre != null){
            if(nombre.equals(DictionaryManager.NOMBRE.SINGULAR)){
                return getSingular();
            }else if(nombre.equals(DictionaryManager.NOMBRE.PLURAL)){
                return getPlural();
            }
        }
        return null;
    }

    public String getSingular(){
        return singular;
    }

    public String getPlural(){
        return plural;
    }

    public void set(DictionaryManager.NOMBRE nombre, String unit){
        if(nombre != null && unit != null){
            if(nombre.equals(DictionaryManager.NOMBRE.SINGULAR)){
                singular = unit.trim();
            }else if(nombre.equals((DictionaryManager.NOMBRE.PLURAL))){
                plural = unit.trim();
            }
        }
    }

    public void set(@NotNull String singular, @NotNull String plural){
        set(DictionaryManager.NOMBRE.SINGULAR, singular.trim());
        set(DictionaryManager.NOMBRE.PLURAL, plural.trim());
    }

    public boolean contains(String unit){
        return unit != null && (singular.equals(unit.trim()) || plural.equals(unit.trim()));
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = super.toJSON();
        try{
            jsonObject.put(JSON_KEY_SINGULAR, singular);
            jsonObject.put(JSON_KEY_PLURAL, plural);
        }catch(JSONException ignored){
        }
        return jsonObject;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Couple)){
            return false;
        }
        Couple couple = (Couple)o;
        return singular.equals(couple.singular) &&
                plural.equals(couple.plural);
    }

    @Override
    public int hashCode(){
        return Objects.hash(singular, plural);
    }
}
