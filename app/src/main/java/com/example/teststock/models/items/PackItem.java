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

    public PackItem(int ID, String name, int quantityOut, Couple unitInPackCouple, int nbPackFull, Couple packUnitCouple, int quantityMaxInPack, int seuil){
        super(ID, name, seuil);
        this.quantityMaxInPack = quantityMaxInPack;
        this.quantityOut = quantityOut;
        this.unitInPackCouple = unitInPackCouple;
        this.packUnitCouple = packUnitCouple;
        this.nbPackFull = nbPackFull;
    }

    public static @Nullable PackItem fromJSON(JSONObject json){
        try{
            return new PackItem(
                    json.getInt(JSON_KEY_ID),
                    json.getString(JSON_KEY_NAME),
                    json.getInt(JSON_KEY_QUANTITY_OUT),
                    (Couple)JSONable.fromJSON(json.getJSONObject(JSON_KEY_UNIT_IN_PACK_COUPLE)),
                    json.getInt(JSON_KEY_NB_PACK_FULL),
                    (Couple)JSONable.fromJSON(json.getJSONObject(JSON_KEY_PACK_UNIT_COUPLE)),
                    json.getInt(JSON_KEY_QUANTITY_MAX_IN_PACK),
                    json.getInt(JSON_KEY_SEUIL)
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

    public String getUnitInPack(){
        return getUnitInPack(DictionaryManager.getNombre(getQuantityOut()));
    }

    public boolean useUnitInPack(Couple couple){
        return couple != null && (getUnitInPackSingular().equals(couple.getSingular()) || getUnitInPackPlural().equals(couple.getPlural()));
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

    public int getTotalQuantity(){
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

    public String getPackUnit(){
        return getPackUnit(DictionaryManager.getNombre(getNbPackFull()));
    }

    public boolean usePackUnit(Couple couple){
        return couple != null && (getPackUnitSingular().equals(couple.getSingular()) || getPackUnitPlural().equals(couple.getPlural()));
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
        return String.format(Locale.getDefault(), "%d %s de %d %s", getSeuil(), getPackUnit(DictionaryManager.getNombre(getSeuil())), getQuantityMaxInPack(), getUnitInPack(DictionaryManager.getNombre(getQuantityMaxInPack())));
    }

    public String getQuantityOutFormated(){
        return String.format(Locale.getDefault(), "%d %s", getQuantityOut(), getUnitInPack());
    }

    public String getNbPackFullFormated(){
        return String.format(Locale.getDefault(), "%d %s de %d %s", getNbPackFull(), getPackUnit(), getQuantityMaxInPack(), getUnitInPack(DictionaryManager.getNombre(getQuantityMaxInPack())));
    }

    @Override
    public String getQuantityLeftFormated(){
        return getNbPackFullFormated();
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
