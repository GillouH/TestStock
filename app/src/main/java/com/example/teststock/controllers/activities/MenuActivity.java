package com.example.teststock.controllers.activities;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.teststock.R;
import com.example.teststock.controllers.CustomActivity;
import com.example.teststock.models.CustomLog;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MenuActivity extends CustomActivity{
    protected static final String BUNDLE_STATE_MODE = "BUNDLE_STATE_MODE";

    protected ACTION_MODE actionMode;
    protected boolean showSubMenu;
    protected List<Integer> subMenuItemToShow;
    protected boolean hasMenu = true;

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onCreateOptionsMenu()");
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
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "onOptionsItemSelected()");
        int itemID = item.getItemId();
        ArrayList<Integer> menuItemsIDToActionModeNormal = new ArrayList<>(Arrays.asList(R.id.menu_button_cancel, R.id.menu_button_ok, R.id.menu_submenu_button_resetButton));
        if(menuItemsIDToActionModeNormal.contains(itemID)){
            actionMode = ACTION_MODE.NORMAL;
            if(itemID == R.id.menu_button_cancel){
                customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickCancelButton");
            }else if(itemID == R.id.menu_button_ok){
                customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickOkButton");
            }else if(itemID == R.id.menu_submenu_button_resetButton){
                customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickResetButton");
            }
        }else if(itemID == R.id.menu_submenu_button_modifyOrder){
            customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickModifyOrderButton");
            actionMode = ACTION_MODE.MOVE;
        }else if(itemID == R.id.menu_submenu_button_edit){
            customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickEditButton");
            actionMode = ACTION_MODE.EDIT;
        }else if(itemID == R.id.menu_submenu_button_removeItem){
            customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickRemoveItemButton");
            actionMode = ACTION_MODE.DELETE;
        }else if(itemID == R.id.menu_submenu_button_dictionary){
            customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickDictionaryButton");
            Intent intent = new Intent(this, DictionaryActivity.class);
            startActivity(intent);
        }else if(itemID == R.id.menu_submenu_button_test){
            customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickTestActivityButton");
            Intent intent = new Intent(this, TestActivity.class);
            startActivity(intent);
        }else if(itemID == R.id.menu_submenu_button_import){
            customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickImportActivityButton");
            Intent intent = new Intent(this, ImportActivity.class);
            startActivity(intent);
        }else if(itemID == R.id.menu_submenu_button_export){
            customLog.write(CustomLog.TYPE.CLICK, getClass(), "clickExportButton");
            export();
        }else{
            return super.onOptionsItemSelected(item);
        }
        return false;
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
        if(clipboardManager != null){
            clipboardManager.setPrimaryClip(
                    ClipData.newPlainText(
                            "List to export",
                            jsonObjectToExport.toString()
                    )
            );
            Toast.makeText(this, "Liste copiée dans le presse-papier", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Problème lors de l'export des données", Toast.LENGTH_LONG).show();
        }
    }

    public enum ACTION_MODE{
        NORMAL,
        MOVE,
        EDIT,
        DELETE
    }
}
