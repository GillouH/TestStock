package com.example.teststock.models;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class PackItem extends Item{
    private static final String JSON_KEY_QUANTITYOUT = "JSON_KEY_QUANTITYOUT";
    private static final String JSON_KEY_UNITINPACK = "JSON_KEY_UNITINPACK";
    private static final String JSON_KEY_NBPACKFULL = "JSON_KEY_NBPACKFULL";
    private static final String JSON_KEY_PACKUNIT = "JSON_KEY_PACKUNIT";
    private static final String JSON_KEY_QUANTITYMAXINPACK = "JSON_KEY_QUANTITYMAXINPACK";
    private final int quantityMaxInPack;
    private int quantityOut;
    private final String unitInPack;
    private final String packUnit;
    private int nbPackFull;

    public PackItem(int ID, String name, int quantityOut, String unitInPack, int nbPackFull, String packUnit, int quantityMaxInPack, int seuil){
        super(ID, name, seuil);
        this.quantityOut = quantityOut;
        this.unitInPack = unitInPack;
        this.nbPackFull = nbPackFull;
        this.packUnit = packUnit;
        this.quantityMaxInPack = quantityMaxInPack;
    }

    public static @Nullable PackItem fromJSON(JSONObject json){
        try{
            return new PackItem(json.getInt(JSON_KEY_ID), json.getString(JSON_KEY_NAME), json.getInt(JSON_KEY_QUANTITYOUT), json.getString(JSON_KEY_UNITINPACK), json.getInt(JSON_KEY_NBPACKFULL), json.getString(JSON_KEY_PACKUNIT), json.getInt(JSON_KEY_QUANTITYMAXINPACK), json.getInt(JSON_KEY_SEUIL));
        }catch(Exception ignored){
        }
        return null;
    }

    public String getUnitInPack(DictionaryManager.NOMBRE nombre){
        if(nombre.equals(DictionaryManager.NOMBRE.PLURAL)){
            return unitInPackPlural;
        }else{ // if(nombre.equals(DictionaryManager.NOMBRE.SINGULAR))
            return unitInPackSingular;
        }
    }

    public SEND_NOTIFICATION modifyNbPack(int quantity){
        this.nbPackFull += quantity;
        if(this.nbPackFull > seuil){
            return SEND_NOTIFICATION.NO;
        }else{
            if(this.nbPackFull < 0){
                this.quantityOut = 0;
                this.nbPackFull = 0;
            }
            return SEND_NOTIFICATION.YES;
        }
    }

    public int getQuantityOut(){
        return quantityOut;
    }

    public String getUnitInPack(){
        return unitInPack;
    }

    public SEND_NOTIFICATION modifyQuantityOut(int quantity){
        this.quantityOut += quantity;
        while(this.quantityOut < 0){
            if(modifyNbPack(-1) == SEND_NOTIFICATION.YES){
                return SEND_NOTIFICATION.YES;
            }
            this.quantityOut += quantityMaxInPack;
        }
        while(this.quantityOut >= quantityMaxInPack){
            modifyNbPack(1);
            this.quantityOut -= quantityMaxInPack;
        }
        return SEND_NOTIFICATION.NO;
    }

    public int getNbPackFull(){
        return nbPackFull;
    }

    public String getPackUnit(){
        return packUnit;
    }

    public int getQuantityMaxInPack(){
        return quantityMaxInPack;
    }

    @Override
    public String getSeuilFormated(){
        return String.format(Locale.getDefault(), "%d %s de %d %s", getSeuil(), getPackUnit(), getQuantityMaxInPack(), getUnitInPack());
    }

    public String getQuantityOutFormated(){
        return String.format(Locale.getDefault(), "%d %s", getQuantityOut(), getUnitInPack());
    }

    public String getNbPackFullFormated(){
        return String.format(Locale.getDefault(), "%d %s de %d %s", getNbPackFull(), getPackUnit(), getQuantityMaxInPack(), getUnitInPack());
    }

    @Override
    public String getQuantityLeftFormated(){
        return getNbPackFullFormated();
    }

    public JSONObject toJSON(){
        JSONObject json = super.toJSON();
        try{
            json.put(JSON_KEY_QUANTITYOUT, quantityOut);
            json.put(JSON_KEY_UNITINPACK, unitInPack);
            json.put(JSON_KEY_NBPACKFULL, nbPackFull);
            json.put(JSON_KEY_PACKUNIT, packUnit);
            json.put(JSON_KEY_QUANTITYMAXINPACK, quantityMaxInPack);
        }catch(JSONException ignored){
        }
        return json;
    }

    @Override
    public String toString(){
        return toJSON().toString();
    }
}
