package com.example.teststock.models.items;

import com.example.teststock.R;
import com.example.teststock.models.Couple;
import com.example.teststock.models.JSONable;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class Item extends JSONable{
    protected static final String JSON_KEY_NAME = "JSON_KEY_NAME";
    protected static final String JSON_KEY_SEUIL = "JSON_KEY_SEUIL";
    public static final String JSON_VALUE_IMAGE_NULL = "JSON_VALUE_IMAGE_NULL";
    public static final int drawableIDDefaultItemPicture = R.drawable.ic_baseline_photo_size_select_actual_24;
    protected static final String JSON_KEY_IMAGE = "JSON_KEY_IMAGE";

    protected final int seuil;
    private final String name;
    private final String image;

    public Item(int ID, @NotNull String name, int seuil, String image){
        super(ID);
        String nameTrim = name.trim();
        this.name = nameTrim.substring(0, 1).toUpperCase() + nameTrim.substring(1);
        this.seuil = seuil;
        this.image = image == null ? JSON_VALUE_IMAGE_NULL : image;
    }

    public String getName(){
        return name;
    }

    public int getSeuil(){
        return seuil;
    }

    public String getImage(){
        return image;
    }

    public abstract boolean useUnit(Couple couple);

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean useUnit(String unit){
        return useUnit(new Couple(-1, unit, unit));
    }

    public abstract String getSeuilFormated();

    public abstract String getQuantityLeftFormated();

    public JSONObject toJSON(){
        JSONObject jsonObject = super.toJSON();
        try{
            jsonObject.put(JSON_KEY_NAME, getName());
            jsonObject.put(JSON_KEY_SEUIL, getSeuil());
            jsonObject.put(JSON_KEY_IMAGE, getImage());
        }catch(JSONException ignored){
        }
        return jsonObject;
    }

    @Override
    public String toString(){
        return toJSON().toString();
    }

    public enum SEND_NOTIFICATION{
        YES,
        NO
    }
}
