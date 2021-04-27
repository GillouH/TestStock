package com.example.teststock.controllers;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.teststock.R;
import com.example.teststock.controllers.activities.DictionaryActivity;
import com.example.teststock.controllers.activities.ImportActivity;
import com.example.teststock.controllers.activities.TestActivity;
import com.example.teststock.models.PersonalLog;
import com.example.teststock.models.managers.DictionaryManager;
import com.example.teststock.models.managers.ItemManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalActivity extends AppCompatActivity{
    protected static final String BUNDLE_STATE_MODE = "BUNDLE_STATE_MODE";
    protected static final PersonalLog personalLog = new PersonalLog();
    protected final ItemManager itemManager;
    protected final DictionaryManager dictionaryManager;
    protected ACTION_MODE actionMode;
    protected boolean showSubMenu;
    protected List<Integer> subMenuItemToShow;
    protected boolean hasMenu = false;

    public PersonalActivity(){
        super();
        itemManager = new ItemManager(this);
        dictionaryManager = new DictionaryManager(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), String.format("onCreateOptionsMenu(%b)", hasMenu));
        if(hasMenu){
            getMenuInflater().inflate(R.menu.menu, menu);
            menu.setGroupVisible(R.id.menu_submenu_buttons, showSubMenu);
            menu.setGroupVisible(R.id.menu_buttons, !showSubMenu);
            if(showSubMenu && subMenuItemToShow != null){
                MenuItem menuItem;
                for(int i = 0; i < menu.size(); i++){
                    menuItem = menu.getItem(i);
                    menuItem.setVisible(subMenuItemToShow.contains(menuItem.getItemId()));
                }
                menu.findItem(R.id.menu_submenu_button_resetButton).setVisible(false);
                menu.findItem(R.id.menu_submenu_button_test).setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onOptionsItemSelected()");
        int itemID = item.getItemId();
        ArrayList<Integer> menuItemsIDToActionModeNormal = new ArrayList<>(Arrays.asList(R.id.menu_button_cancel, R.id.menu_button_ok, R.id.menu_submenu_button_resetButton));
        if(menuItemsIDToActionModeNormal.contains(itemID)){
            actionMode = PersonalActivity.ACTION_MODE.NORMAL;
            if(itemID == R.id.menu_button_cancel){
                personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickCancelButton");
            }else if(itemID == R.id.menu_button_ok){
                personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickOkButton");
            }else if(itemID == R.id.menu_submenu_button_resetButton){
                personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickResetButton");
            }
        }else if(itemID == R.id.menu_submenu_button_modifyOrder){
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickModifyOrderButton");
            actionMode = PersonalActivity.ACTION_MODE.MOVE;
        }else if(itemID == R.id.menu_submenu_button_edit){
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickEditButton");
            actionMode = PersonalActivity.ACTION_MODE.EDIT;
        }else if(itemID == R.id.menu_submenu_button_removeItem){
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickRemoveItemButton");
            actionMode = PersonalActivity.ACTION_MODE.DELETE;
        }else if(itemID == R.id.menu_submenu_button_dictionary){
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickDictionaryButton");
            Intent intent = new Intent(this, DictionaryActivity.class);
            startActivity(intent);
        }else if(itemID == R.id.menu_submenu_button_test){
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickTestActivityButton");
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        }else if(itemID == R.id.menu_submenu_button_import){
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickImportActivityButton");
            Intent intent = new Intent(this, ImportActivity.class);
            startActivity(intent);
        }else if(itemID == R.id.menu_submenu_button_export){
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickExportButton");
            export();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onCreate()");
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onStart()");
        super.onStart();
    }

    @Override
    protected void onResume(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onResume()");
        super.onResume();
    }

    @Override
    protected void onPause(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onPause()");
        super.onPause();
    }

    @Override
    protected void onStop(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onStop()");
        super.onStop();
    }

    @Override
    protected void onRestart(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onRestart()");
        super.onRestart();
    }

    @Override
    protected void onDestroy(){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onDestroy()");
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp(){
        personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickNavigateUpButton");
        if(!super.onSupportNavigateUp()){
            finish();
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onSaveInstanceState()");
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        personalLog.write(PersonalLog.TYPE.METHOD, getClass(), "onRestoreInstanceState()");
        super.onRestoreInstanceState(savedInstanceState);
    }

    public enum ACTION_MODE{
        NORMAL,
        MOVE,
        EDIT,
        DELETE
    }

    private void export(){
        JSONObject jsonObjectToExport = new JSONObject();
        try{
            jsonObjectToExport.put(dictionaryManager.getPrefKey(), dictionaryManager.convertListToString(dictionaryManager.getList()));
            jsonObjectToExport.put(itemManager.getPrefKey(), itemManager.convertListToString(itemManager.getList()));
        }catch(JSONException e){
            e.printStackTrace();
        }
        ClipboardManager clipboardManager = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(
                ClipData.newPlainText(
                        "List to export",
                        jsonObjectToExport.toString()
                )
        );
        Toast.makeText(this, "Liste copiée dans le presse-papier", Toast.LENGTH_LONG).show();
    }
}
