package com.example.teststock.models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;

import com.example.teststock.R;
import com.example.teststock.controllers.ItemDetailActivity;
import com.example.teststock.controllers.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class ItemManager{
    public static final String INTENT_EXTRA_DATA_KEY_ID = "INTENT_EXTRA_DATA_KEY_ID";
    private static final String MEMORY_FILE_NAME = "ITEM_LIST_MEMORY_FILE";
    private static final String PREF_KEY_ITEM_LIST = "PREF_KEY_ITEM_LIST";
    private final Context context;

    public ItemManager(Context context){
        this.context = context;
    }

    private void saveItemList(JSONArray jsonArray){
        if(jsonArray != null){
            context.getSharedPreferences(MEMORY_FILE_NAME, MODE_PRIVATE).edit().putString(PREF_KEY_ITEM_LIST, jsonArray.toString()).apply();
        }
    }

    public JSONArray convertItemListToJSONArray(List<Item> itemList){
        JSONArray jsonArray = new JSONArray();
        if(itemList != null){
            for(Item item : itemList){
                jsonArray.put(item.toJSON());
            }
        }
        return jsonArray;
    }

    public void saveItemList(List<Item> itemList){
        if(itemList != null){
            itemList.sort((Item o1, Item o2)->Integer.compare(o1.getID(), o2.getID()));
            updateListOrder(itemList);
            saveItemList(convertItemListToJSONArray(itemList));
        }
    }

    public void modifyItemInItemList(Item item){
        List<Item> itemList = getItemList(false);
        itemList.removeIf(itemInList->itemInList.getID() == item.getID());
        itemList.add(item);
        saveItemList(itemList);
    }

    private String getItemListAsString(){
        return context.getSharedPreferences(MEMORY_FILE_NAME, MODE_PRIVATE).getString(PREF_KEY_ITEM_LIST, null);
    }

    public JSONArray convertJSONArrayAsStringToJSONArray(String JSONArrayAsString){
        if(JSONArrayAsString != null){
            try{
                return new JSONArray(JSONArrayAsString);
            }catch(JSONException ignored){
            }
        }
        return new JSONArray();
    }

    public List<Item> convertJSONArrayToItemList(JSONArray jsonArray){
        List<Item> itemList = new ArrayList<>();
        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length(); i++){
                try{
                    itemList.add(Item.fromJSON(jsonArray.getJSONObject(i)));
                }catch(JSONException ignored){
                }
            }
        }
        return itemList;
    }

    public List<Item> getItemList(boolean newOne){
        List<Item> itemList;
        if(newOne){
            itemList = createDefaultList();
            saveItemList(itemList);
        }else{
            JSONArray jsonArray = convertJSONArrayAsStringToJSONArray(getItemListAsString());
            itemList = convertJSONArrayToItemList(jsonArray);
        }

        return itemList;
    }

    private @NotNull List<Item> createDefaultList(){
        List<Item> itemList = new ArrayList<>();

        // Item list initialization.
        int ID = 0;

        itemList.add(new BasicItem(ID++, "Gel WC", 4, "bouteille(s)", 2));
        itemList.add(new BasicItem(ID++, "Lingettes bébé", 2, "boîte(s)", 1));
        itemList.add(new BasicItem(ID++, "Lingettes desinfectante", 2, "boîte(s)", 1));
        itemList.add(new BasicItem(ID++, "Mouchoirs", 5, "boîte(s)", 2));
        itemList.add(new BasicItem(ID++, "Gants", 8, "boîte(s)", 1));
        itemList.add(new PackItem(ID++, "Papier toilette", 3, "rouleaux", 1, "paquet(s)", 8, 1));
        itemList.add(new PackItem(ID++, "Essuie mains", 2, "rouleaux", 3, "paquet(s)", 6, 1));
        itemList.add(new BasicItem(ID++, "Sacs poubelle (petit noir)", 5, "rouleau(x)", 3));
        itemList.add(new BasicItem(ID++, "Sacs poubelle (grand noir)", 3, "rouleau(x)", 2));
        itemList.add(new BasicItem(ID, "Sacs poubelle (blanc)", 1, "rouleau(x)", 1));

        return itemList;
    }

    public Item getItem(int itemID){
        List<Item> itemList = getItemList(false);
        Item itemReturned = null;
        for(Item item : itemList){
            if(item.getID() == itemID){
                itemReturned = item;
                break;
            }
        }
        return itemReturned;
    }

    public void updateListOrder(List<Item> itemList){
        if(itemList != null){
            for(int i = 0; i < itemList.size(); i++){
                itemList.get(i).setID(i);
            }
        }
    }

    public void addItemInItemList(Item item){
        if(item != null){
            List<Item> itemList = getItemList(false);
            item.setID(itemList.size());
            itemList.add(item);
            saveItemList(itemList);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void sendNotification(String channelID, String channelName, String title, String subTitle, String littleText, String longText, int notificationID){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel(channelID, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setShowBadge(true);
            if(notificationManager != null){
                notificationManager.createNotificationChannel(notificationChannel);
            }

            Notification.Builder notificationBbuilder = new Notification.Builder(context, channelID);
            notificationBbuilder.setContentTitle(title);
            notificationBbuilder.setSubText(subTitle);
            notificationBbuilder.setContentText(littleText);
            notificationBbuilder.setSmallIcon(R.drawable.ic_baseline_warning_24);

            Notification.InboxStyle inboxStyle = new Notification.InboxStyle();
            String[] stringArrayList = longText.split("\n");
            for(String string : stringArrayList){
                inboxStyle.addLine(string);
            }
            notificationBbuilder.setStyle(inboxStyle);

            Intent tapIntent = new Intent(context, ItemDetailActivity.class);
            tapIntent.putExtra(INTENT_EXTRA_DATA_KEY_ID, notificationID);
            tapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingTapIntent = PendingIntent.getActivity(context, 0, tapIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            notificationBbuilder.setContentIntent(pendingTapIntent);
            notificationBbuilder.setAutoCancel(false);


            Intent firstActionIntent = new Intent(context, MainActivity.class);
            firstActionIntent.setAction("Action");
            PendingIntent pendingFirstActionIntent = PendingIntent.getActivity(context, 0, firstActionIntent, 0);
            Icon icon = Icon.createWithResource(context, R.drawable.ic_baseline_add_24);
            Notification.Action.Builder notificationActionBuilder = new Notification.Action.Builder(icon, "Ouvrir", pendingFirstActionIntent);
            Notification.Action action = notificationActionBuilder.build();
            notificationBbuilder.addAction(action);

            notificationBbuilder.setColor(context.getResources().getColor(R.color.colorPrimary, null));


            Notification notification = notificationBbuilder.build();
            if(notificationManager != null){
                notificationManager.notify(notificationID, notification);
            }
        }
    }

    public void sendNotification(Item item){
        if(item != null){
            String littleText = String.format("Restant : %s", item.getQuantityLeftFormated());
            String longText = String.format("%s\nSeuil : %s", littleText, item.getSeuilFormated());
            sendNotification("CHANNEL_ID", "CHANNEL_NAME", item.getName(), "Rupture de stock", littleText, longText, item.getID());
        }
    }
}
