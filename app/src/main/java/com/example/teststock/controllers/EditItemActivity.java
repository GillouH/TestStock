package com.example.teststock.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.example.teststock.R;
import com.example.teststock.models.BasicItem;
import com.example.teststock.models.Item;
import com.example.teststock.models.PackItem;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;
import java.util.Objects;

public class EditItemActivity extends ItemManagerActivity{
    private static final String BUNDLE_STATE_ITEM_ID = "BUNDLE_STATE_ITEM_ID";
    private static final String BUNDLE_STATE_ITEM_NAME = "BUNDLE_STATE_ITEM_NAME";
    private static final String BUNDLE_STATE_ITEM_TYPE = "BUNDLE_STATE_ITEM_TYPE";
    private static final String BUNDLE_STATE_ITEM_TYPE_UNDEFINED = "BUNDLE_STATE_ITEM_TYPE_UNDEFINED";
    private static final String BUNDLE_STATE_ITEM_TYPE_BASIC = "BUNDLE_STATE_ITEM_TYPE_BASIC";
    private static final String BUNDLE_STATE_ITEM_TYPE_PACKED = "BUNDLE_STATE_ITEM_TYPE_PACKED";
    private static final String BUNDLE_STATE_BASIC_ITEM_UNIT = "BUNDLE_STATE_BASIC_ITEM_UNIT";
    private static final String BUNDLE_STATE_BASIC_ITEM_SEUIL = "BUNDLE_STATE_BASIC_ITEM_SEUIL";
    private static final String BUNDLE_STATE_PACK_ITEM_UNIT_PACK = "BUNDLE_STATE_PACK_ITEM_UNIT_PACK";
    private static final String BUNDLE_STATE_PACK_ITEM_UNIT = "BUNDLE_STATE_PACK_ITEM_UNIT";
    private static final String BUNDLE_STATE_PACK_ITEM_QUANTITY_MAX_IN_PACK = "BUNDLE_STATE_PACK_ITEM_QUANTITY_MAX_IN_PACK";
    private static final String BUNDLE_STATE_PACK_ITEM_SEUIL = "BUNDLE_STATE_PACK_ITEM_SEUIL";
    private EditText
            editTextItemName,
            editTextBasicItemUnit,
            editTextBasicItemSeuil,
            editTextPackItemUnitPack,
            editTextPackItemUnit,
            editTextPackItemQuantityMaxInPack,
            editTextPackItemSeuil;
    private RadioButton radioButtonBasicItem, radioButtonPackItem;
    private LinearLayout linearLayoutBasicItem, packItemLinearLayout;
    private TextView textViewPackItemExample;
    private Button buttonSave;
    private int itemID;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = findViewById(R.id.activityEditItem_toolbar);
        editTextItemName = findViewById(R.id.activityEditItem_editText_itemName);
        radioButtonBasicItem = findViewById(R.id.activityEditItem_radioButton_basicItem);
        radioButtonPackItem = findViewById(R.id.activityEditItem_radioButton_packItem);
        linearLayoutBasicItem = findViewById(R.id.activityEditItem_linearLayout_basicItem);
        editTextBasicItemUnit = findViewById(R.id.activityEditItem_editText_basicItem_unit);
        editTextBasicItemSeuil = findViewById(R.id.activityEditItem_editText_basicItem_seuil);
        TextView textViewBasicItemUnitForSeuil = findViewById(R.id.activityEditItem_textView_basicItem_unit_for_seuil);
        packItemLinearLayout = findViewById(R.id.activityEditItem_linearLayout_packItem);
        editTextPackItemUnitPack = findViewById(R.id.activityEditItem_editText_packItem_unitPack);
        editTextPackItemUnit = findViewById(R.id.activityEditItem_editText_packItem_unit);
        editTextPackItemQuantityMaxInPack = findViewById(R.id.activityEditItem_editText_packItem_quantityMaxInPack);
        TextView textViewPackItemUnitForQuantityMaxInPack = findViewById(R.id.activityEditItem_textView_packItem_unit_for_quantityMaxInPack);
        editTextPackItemSeuil = findViewById(R.id.activityEditItem_editText_packItem_seuil);
        TextView textViewPackItemUnitForSeuil = findViewById(R.id.activityEditItem_textView_packItem_unit_for_seuil);
        textViewPackItemExample = findViewById(R.id.activityEditItem_textView_packItem_example);
        buttonSave = findViewById(R.id.activityEditItem_button_save);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        linearLayoutBasicItem.setVisibility(View.GONE);
        packItemLinearLayout.setVisibility(View.GONE);

        createTextListener(editTextItemName, null, false);
        createTextListener(editTextBasicItemUnit, textViewBasicItemUnitForSeuil, false);
        createTextListener(editTextBasicItemSeuil, null, false);
        createTextListener(editTextPackItemUnit, textViewPackItemUnitForQuantityMaxInPack, true);
        createTextListener(editTextPackItemQuantityMaxInPack, null, true);
        createTextListener(editTextPackItemUnitPack, textViewPackItemUnitForSeuil, true);
        createTextListener(editTextPackItemSeuil, null, false);
        setPreviewText();

