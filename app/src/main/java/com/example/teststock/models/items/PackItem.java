package com.example.teststock.models.items;

import com.example.teststock.models.Couple;
import com.example.teststock.models.JSONable;
import com.example.teststock.models.managers.DictionaryManager;

import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class PackItem extends Item{
    private static final String JSON_KEY_QUANTITY_OUT = "JSON_KEY_QUANTITY_OUT";
    private static final String JSON_KEY_UNIT_IN_PACK_COUPLE = "JSON_KEY_UNIT_IN_PACK_COUPLE";
    private static final String JSON_KEY_NB_PACK_FULL = "JSON_KEY_NB_PACK_FULL";
    private static final String JSON_KEY_PACK_UNIT_COUPLE = "JSON_KEY_PACK_UNIT_COUPLE";
    private static final String JSON_KEY_QUANTITY_MAX_IN_PACK = "JSON_KEY_QUANTITY_MAX_IN_PACK";

    private final int quantityMaxInPack;
    private int quantityOut;
    private Couple unitInPackCouple;
    private Couple packUnitCouple;
    private int nbPackFull;

    public PackItem(int ID, String name, int quantityOut, Couple unitInPackCouple, int nbPackFull, Couple packUnitCouple, int quantityMaxInPack, int seuil, String image){
        super(ID, name, seuil, image);
        this.quantityMaxInPack = quantityMaxInPack;
        this.quantityOut = quantityOut;
        this.unitInPackCouple = unitInPackCouple;
        this.packUnitCouple = packUnitCouple;
        this.nbPackFull = nbPackFull;
    }

    public static @Nullable PackItem fromJSON(JSONObject json){
        try{
            String imageStr;
            if(json.has(JSON_KEY_IMAGE)){
                imageStr = json.getString(JSON_KEY_IMAGE);
            }else{
                imageStr = null;
            }
            return new PackItem(
                    json.getInt(JSON_KEY_ID),
                    json.getString(JSON_KEY_NAME),
                    json.getInt(JSON_KEY_QUANTITY_OUT),
                    (Couple)JSONable.fromJSON(json.getJSONObject(JSON_KEY_UNIT_IN_PACK_COUPLE)),
                    json.getInt(JSON_KEY_NB_PACK_FULL),
                    (Couple)JSONable.fromJSON(json.getJSONObject(JSON_KEY_PACK_UNIT_COUPLE)),
                    json.getInt(JSON_KEY_QUANTITY_MAX_IN_PACK),
                    json.getInt(JSON_KEY_SEUIL),
                    imageStr
            );
        }catch(Exception ignored){
        }
        return null;
    }

    public int getQuantityOut(){
        return quantityOut;
    }

    public Couple getUnitInPackCouple(){
        return unitInPackCouple;
    }

    public void setUnitInPackCouple(Couple couple){
        if(couple != null){
            this.unitInPackCouple = couple;
        }
    }

    public String getUnitInPackSingular(){
        return getUnitInPackCouple().getSingular();
    }

    public String getUnitInPackPlural(){
        return getUnitInPackCouple().getPlural();
    }

    public String getUnitInPack(DictionaryManager.NOMBRE nombre){
        if(nombre == null){
            return null;
        }
        if(nombre.equals(DictionaryManager.NOMBRE.PLURAL)){
            return getUnitInPackPlural();
        }else{ // if(nombre.equals(DictionaryManager.NOMBRE.SINGULAR))
            return getUnitInPackSingular();
        }
    }

    public String getUnitInPack(int nombre){
        return getUnitInPack(DictionaryManager.getNombre(nombre));
    }

    public String getUnitInPack(){
        return getUnitInPack(getQuantityOut());
    }

    public boolean useUnitInPack(Couple couple){
        return couple != null && (getUnitInPackSingular().equals(couple.getSingular()) || getUnitInPackPlural().equals(couple.getPlural()));
    }

    public SEND_NOTIFICATION modifyQuantityOut(int quantity){
        SEND_NOTIFICATION sendNotification = SEND_NOTIFICATION.NO;
        if(quantity > 0 || -quantity <= quantityOut + quantityMaxInPack * nbPackFull){
            this.quantityOut += quantity;
            while(quantityOut < 0){
                modifyNbPack(-1);
                quantityOut += quantityMaxInPack;
            }
            while(quantityOut >= quantityMaxInPack){
                modifyNbPack(1);
                quantityOut -= quantityMaxInPack;
            }
            if(getQuantityLeft() <= seuil){
                sendNotification = SEND_NOTIFICATION.YES;
            }
        }
        return sendNotification;
    }

    public int getNbPackFull(){
        return nbPackFull;
    }

    @Override
    public int getQuantityLeft(){
        return quantityOut + quantityMaxInPack * nbPackFull;
    }

    public Couple getPackUnitCouple(){
        return packUnitCouple;
    }

    public void setPackUnitCouple(Couple couple){
        if(couple != null){
            this.packUnitCouple = couple;
        }
    }

    public String getPackUnitSingular(){
        return getPackUnitCouple().getSingular();
    }

    public String getPackUnitPlural(){
        return getPackUnitCouple().getPlural();
    }

    public String getPackUnit(DictionaryManager.NOMBRE nombre){
        if(nombre == null){
            return null;
        }
        if(nombre.equals(DictionaryManager.NOMBRE.PLURAL)){
            return getPackUnitPlural();
        }else{ // if(nombre.equals(DictionaryManager.NOMBRE.SINGULAR))
            return getPackUnitSingular();
        }
    }

    public String getPackUnit(int nombre){
        return getPackUnit(DictionaryManager.getNombre(nombre));
    }

    public String getPackUnit(){
        return getPackUnit(getNbPackFull());
    }

    public boolean usePackUnit(Couple couple){
        return couple != null && (getPackUnitSingular().equals(couple.getSingular()) || getPackUnitPlural().equals(couple.getPlural()));
    }

    public SEND_NOTIFICATION modifyNbPack(int quantity){
        if(quantity > 0 || -quantity <= nbPackFull){
            nbPackFull += quantity;
        }
        return getQuantityLeft() <= seuil ? SEND_NOTIFICATION.YES : SEND_NOTIFICATION.NO;
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public void setUnitCouple(Couple unitInPackCouple, Couple packUnitCouple){
        if(unitInPackCouple != null){
            setUnitInPackCouple(unitInPackCouple);
        }
        if(packUnitCouple != null){
            setPackUnitCouple(packUnitCouple);
        }
    }

    public boolean useUnit(Couple couple){
        return couple != null && (useUnitInPack(couple) || usePackUnit(couple));
    }

    public int getQuantityMaxInPack(){
        return quantityMaxInPack;
    }

    @Override
    public String getSeuilFormated(){
        return String.format(Locale.getDefault(), "%d %s", getSeuil(), getUnitInPack(getSeuil()));
    }

    public String getQuantityOutFormated(){
        return String.format(Locale.getDefault(), "%d %s", getQuantityOut(), getUnitInPack());
    }

    public String getNbPackFullFormated(){
        return String.format(Locale.getDefault(), "%d %s de %d %s", getNbPackFull(), getPackUnit(), getQuantityMaxInPack(), getUnitInPack(getQuantityMaxInPack()));
    }

    @Override
    public String getQuantityLeftFormated(){
        return String.format(Locale.getDefault(), "%d %s", getQuantityLeft(), getUnitInPack(getQuantityLeft()));
    }

    public JSONObject toJSON(){
        JSONObject jsonObject = super.toJSON();
        try{
            jsonObject.put(JSON_KEY_QUANTITY_OUT, getQuantityOut());
            jsonObject.put(JSON_KEY_UNIT_IN_PACK_COUPLE, getUnitInPackCouple().toJSON());
            jsonObject.put(JSON_KEY_NB_PACK_FULL, getNbPackFull());
            jsonObject.put(JSON_KEY_PACK_UNIT_COUPLE, getPackUnitCouple().toJSON());
            jsonObject.put(JSON_KEY_QUANTITY_MAX_IN_PACK, getQuantityMaxInPack());
        }catch(JSONException ignored){
        }
        return jsonObject;
    }

    @Override
    public String toString(){
        return toJSON().toString();
    }
}
