package com.example.teststock.models.managers;

import android.content.Context;

import com.example.teststock.models.Couple;
import com.example.teststock.models.items.BasicItem;
import com.example.teststock.models.items.Item;
import com.example.teststock.models.items.PackItem;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DictionaryManager extends ListManager<Couple>{
    public static final String INTENT_EXTRA_DATA_NEW_UNIT = "INTENT_EXTRA_DATA_NEW_UNIT";
    public static final String INTENT_EXTRA_DATA_UNIT_ID = "INTENT_EXTRA_DATA_UNIT_ID";
    public static final String INTENT_EXTRA_DATA_UNIT = "INTENT_EXTRA_DATA_UNIT";
    public static final String INTENT_EXTRA_DATA_EDIT_TEXT_ID = "INTENT_EXTRA_DATA_EDIT_TEXT_DATA";
    private static final String PREF_KEY_DICTIONARY = "PREF_KEY_DICTIONARY";

    public DictionaryManager(Context context){
        super(context);
    }

    public static NOMBRE getNombre(int value){
        return value < 2 ? NOMBRE.SINGULAR : NOMBRE.PLURAL;
    }

    public void sortList(List<Couple> list){
        if(list != null){
            list.sort(
                    (Couple object1, Couple object2)->object1.getSingular().compareToIgnoreCase(object2.getSingular())
            );
        }
    }

    @Override
    protected String getPrefKey(){
        return PREF_KEY_DICTIONARY;
    }

    @Override
    protected @NotNull List<Couple> createDefaultList(){
        List<Couple> dictionary = new ArrayList<>();

        int ID = 0;

        dictionary.add(new Couple(ID++, "gant", "gants"));
        dictionary.add(new Couple(ID++, "couteau", "couteaux"));
        dictionary.add(new Couple(ID, "bocal", "bocaux"));

        return dictionary;
    }

    public Couple get(List<Couple> list, String unit){
        if(list != null && unit != null){
            for(Couple couple : list){
                if(couple.contains(unit)){
                    return couple;
                }
            }
        }
        return null;
    }

    public Couple get(String unit){
        return get(getList(), unit);
    }

    public Couple get(List<Couple> list, Couple couple){
        if(list != null && couple != null){
            for(Couple coupleInList : list){
                if(coupleInList.equals(couple)){
                    return coupleInList;
                }
            }
        }
        return null;
    }

    public Couple get(Couple couple){
        return get(getList(), couple);
    }

    public String get(List<Couple> list, int ID, NOMBRE nombre){
        Couple couple = get(list, ID);
        return couple != null ? couple.get(nombre) : null;

    }

    public String get(int ID, NOMBRE nombre){
        return get(getList(), ID, nombre);
    }

    public String get(List<Couple> list, String unit, NOMBRE nombre){
        Couple couple = get(list, unit);
        return couple != null ? couple.get(nombre) : null;
    }

    public String get(String unit, NOMBRE nombre){
        return get(getList(), unit, nombre);
    }

    public String getSingular(List<Couple> list, String unit){
        return get(list, unit, NOMBRE.SINGULAR);
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public @Nullable String getSingular(String unit){
        return getSingular(getList(), unit);
    }

    public String getSingular(List<Couple> list, int ID){
        return get(list, ID, NOMBRE.SINGULAR);
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public String getSingular(int ID){
        return getSingular(getList(), ID);
    }

    public String getPlural(List<Couple> list, String unit){
        return get(list, unit, NOMBRE.PLURAL);
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public @Nullable String getPlural(String unit){
        return getPlural(getList(), unit);
    }

    public String getPlural(List<Couple> list, int ID){
        return get(list, ID, NOMBRE.PLURAL);
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public String getPlural(int ID){
        return getPlural(getList(), ID);
    }

    public int getID(List<Couple> list, String unit){
        Couple couple = get(list, unit);
        return couple != null ? couple.getID() : -1;

    }

    public int getID(String unit){
        return getID(getList(), unit);
    }

    public int getID(List<Couple> list, Couple couple){
        Couple coupleInList = get(list, couple);
        return coupleInList != null ? coupleInList.getID() : -1;
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public int getID(Couple couple){
        return getID(getList(), couple);
    }

    public void addInList(List<Couple> list, String singular, String plural){
        if(singular != null && plural != null){
            addInList(list, new Couple(-1, singular, plural));
        }
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public void addInList(String singular, String plural){
        addInList(getList(), singular, plural);
    }

    public List<Item> listItemUsingUnit(List<Item> itemList, Couple couple){
        List<Item> itemListUsingUnit = new ArrayList<>();
        if(itemList != null && couple != null){
            for(Item item : itemList){
                if(item.useUnit(couple)){
                    itemListUsingUnit.add(item);
                }
            }
        }
        return itemListUsingUnit;
    }

    public @NotNull List<Item> listItemUsingUnit(Couple couple){
        ItemManager itemManager = new ItemManager(context);
        return listItemUsingUnit(itemManager.getList(), couple);
    }

    public List<Item> listItemUsingUnit(List<Item> itemList, int ID){
        return listItemUsingUnit(itemList, get(ID));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public List<Item> listItemUsingUnit(int ID){
        ItemManager itemManager = new ItemManager(context);
        return listItemUsingUnit(itemManager.getList(), ID);
    }

    public List<Item> listItemUsingUnit(List<Item> itemList, String unit){
        return listItemUsingUnit(itemList, get(unit));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public List<Item> listItemUsingUnit(String unit){
        ItemManager itemManager = new ItemManager(context);
        return listItemUsingUnit(itemManager.getList(), unit);
    }

    public boolean isUnitUsed(List<Item> itemList, Couple couple){
        return listItemUsingUnit(itemList, couple).size() > 0;
    }

    public boolean isUnitUsed(Couple couple){
        ItemManager itemManager = new ItemManager(context);
        return isUnitUsed(itemManager.getList(), couple);
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean isUnitUsed(List<Item> itemList, int ID){
        return isUnitUsed(itemList, get(ID));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean isUnitUsed(int ID){
        return isUnitUsed(get(ID));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean isUnitUsed(List<Item> itemList, String unit){
        return isUnitUsed(itemList, get(unit));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean isUnitUsed(String unit){
        return isUnitUsed(get(unit));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean replaceInList(List<Couple> dictionary, int ID, String singular, String plural){
        return replaceInList(dictionary, new Couple(ID, singular, plural));
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    public boolean replaceInList(int ID, String singular, String plural){
        return replaceInList(new Couple(ID, singular, plural));
    }

    @Contract("null -> new")
    public String @NotNull [] getUnitArray(NOMBRE nombre){
        if(nombre != null){
            List<Couple> dictionary = getList();
            String[] unitArray = new String[dictionary.size()];
            for(int i = 0; i < dictionary.size(); i++){
                unitArray[i] = dictionary.get(i).get(nombre);
            }
            return unitArray;
        }
        return new String[0];
    }

    public String @NotNull [] getUnitArray(){
        return getUnitArray(NOMBRE.SINGULAR);
    }

    public void updateUnitInItemList(List<Couple> newDictionary){
        if(newDictionary != null && newDictionary.size() > 0){
            PackItem packItem;
            List<Item> itemList;
            ItemManager itemManager = new ItemManager(context);
            for(Couple newCouple : newDictionary){
                Couple previousCouple = get(newCouple.getID());
                if(previousCouple != null && !newCouple.equals(previousCouple)){
                    itemList = listItemUsingUnit(previousCouple);
                    for(Item item : itemList){
                        if(item.getClass().equals(BasicItem.class)){
                            ((BasicItem)item).setUnitCouple(newCouple);
                        }else if(item.getClass().equals(PackItem.class)){
                            packItem = (PackItem)item;
                            if(packItem.usePackUnit(previousCouple)){
                                packItem.setPackUnitCouple(newCouple);
                            }else if(packItem.useUnitInPack(previousCouple)){
                                packItem.setUnitInPackCouple(newCouple);
                            }
                        }
                        itemManager.replaceInList(item);
                    }
                }
            }
            saveList(newDictionary);
        }
    }

    public enum NOMBRE{
        SINGULAR,
        PLURAL
    }
}