        if(savedInstanceState != null){
            itemID = savedInstanceState.getInt(BUNDLE_STATE_ITEM_ID);
            editTextItemName.setText(savedInstanceState.getString(BUNDLE_STATE_ITEM_NAME));
            String itemType = savedInstanceState.getString(BUNDLE_STATE_ITEM_TYPE);
            editTextBasicItemUnit.setText(savedInstanceState.getString(BUNDLE_STATE_BASIC_ITEM_UNIT));
            editTextBasicItemSeuil.setText(savedInstanceState.getString(BUNDLE_STATE_BASIC_ITEM_SEUIL));
            editTextPackItemUnitPack.setText(savedInstanceState.getString(BUNDLE_STATE_PACK_ITEM_UNIT_PACK));
            editTextPackItemUnit.setText(savedInstanceState.getString(BUNDLE_STATE_PACK_ITEM_UNIT));
            editTextPackItemQuantityMaxInPack.setText(savedInstanceState.getString(BUNDLE_STATE_PACK_ITEM_QUANTITY_MAX_IN_PACK));
            editTextPackItemSeuil.setText(savedInstanceState.getString(BUNDLE_STATE_PACK_ITEM_SEUIL));
            if(itemType.equals(BUNDLE_STATE_ITEM_TYPE_BASIC)){
                onRadioButtonClicked(radioButtonBasicItem);
            }else if(itemType.equals(BUNDLE_STATE_ITEM_TYPE_PACKED)){
                onRadioButtonClicked(radioButtonPackItem);
            }
        }else{
            itemID = getIntent().getIntExtra(ItemManagerActivity.INTENT_EXTRA_DATA_KEY_ID, -1);
            if(itemID != -1){
                Item item = getItem(itemID);
                fillForm(item);
            }
        }

