package com.example.teststock.controllers.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.teststock.R;
import com.example.teststock.models.Couple;
import com.example.teststock.models.CustomLog;
import com.example.teststock.models.managers.DictionaryManager;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class DictionaryActivity extends MenuActivity{
    private static final String BUNDLE_STATE_DICTIONARY = "BUNDLE_STATE_DICTIONARY";
    private static final String BUNDLE_STATE_EDITING = "BUNDLE_STATE_EDITING";
    private static final String BUNDLE_STATE_ROW_INDEX = "BUNDLE_STATE_ROW_INDEX";
    private static final String BUNDLE_STATE_EDIT_SINGULAR = "BUNDLE_STATE_EDIT_SINGULAR";
    private static final String BUNDLE_STATE_EDIT_PLURAL = "BUNDLE_STATE_EDIT_PLURAL";

    private List<Couple> dictionary;

    private TableLayout tableLayout;
    private Drawable drawableEdit, drawableDelete, drawableWarning;
    private Button addSaveButton, cancelButton;

    private boolean editing;
    private int rowIndexEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary);

        Toolbar toolbar = findViewById(R.id.activityDictionary_toolbar);
        tableLayout = findViewById(R.id.activityDictionary_tableLayout);
        addSaveButton = findViewById(R.id.activityDictionary_addSaveButton);
        cancelButton = findViewById(R.id.activityDictionary_cancelButton);

        subMenuItemToShow = Arrays.asList(R.id.menu_submenu_button_removeItem, R.id.menu_submenu_button_edit);
        setSupportActionBar(toolbar);

        drawableEdit = ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24);
        drawableDelete = ContextCompat.getDrawable(this, R.drawable.ic_baseline_delete_24);
        drawableWarning = ContextCompat.getDrawable(this, R.drawable.ic_baseline_warning_24);

        Intent intent;

        if((intent = getIntent()).hasExtra(DictionaryManager.INTENT_EXTRA_DATA_NEW_UNIT)){
            actionMode = ACTION_MODE.NORMAL;
            cancelButton.setOnClickListener(v->onSupportNavigateUp());
            hasMenu = false;
            showSubMenu = false;
            editing = true;
            dictionary = dictionaryManager.getList();
            displayTable();
            if(savedInstanceState == null){
                boolean newUnit = intent.getBooleanExtra(DictionaryManager.INTENT_EXTRA_DATA_NEW_UNIT, false);
                if(newUnit){
                    String unit = intent.getStringExtra(DictionaryManager.INTENT_EXTRA_DATA_UNIT);
                    addEditRow(unit, null, false);
                }else{
                    int unitIDInDictionary = intent.getIntExtra(DictionaryManager.INTENT_EXTRA_DATA_UNIT_ID, -1);
                    editRow((TableRow)tableLayout.getChildAt(unitIDInDictionary + 1), unitIDInDictionary, false);
                }
            }else{
                int rowIndexEditing = savedInstanceState.getInt(BUNDLE_STATE_ROW_INDEX);
                String singular = savedInstanceState.getString(BUNDLE_STATE_EDIT_SINGULAR);
                String plural = savedInstanceState.getString(BUNDLE_STATE_EDIT_PLURAL);
                if(rowIndexEditing == tableLayout.getChildCount()){
                    addEditRow(singular, plural, false);
                }else{
                    TableRow tableRow = (TableRow)tableLayout.getChildAt(rowIndexEditing);
                    editRow(tableRow, rowIndexEditing - 1, false);
                    ((EditText)tableRow.getChildAt(0)).setText(singular);
                    ((EditText)tableRow.getChildAt(1)).setText(plural);
                }
            }
        }else{
            hasMenu = true;
            cancelButton.setOnClickListener(v->cancelEditing());
            if(savedInstanceState == null){
                actionMode = ACTION_MODE.NORMAL;
                showSubMenu = true;
                dictionary = dictionaryManager.getList();
                editing = false;
                displayTable();
            }else{
                actionMode = ACTION_MODE.valueOf(savedInstanceState.getString(BUNDLE_STATE_MODE));
                dictionary = dictionaryManager.convertStringToList(savedInstanceState.getString(BUNDLE_STATE_DICTIONARY));
                editing = savedInstanceState.getBoolean(BUNDLE_STATE_EDITING);
                showSubMenu = actionMode.equals(ACTION_MODE.NORMAL) && !editing;
                displayTable();
                if(editing){
                    String singular = savedInstanceState.getString(BUNDLE_STATE_EDIT_SINGULAR);
                    String plural = savedInstanceState.getString(BUNDLE_STATE_EDIT_PLURAL);
                    int rowIndexEditing = savedInstanceState.getInt(BUNDLE_STATE_ROW_INDEX);
                    if(rowIndexEditing == tableLayout.getChildCount()){
                        addEditRow(singular, plural);
                    }else{
                        TableRow tableRow = (TableRow)tableLayout.getChildAt(rowIndexEditing);
                        editRow(tableRow, rowIndexEditing - 1);
                        ((EditText)tableRow.getChildAt(0)).setText(singular);
                        ((EditText)tableRow.getChildAt(1)).setText(plural);
                    }
                }
            }
        }
    }

    private void refresh(){
        customLog.write(CustomLog.TYPE.METHOD, getClass(), "refresh()");
        showSubMenu = actionMode.equals(ACTION_MODE.NORMAL);
        invalidateOptionsMenu();
        displayTable();
    }

    @Override
    protected void onSaveInstanceState(@NotNull Bundle outState){
        outState.putString(BUNDLE_STATE_MODE, actionMode.toString());
        outState.putString(BUNDLE_STATE_DICTIONARY, dictionaryManager.convertListToString(dictionary));
        outState.putBoolean(BUNDLE_STATE_EDITING, editing);
        if(editing){
            String singular, plural;
            TableRow tableRow = (TableRow)tableLayout.getChildAt(rowIndexEditing);
            singular = ((TextView)tableRow.getChildAt(0)).getText().toString();
            plural = ((TextView)tableRow.getChildAt(1)).getText().toString();
            outState.putString(BUNDLE_STATE_EDIT_SINGULAR, singular);
            outState.putString(BUNDLE_STATE_EDIT_PLURAL, plural);
            outState.putInt(BUNDLE_STATE_ROW_INDEX, rowIndexEditing);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item){
        boolean result = super.onOptionsItemSelected(item);
        int itemID = item.getItemId();
        if(itemID == R.id.menu_submenu_button_resetButton){
            dictionary = dictionaryManager.getList(true);
        }else if(itemID == R.id.menu_button_ok){
            dictionaryManager.updateUnitInItemList(dictionary);
        }else if(itemID == R.id.menu_button_cancel){
            dictionary = dictionaryManager.getList();
        }
        refresh();
        return result;
    }

    private void displayTable(){
        int nbRow = tableLayout.getChildCount();
        if(nbRow > 1){
            tableLayout.removeViews(1, nbRow - 1);
        }

        TextView textViewSingular, textViewPlural;
        ImageView imageView;

        for(Couple couple : dictionary){
            final TableRow tableRow = new TableRow(this);
            tableLayout.addView(tableRow);

            textViewSingular = new TextView(this, null, R.style.Theme_TestStock, R.style.DictionaryActivity_TableLayout_TextView);
            textViewSingular.setText(couple.getSingular());
            tableRow.addView(textViewSingular);

            textViewPlural = new TextView(this, null, R.style.Theme_TestStock, R.style.DictionaryActivity_TableLayout_TextView);
            textViewPlural.setText(couple.getPlural());
            tableRow.addView(textViewPlural);

            if(actionMode.equals(ACTION_MODE.EDIT) || actionMode.equals(ACTION_MODE.DELETE)){
                imageView = new ImageView(this, null, R.style.Theme_TestStock, R.style.ImageView);
                if(actionMode.equals(ACTION_MODE.EDIT)){
                    imageView.setContentDescription(getString(R.string.edit));
                    imageView.setImageDrawable(drawableEdit);
                    imageView.setOnClickListener(v->editRow(tableRow, couple.getID()));
                }else if(actionMode.equals(ACTION_MODE.DELETE)){
                    if(dictionaryManager.isUnitUsed(couple)){
                        imageView.setContentDescription(getString(R.string.warning));
                        imageView.setImageDrawable(drawableWarning);
                        imageView.setOnClickListener(v->itemManager.sendNotification(dictionaryManager.listItemUsingUnit(couple), couple.getSingular(), -couple.getID()));
                    }else{
                        imageView.setContentDescription(getString(R.string.delete));
                        imageView.setImageDrawable(drawableDelete);
                        imageView.setOnClickListener(v->deleteRow(tableRow, couple.getID()));
                    }
                }
                tableRow.addView(imageView);
            }
        }

        if(actionMode.equals(ACTION_MODE.NORMAL)){
            addSaveButton.setText(R.string.add);
            addSaveButton.setOnClickListener(v->addEditRow(null, null));
            addSaveButton.setEnabled(true);
            addSaveButton.setVisibility(View.VISIBLE);
        }else{
            addSaveButton.setVisibility(View.GONE);
        }
    }

    private void deleteRow(TableRow tableRow, int coupleID){
        if(tableRow != null){
            dictionary.removeIf(couple->couple.getID() == coupleID);
            displayTable();
        }
    }

    private void editRow(TableRow tableRow, int coupleID, boolean stayInActivity){
        if(tableRow != null){
            setEditing(true);
            rowIndexEditing = coupleID + 1;
            EditText editTextSingular = new EditText(this, null, R.attr.editTextStyle, R.style.DictionaryActivity_TableLayout_EditText);
            EditText editTextPlural = new EditText(this, null, R.attr.editTextStyle, R.style.DictionaryActivity_TableLayout_EditText);
            editTextSingular.addTextChangedListener(getTextWatcher(editTextSingular, editTextPlural));
            editTextPlural.addTextChangedListener(getTextWatcher(editTextSingular, editTextPlural));
            editTextSingular.setText(((TextView)tableRow.getChildAt(0)).getText().toString());
            editTextPlural.setText(((TextView)tableRow.getChildAt(1)).getText().toString());

            tableRow.removeViews(0, 2);
            tableRow.addView(editTextSingular, 0);
            tableRow.addView(editTextPlural, 1);

            addSaveButton.setText(R.string.save);
            addSaveButton.setEnabled(false);
            addSaveButton.setOnClickListener(v->applyRowChange(tableRow, coupleID, stayInActivity));
            addSaveButton.setVisibility(View.VISIBLE);
        }
    }

    private void editRow(TableRow tableRow, int coupleID){
        if(tableRow != null){
            editRow(tableRow, coupleID, true);
        }
    }

    private void applyRowChange(TableRow tableRow, int coupleID, boolean stayInActivity){
        if(tableRow != null){
            setEditing(false);
            String singular = ((EditText)tableRow.getChildAt(0)).getText().toString();
            String plural = ((EditText)tableRow.getChildAt(1)).getText().toString();
            dictionaryManager.get(dictionary, coupleID).set(singular, plural);
            if(stayInActivity){
                displayTable();
            }else{
                dictionaryManager.saveList(dictionary);
                finishActivityWithResult(getIntent().getIntExtra(DictionaryManager.INTENT_EXTRA_DATA_EDIT_TEXT_ID, -1), dictionary.get(coupleID).getSingular());
            }
            addSaveButton.setVisibility(View.GONE);

        }
    }

    private void addEditRow(String singular, String plural, boolean stayInActivity){
        setEditing(true);

        rowIndexEditing = tableLayout.getChildCount();
        EditText editTextSingular = new EditText(this, null, R.attr.editTextStyle, R.style.DictionaryActivity_TableLayout_EditText);
        EditText editTextPlural = new EditText(this, null, R.attr.editTextStyle, R.style.DictionaryActivity_TableLayout_EditText);
        editTextSingular.addTextChangedListener(getTextWatcher(editTextSingular, editTextPlural));
        editTextPlural.addTextChangedListener(getTextWatcher(editTextSingular, editTextPlural));

        addSaveButton.setText(R.string.save);
        addSaveButton.setEnabled(false);
        addSaveButton.setOnClickListener(v->saveNewRow(editTextSingular.getText().toString(), editTextPlural.getText().toString(), stayInActivity));

        editTextSingular.setText(singular);
        editTextPlural.setText(plural);

        TableRow tableRow = new TableRow(this);

        tableRow.addView(editTextSingular);
        tableRow.addView(editTextPlural);
        tableLayout.addView(tableRow);
    }

    private void addEditRow(String singular, String plural){
        addEditRow(singular, plural, true);
    }

    private void saveNewRow(String singular, String plural, boolean stayInActivity){
        setEditing(false);
        dictionaryManager.addInList(dictionary, singular, plural);
        dictionaryManager.saveList(dictionary);
        if(stayInActivity){
            displayTable();
        }else{
            finishActivityWithResult(getIntent().getIntExtra(DictionaryManager.INTENT_EXTRA_DATA_EDIT_TEXT_ID, -1), singular);
        }
    }

    protected void finishActivityWithResult(int editTextId, String unit){
        Intent intent = new Intent();
        intent.putExtra(DictionaryManager.INTENT_EXTRA_DATA_UNIT, unit);
        intent.putExtra(DictionaryManager.INTENT_EXTRA_DATA_EDIT_TEXT_ID, editTextId);
        setResult(RESULT_OK, intent);
        onSupportNavigateUp();
    }

    private @NotNull TextWatcher getTextWatcher(EditText editText1, EditText editText2){
        return new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s){
                if(editText1 != null && editText2 != null){
                    addSaveButton.setEnabled(editText1.getText().length() > 0 && editText2.getText().length() > 0);
                }
            }
        };
    }

    private void setEditing(boolean editing){
        this.editing = editing;
        hasMenu = !editing;
        cancelButton.setVisibility(editing ? View.VISIBLE : View.GONE);
        invalidateOptionsMenu();
    }

    private void cancelEditing(){
        cancelButton.setVisibility(View.GONE);
        setEditing(false);
        refresh();
    }
}