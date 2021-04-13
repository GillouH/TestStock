package com.example.teststock.models.managers;

import android.content.Context;
import android.content.Intent;

import com.example.teststock.R;
import com.example.teststock.controllers.activities.ItemDetailActivity;
import com.example.teststock.controllers.activities.MainActivity;
import com.example.teststock.models.Couple;
import com.example.teststock.models.items.BasicItem;
import com.example.teststock.models.items.Item;
import com.example.teststock.models.items.PackItem;
import com.example.teststock.models.notifications.NotificationButton;
import com.example.teststock.models.notifications.NotificationHelper;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ItemManager extends ListManager<Item>{
    public static final String INTENT_EXTRA_DATA_KEY_ID = "INTENT_EXTRA_DATA_KEY_ID";
    private static final String PREF_KEY_ITEM_LIST = "PREF_KEY_ITEM_LIST";
    private final DictionaryManager dictionaryManager;

    public ItemManager(Context context){
        super(context);
        dictionaryManager = new DictionaryManager(context);
    }

    @Override
    protected String getPrefKey(){
        return PREF_KEY_ITEM_LIST;
    }

    public @Nullable BasicItem createBasicItem(int ID, String name, int quantity, Couple couple, int seuil){
        if(couple == null){
            return null;
        }
        int unitIDInDictionary;
        if((unitIDInDictionary = dictionaryManager.getID(couple.getSingular())) != dictionaryManager.getID(couple.getPlural())){
            return null;
        }
        if(unitIDInDictionary == -1){
            dictionaryManager.addInList(couple);
        }
        return new BasicItem(ID, name, quantity, couple, seuil);
    }

    @Nullable
    public BasicItem createBasicItem(int ID, String name, int quantity, int unitIDInDictionary, int seuil){
        Couple couple = dictionaryManager.get(unitIDInDictionary);
        return couple != null ? createBasicItem(ID, name, quantity, couple, seuil) : null;
    }

    public @Nullable PackItem createPackItem(int ID, String name, int quantityOut, Couple unitInPackCouple, int nbPackFull, Couple packUnitCouple, int quantityMaxInPack, int seuil){
        if(unitInPackCouple == null || packUnitCouple == null){
            return null;
        }
        int unitInPackIDInDictionary, packUnitIDInDictionary;
        if(
                (unitInPackIDInDictionary = dictionaryManager.getID(unitInPackCouple.getSingular())) != dictionaryManager.getID(unitInPackCouple.getPlural()) ||
                        (packUnitIDInDictionary = dictionaryManager.getID(packUnitCouple.getSingular())) != dictionaryManager.getID(packUnitCouple.getPlural())){
            return null;
        }
        if(unitInPackIDInDictionary == -1){
            dictionaryManager.addInList(unitInPackCouple);
        }
        if(packUnitIDInDictionary == -1){
            dictionaryManager.addInList(packUnitCouple);
        }
        return new PackItem(ID, name, quantityOut, unitInPackCouple, nbPackFull, packUnitCouple, quantityMaxInPack, seuil);
    }

    public PackItem createPackItem(int ID, String name, int quantityOut, int unitInPackIndexInDictionary, int nbPackFull, int packUnitIndexInDictionary, int quantityMaxInPack, int seuil){
        Couple unitInPackCouple = dictionaryManager.get(unitInPackIndexInDictionary);
        Couple packUnitCouple = dictionaryManager.get(packUnitIndexInDictionary);
        return unitInPackCouple != null && packUnitCouple != null ? createPackItem(
                ID,
                name,
                quantityOut,
                unitInPackCouple,
                nbPackFull,
                packUnitCouple,
                quantityMaxInPack,
                seuil
        ) : null;
    }

    protected @NotNull List<Item> createDefaultList(){
        List<Couple> defaultDictionary = new ArrayList<>();
        defaultDictionary.add(new Couple(-1, "bouteille", "bouteilles"));
        defaultDictionary.add(new Couple(-1, "boite", "boites"));
        defaultDictionary.add(new Couple(-1, "rouleau", "rouleaux"));
        defaultDictionary.add(new Couple(-1, "paquet", "paquets"));

        for(Couple couple : defaultDictionary){
            if(dictionaryManager.get(couple) == null){
                dictionaryManager.addInList(couple);
            }
        }

        List<Item> itemList = new ArrayList<>();
        int ID = 0;

        itemList.add(createBasicItem(ID++, "Gel WC", 4, dictionaryManager.getID("bouteille"), 2));
        itemList.add(createBasicItem(ID++, "Lingettes bébé", 2, dictionaryManager.getID("boite"), 1));
        itemList.add(createBasicItem(ID++, "Lingettes desinfectante", 2, dictionaryManager.getID("boite"), 1));
        itemList.add(createBasicItem(ID++, "Mouchoirs", 5, dictionaryManager.getID("boite"), 2));
        itemList.add(createBasicItem(ID++, "Gants", 8, dictionaryManager.getID("boite"), 1));
        itemList.add(createPackItem(ID++, "Papier toilette", 3, dictionaryManager.getID("rouleau"), 1, dictionaryManager.getID("paquet"), 8, 1));
        itemList.add(createPackItem(ID++, "Essuie mains", 2, dictionaryManager.getID("rouleau"), 3, dictionaryManager.getID("paquet"), 6, 5));
        itemList.add(createBasicItem(ID++, "Sacs poubelle (petit noir)", 5, dictionaryManager.getID("rouleau"), 3));
        itemList.add(createBasicItem(ID++, "Sacs poubelle (grand noir)", 3, dictionaryManager.getID("rouleau"), 2));
        itemList.add(createBasicItem(ID, "Sacs poubelle (blanc)", 1, dictionaryManager.getID("rouleau"), 1));

        return itemList;
    }

    public void sendNotification(Item item){
        if(item != null){
            String littleText = String.format("Restant : %s", item.getQuantityLeftFormated());
            String longText = String.format("%s\nSeuil : %s", littleText, item.getSeuilFormated());

            Intent mainIntent = new Intent(context, MainActivity.class);

            Intent buttonIntent = new Intent(context, ItemDetailActivity.class);
            buttonIntent.putExtra(INTENT_EXTRA_DATA_KEY_ID, item.getID());
            NotificationButton notificationButton = new NotificationButton(buttonIntent, "Ouvrir", R.drawable.ic_baseline_add_24);

            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.sendNotification(item.getName(), "Rupture de stock", littleText, longText, mainIntent, notificationButton, item.getID());
        }
    }

    public void sendNotification(List<Item> itemList, String unit, int id){
        if(itemList != null && itemList.size() > 0 && unit != null){
            StringBuilder longText = new StringBuilder(String.format("%s: ", unit));
            for(Item item : itemList.subList(0, itemList.size() - 1)){
                longText.append(String.format("\n\t%s", item.getName()));
            }
            longText.append(String.format("\n\t%s", itemList.get(itemList.size() - 1).getName()));

            Intent mainIntent = new Intent(context, MainActivity.class);

            NotificationHelper notificationHelper = new NotificationHelper(context);
            notificationHelper.sendNotification(context.getString(R.string.dictionary), "Item utilisant l'unitée :", unit, longText.toString(), mainIntent, id);
        }
    }
}
