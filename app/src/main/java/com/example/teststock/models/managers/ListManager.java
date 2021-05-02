package com.example.teststock.models.managers;

import android.content.Context;

import com.example.teststock.models.JSONable;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

abstract class ListManager<T extends JSONable>{
    private static final String MEMORY_FILE_NAME = "com.example.teststock.models.manager";

    final Context context;

    ListManager(Context context){
        this.context = context;
    }

    public abstract String getPrefKey();

    public void saveList(List<T> list){
        if(list != null){
            sortList(list);
            updateListOrder(list);
            context
                    .getSharedPreferences(MEMORY_FILE_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .putString(getPrefKey(), convertListToString(list))
                    .apply();
        }
    }

    public List<T> getList(){
        return getList(false);
    }

    public List<T> getList(boolean newOne){
        List<T> list;
        if(newOne){
            list = createDefaultList();
            saveList(list);
        }else{
            String string = context
                    .getSharedPreferences(MEMORY_FILE_NAME, Context.MODE_PRIVATE)
                    .getString(getPrefKey(), null);
            if(string == null){
                return getList(true);
            }else{
                list = convertStringToList(string);
            }
        }
        return list;
    }

    public T get(List<T> list, int ID){
        if(list != null){
            for(T object : list){
                if(object.getID() == ID){
                    return object;
                }
            }
        }
        return null;
    }

    public T get(int ID){
        return get(getList(), ID);
    }

    public void updateListOrder(List<T> list){
        if(list != null){
            for(int i = 0; i < list.size(); i++){
                list.get(i).setID(i);
            }
        }
    }

    @Contract("null -> new")
    public @NotNull JSONArray convertStringToJSONArray(String string){
        if(string != null){
            try{
                return new JSONArray(string);
            }catch(JSONException ignored){
            }
        }
        return new JSONArray();
    }

    public @NotNull List<T> convertJSONArrayToList(JSONArray jsonArray){
        List<T> list = new ArrayList<>();
        JSONObject jsonObject;
        if(jsonArray != null){
            for(int i = 0; i < jsonArray.length(); i++){
                try{
                    jsonObject = jsonArray.getJSONObject(i);
                }catch(JSONException ignored){
                    jsonObject = new JSONObject();
                }
                //noinspection unchecked
                list.add((T)JSONable.fromJSON(jsonObject));
            }
        }
        return list;
    }

    @NotNull
    public List<T> convertStringToList(String string){
        return convertJSONArrayToList(convertStringToJSONArray(string));
    }

    public @NotNull JSONArray convertListToJSONArray(List<T> list){
        JSONArray jsonArray = new JSONArray();
        if(list != null){
            for(T object : list){
                jsonArray.put(object.toJSON());
            }
        }
        return jsonArray;
    }

    public String convertJSONArrayToString(JSONArray jsonArray){
        String string = "";
        if(jsonArray != null){
            string += jsonArray.toString();
        }
        return string;
    }

    public String convertListToString(List<T> list){
        return convertJSONArrayToString(convertListToJSONArray(list));
    }

    protected abstract @NotNull List<T> createDefaultList();

    public boolean replaceInList(List<T> list, T object){
        boolean replaced = false;
        if(list != null && object != null){
            replaced = list.removeIf(objectInList->objectInList.getID() == object.getID());
            if(replaced){
                list.add(object);
            }
            sortList(list);
        }
        return replaced;
    }

    public boolean replaceInList(T object){
        List<T> list = getList();
        boolean replaced = replaceInList(list, object);
        if(replaced){
            saveList(list);
        }
        return replaced;
    }

    public void addInList(List<T> list, T object){
        if(list != null && object != null){
            object.setID(list.size());
            list.add(object);
            sortList(list);
        }
    }

    public void addInList(T object){
        if(object != null){
            List<T> list = getList();
            addInList(list, object);
            saveList(list);
        }
    }

    public void sortList(List<T> list){
        if(list != null){
            list.sort(
                    (T object1, T object2)->Integer.compare(object1.getID(), object2.getID())
            );
        }
    }
}
