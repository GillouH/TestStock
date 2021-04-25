package com.example.teststock.models.items;

import com.example.teststock.models.Couple;
import com.example.teststock.models.JSONable;
import com.example.teststock.models.managers.DictionaryManager;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class BasicItem extends Item{
    private static final String JSON_KEY_QUANTITY = "JSON_KEY_QUANTITY";
    private static final String JSON_KEY_UNIT_COUPLE = "JSON_KEY_UNIT_COUPLE";

    private int quantity;
    private Couple coupleUnit;

    public BasicItem(int ID, String name, int quantity, Couple coupleUnit, int seuil, String image){
        super(ID, name, seuil, image);
        this.quantity = quantity;
        this.coupleUnit = coupleUnit;
    }

    public static @Nullable BasicItem fromJSON(JSONObject json){
        try{
            // TODO: 22/04/2021 Prochaine version, enlever la vérification
            String imageStr;
            if(json.has(JSON_KEY_IMAGE)){
                imageStr = json.getString(JSON_KEY_IMAGE);
            }else{
                imageStr = null;
            }
            return new BasicItem(
                    json.getInt(JSONable.JSON_KEY_ID),
                    json.getString(JSON_KEY_NAME),
                    json.getInt(JSON_KEY_QUANTITY),
                    (Couple)JSONable.fromJSON(json.getJSONObject(JSON_KEY_UNIT_COUPLE)),
                    json.getInt(JSON_KEY_SEUIL),
                    imageStr
            );
        }catch(Exception ignored){
        }
        return null;
    }

    public int getQuantity(){
        return quantity;
    }

    public Couple getCoupleUnit(){
        return coupleUnit;
    }

    public String getUnitSingular(){
        return getCoupleUnit().getSingular();
    }

    public String getUnitPlural(){
        return getCoupleUnit().getPlural();
    }

    public String getUnit(DictionaryManager.NOMBRE nombre){
        if(nombre == null){
            return null;
        }
        if(nombre.equals(DictionaryManager.NOMBRE.PLURAL)){
            return getUnitPlural();
        }else{ // if(nombre.equals(DictionaryManager.NOMBRE.SINGULAR))
            return getUnitSingular();
        }
    }

    public String getUnit(int nombre){
        return getUnit(DictionaryManager.getNombre(nombre));
    }

    public String getUnit(){
        return getUnit(getQuantity());
    }

    public void setUnitCouple(Couple coupleUnit){
        if(coupleUnit != null){
            this.coupleUnit = coupleUnit;
        }
    }

    @Override
    public boolean useUnit(Couple couple){
        return couple != null && (getUnitSingular().equals(couple.getSingular()) || getUnitPlural().equals(couple.getPlural()));
    }

    public SEND_NOTIFICATION modifyQuantity(int quantity){
        this.quantity += quantity;
        if(getQuantity() > seuil){
            return SEND_NOTIFICATION.NO;
        }else{
            if(getQuantity() < 0){
                this.quantity = 0;
            }
            return SEND_NOTIFICATION.YES;
        }
    }

    @Override
    public String getSeuilFormated(){
        return String.format(Locale.getDefault(), "%d %s", getSeuil(), getUnit(getSeuil()));
    }

    public String getQuantityFormated(){
        return String.format(Locale.getDefault(), "%d %s", getQuantity(), getUnit());
    }

    @Override
    public String getQuantityLeftFormated(){
        return getQuantityFormated();
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = super.toJSON();
        try{
            jsonObject.put(JSON_KEY_QUANTITY, getQuantity());
            jsonObject.put(JSON_KEY_UNIT_COUPLE, getCoupleUnit().toJSON());
        }catch(JSONException ignored){
        }
        return jsonObject;
    }

    @Override
    public String toString(){
        return toJSON().toString();
    }
}