        buttonSave.setOnClickListener(v->{
            log(TYPE.CLICK, "clickSaveButton");
            Item item = createItemFromForm();
            if(item != null){
                if(itemID == -1){
                    addItemInItemList(item);
                }else{
                    modifyItemInItemList(item);
                }
                Intent itemDetailActivity = new Intent(EditItemActivity.this, ItemDetailActivity.class);
                itemDetailActivity.putExtra(ItemManagerActivity.INTENT_EXTRA_DATA_KEY_ID, item.getID());
                startActivity(itemDetailActivity);
                finish();
            }
        });
        enableSaveButton();
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState){
        outState.putInt(BUNDLE_STATE_ITEM_ID, itemID);
        outState.putString(BUNDLE_STATE_ITEM_NAME, editTextItemName.getText().toString());
        String itemType;
        if(radioButtonBasicItem.isChecked()){
            itemType = BUNDLE_STATE_ITEM_TYPE_BASIC;
        }else if(radioButtonPackItem.isChecked()){
            itemType = BUNDLE_STATE_ITEM_TYPE_PACKED;
        }else{
            itemType = BUNDLE_STATE_ITEM_TYPE_UNDEFINED;
        }
        outState.putString(BUNDLE_STATE_ITEM_TYPE, itemType);
        outState.putString(BUNDLE_STATE_BASIC_ITEM_UNIT, editTextBasicItemUnit.getText().toString());
        outState.putString(BUNDLE_STATE_BASIC_ITEM_SEUIL, editTextBasicItemSeuil.getText().toString());
        outState.putString(BUNDLE_STATE_PACK_ITEM_UNIT_PACK, editTextPackItemUnitPack.getText().toString());
        outState.putString(BUNDLE_STATE_PACK_ITEM_UNIT, editTextPackItemUnit.getText().toString());
        outState.putString(BUNDLE_STATE_PACK_ITEM_QUANTITY_MAX_IN_PACK, editTextPackItemQuantityMaxInPack.getText().toString());
        outState.putString(BUNDLE_STATE_PACK_ITEM_SEUIL, editTextPackItemSeuil.getText().toString());
        super.onSaveInstanceState(outState);
    }

    public void onRadioButtonClicked(View view){
        if(view != null){
            radioButtonBasicItem.setChecked(view.getId() == R.id.activityEditItem_radioButton_basicItem);
            radioButtonPackItem.setChecked(view.getId() == R.id.activityEditItem_radioButton_packItem);

            linearLayoutBasicItem.setVisibility(view.getId() == R.id.activityEditItem_radioButton_basicItem ? View.VISIBLE : View.GONE);
            packItemLinearLayout.setVisibility(view.getId() == R.id.activityEditItem_radioButton_packItem ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isBasicItemFormFill(){
        boolean isBasicItemFormFill = radioButtonBasicItem.isChecked();
        isBasicItemFormFill = isBasicItemFormFill && editTextBasicItemUnit.getText().length() > 0;
        try{
            int seuil = Integer.parseInt(editTextBasicItemSeuil.getText().toString());
            isBasicItemFormFill = isBasicItemFormFill && seuil > 0;
        }catch(NumberFormatException e){
            isBasicItemFormFill = false;
        }
        return isBasicItemFormFill;
    }

    private boolean isPackItemFormFill(){
        boolean isPackItemFormFill = radioButtonPackItem.isChecked();
        isPackItemFormFill = isPackItemFormFill && editTextPackItemUnit.getText().length() > 0;
        try{
            int quantityMaxInPack = Integer.parseInt(editTextPackItemQuantityMaxInPack.getText().toString());
            isPackItemFormFill = isPackItemFormFill && quantityMaxInPack > 0;
        }catch(NumberFormatException e){
            isPackItemFormFill = false;
        }
        isPackItemFormFill = isPackItemFormFill && editTextPackItemUnitPack.getText().length() > 0;
        try{
            int seuil = Integer.parseInt(editTextPackItemSeuil.getText().toString());
            isPackItemFormFill = isPackItemFormFill && seuil > 0;
        }catch(NumberFormatException e){
            isPackItemFormFill = false;
        }
        return isPackItemFormFill;
    }

    private void enableSaveButton(){
        boolean isFormFill = editTextItemName.getText().length() > 0;
        isFormFill = isFormFill && (isBasicItemFormFill() || isPackItemFormFill());
        buttonSave.setEnabled(isFormFill);
    }

    private void setPreviewText(){
        String unitPack = editTextPackItemUnitPack.getText().length() > 0 ? editTextPackItemUnitPack.getText().toString() : String.format("[%s]", editTextPackItemUnitPack.getHint());
        String quantityMaxInPack = editTextPackItemQuantityMaxInPack.getText().length() > 0 ? editTextPackItemQuantityMaxInPack.getText().toString() : String.format("[%s]", editTextPackItemQuantityMaxInPack.getHint());
        String unitInPack = editTextPackItemUnit.getText().length() > 0 ? editTextPackItemUnit.getText().toString() : String.format("[%s]", editTextPackItemUnit.getHint());
        textViewPackItemExample.setText(String.format("5 %s de %s %s", unitPack, quantityMaxInPack, unitInPack));
    }

    private void createTextListener(EditText editText, TextView textView, boolean packField){
        if(editText != null){
            editText.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after){
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count){
                }

                @Override
                public void afterTextChanged(Editable s){
                    if(textView != null){
                        textView.setText(s);
                    }
                    if(packField){
                        setPreviewText();
                    }
                    enableSaveButton();
                }
            });
        }
    }

    private void fillForm(Item item){
        if(item != null){
            editTextItemName.setText(item.getName());
            if(item.getClass().equals(BasicItem.class)){
                BasicItem basicItem = (BasicItem)item;
                editTextBasicItemUnit.setText(basicItem.getUnit());
                int seuil = basicItem.getSeuil();
                if(seuil != -1){
                    editTextBasicItemSeuil.setText(String.format(Locale.getDefault(), "%d", basicItem.getSeuil()));
                }
                onRadioButtonClicked(radioButtonBasicItem);
            }else if(item.getClass().equals(PackItem.class)){
                PackItem packItem = (PackItem)item;
                editTextPackItemUnit.setText(packItem.getUnitInPack());
                int quantityMaxInPack = packItem.getQuantityMaxInPack();
                if(quantityMaxInPack != -1){
                    editTextPackItemQuantityMaxInPack.setText(String.format(Locale.getDefault(), "%d", packItem.getQuantityMaxInPack()));
                }
                editTextPackItemUnitPack.setText(packItem.getPackUnit());
                int seuil = packItem.getSeuil();
                if(seuil != -1){
                    editTextPackItemSeuil.setText(String.format(Locale.getDefault(), "%d", packItem.getSeuil()));
                }
                onRadioButtonClicked(radioButtonPackItem);
            }
        }
    }

    private Item createItemFromForm(){
        Item item = null;
        if(radioButtonBasicItem.isChecked()){
            int seuil;
            try{
                seuil = Integer.parseInt(editTextBasicItemSeuil.getText().toString());
            }catch(NumberFormatException e){
                seuil = -1;
            }
            item = new BasicItem(itemID, editTextItemName.getText().toString(), 0, editTextBasicItemUnit.getText().toString(), seuil);
        }else if(radioButtonPackItem.isChecked()){
            int quantityMaxInPack, seuil;
            try{
                quantityMaxInPack = Integer.parseInt(editTextPackItemQuantityMaxInPack.getText().toString());
            }catch(NumberFormatException e){
                quantityMaxInPack = -1;
            }
            try{
                seuil = Integer.parseInt(editTextPackItemSeuil.getText().toString());
            }catch(NumberFormatException e){
                seuil = -1;
            }
            item = new PackItem(itemID, editTextItemName.getText().toString(), 0, editTextPackItemUnit.getText().toString(), 0, editTextPackItemUnitPack.getText().toString(), quantityMaxInPack, seuil);
        }
        return item;
    }
}