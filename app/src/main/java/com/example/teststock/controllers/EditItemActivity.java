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
import com.example.teststock.models.ItemManager;
import com.example.teststock.models.PackItem;
import com.example.teststock.models.PersonalLog;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class EditItemActivity extends OneItemManagerActivity{
    private EditText
            editTextItemName,
            editTextBasicItemUnit,
            editTextBasicItemQuantity,
            editTextBasicItemSeuil,
            editTextPackItemUnitPack,
            editTextPackItemUnit,
            editTextPackItemQuantityMaxInPack,
            editTextPackItemQuantityOut,
            editTextPackItemNbPack,
            editTextPackItemSeuil;
    private RadioButton radioButtonBasicItem, radioButtonPackItem;
    private LinearLayout linearLayoutBasicItem, linearLayoutPackItem;
    private TextView textViewPackItemExample;
    private Button buttonSave;

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
        editTextBasicItemQuantity = findViewById(R.id.activityEditItem_editText_basicItem_quantity);
        TextView textViewBasicItemUnitForQuantity = findViewById(R.id.activityEditItem_textView_basicItem_unit_for_quantity);
        editTextBasicItemSeuil = findViewById(R.id.activityEditItem_editText_basicItem_seuil);
        TextView textViewBasicItemUnitForSeuil = findViewById(R.id.activityEditItem_textView_basicItem_unit_for_seuil);
        linearLayoutPackItem = findViewById(R.id.activityEditItem_linearLayout_packItem);
        editTextPackItemUnitPack = findViewById(R.id.activityEditItem_editText_packItem_unitPack);
        editTextPackItemUnit = findViewById(R.id.activityEditItem_editText_packItem_unit);
        editTextPackItemQuantityMaxInPack = findViewById(R.id.activityEditItem_editText_packItem_quantityMaxInPack);
        TextView textViewPackItemUnitForQuantityMaxInPack = findViewById(R.id.activityEditItem_textView_packItem_unit_for_quantityMaxInPack);
        editTextPackItemQuantityOut = findViewById(R.id.activityEditItem_editText_packItem_quantityOut);
        TextView textViewPackItemUnitForQuantityOut = findViewById(R.id.activityEditItem_textView_packItem_unit_for_quantityOut);
        editTextPackItemNbPack = findViewById(R.id.activityEditItem_editText_packItem_nbPack);
        TextView textViewPackItemUnitForNbPack = findViewById(R.id.activityEditItem_textView_packItem_unit_for_nbPack);
        editTextPackItemSeuil = findViewById(R.id.activityEditItem_editText_packItem_seuil);
        TextView textViewPackItemUnitForSeuil = findViewById(R.id.activityEditItem_textView_packItem_unit_for_seuil);
        textViewPackItemExample = findViewById(R.id.activityEditItem_textView_packItem_example);
        buttonSave = findViewById(R.id.activityEditItem_button_save);

        setSupportActionBar(toolbar);

        createTextListener(editTextItemName, null, false);
        createTextListener(editTextBasicItemUnit, new TextView[]{textViewBasicItemUnitForQuantity, textViewBasicItemUnitForSeuil}, false);
        createTextListener(editTextBasicItemSeuil, null, false);
        createTextListener(editTextPackItemUnit, new TextView[]{textViewPackItemUnitForQuantityMaxInPack, textViewPackItemUnitForQuantityOut}, true);
        createTextListener(editTextPackItemQuantityMaxInPack, null, true);
        createTextListener(editTextPackItemUnitPack, new TextView[]{textViewPackItemUnitForNbPack, textViewPackItemUnitForSeuil}, true);
        createTextListener(editTextPackItemSeuil, null, false);

        if(savedInstanceState == null){
            setPreviewText();
            itemID = getIntent().getIntExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, -1);
            if(itemID != -1){
                Item item = itemManager.getItem(itemID);
                editTextItemName.setText(item.getName());
                if(item.getClass().equals(BasicItem.class)){
                    fillBasicItemForm((BasicItem)item);
                }else if(item.getClass().equals(PackItem.class)){
                    fillPackItemForm((PackItem)item);
                }
            }else{
                linearLayoutBasicItem.setVisibility(View.GONE);
                linearLayoutPackItem.setVisibility(View.GONE);
                editTextBasicItemQuantity.setText(String.format(Locale.getDefault(), "%d", 0));
                editTextPackItemQuantityOut.setText(String.format(Locale.getDefault(), "%d", 0));
                editTextPackItemNbPack.setText(String.format(Locale.getDefault(), "%d", 0));
            }
        }

        buttonSave.setOnClickListener(v->{
            personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickSaveButton");
            Item item = createItemFromForm();
            if(itemID == -1){
                itemManager.addItemInItemList(item);
                Intent intent = new Intent(this, ItemDetailActivity.class);
                intent.putExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, item.getID());
                startActivity(intent);
            }else{
                itemManager.modifyItemInItemList(item);
            }
            finish();
        });

        enableSaveButton();
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState){
        String itemType = BUNDLE_VALUE_ITEM_TYPE_UNDEFINED;
        if(radioButtonBasicItem.isChecked()){
            itemType = BUNDLE_VALUE_ITEM_TYPE_BASIC;
        }else if(radioButtonPackItem.isChecked()){
            itemType = BUNDLE_VALUE_ITEM_TYPE_PACKED;
        }
        outState.putString(BUNDLE_KEY_ITEM_TYPE, itemType);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        String itemType = savedInstanceState.getString(BUNDLE_KEY_ITEM_TYPE);
        if(itemType != null){
            if(itemType.equals(BUNDLE_VALUE_ITEM_TYPE_BASIC)){
                onRadioButtonClicked(radioButtonBasicItem);
            }else if(itemType.equals(BUNDLE_VALUE_ITEM_TYPE_PACKED)){
                onRadioButtonClicked(radioButtonPackItem);
            }
        }else{
            linearLayoutBasicItem.setVisibility(View.GONE);
            linearLayoutPackItem.setVisibility(View.GONE);
        }
    }

    public void onRadioButtonClicked(View view){
        if(view != null){
            radioButtonBasicItem.setChecked(view.getId() == R.id.activityEditItem_radioButton_basicItem);
            radioButtonPackItem.setChecked(view.getId() == R.id.activityEditItem_radioButton_packItem);

            linearLayoutBasicItem.setVisibility(view.getId() == R.id.activityEditItem_radioButton_basicItem ? View.VISIBLE : View.GONE);
            linearLayoutPackItem.setVisibility(view.getId() == R.id.activityEditItem_radioButton_packItem ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isBasicItemFormFill(){
        boolean isBasicItemFormFill = radioButtonBasicItem.isChecked();
        isBasicItemFormFill = isBasicItemFormFill && editTextBasicItemUnit.getText().length() > 0;
        int seuil, quantity;
        try{
            quantity = Integer.parseInt(editTextBasicItemQuantity.getText().toString());
            seuil = Integer.parseInt(editTextBasicItemSeuil.getText().toString());
        }catch(NumberFormatException e){
            return false;
        }
        return isBasicItemFormFill && seuil > 0 && quantity >= 0;
    }

    private boolean isPackItemFormFill(){
        boolean isPackItemFormFill = radioButtonPackItem.isChecked();
        isPackItemFormFill = isPackItemFormFill && editTextPackItemUnit.getText().length() > 0;
        isPackItemFormFill = isPackItemFormFill && editTextPackItemUnitPack.getText().length() > 0;
        int quantityMaxInPack, quantityOut, nbPack, seuil;
        try{
            quantityMaxInPack = Integer.parseInt(editTextPackItemQuantityMaxInPack.getText().toString());
            quantityOut = Integer.parseInt(editTextPackItemQuantityOut.getText().toString());
            nbPack = Integer.parseInt(editTextPackItemNbPack.getText().toString());
            seuil = Integer.parseInt(editTextPackItemSeuil.getText().toString());
        }catch(NumberFormatException e){
            return false;
        }
        return isPackItemFormFill && quantityMaxInPack > 0 && seuil > 0 && quantityOut >= 0 && nbPack >= 0;
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

    private void createTextListener(EditText editText, TextView[] textViewList, boolean packField){
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
                    if(textViewList != null){
                        for(TextView textView : textViewList){
                            textView.setText(s);
                        }
                    }
                    if(packField){
                        setPreviewText();
                    }
                    enableSaveButton();
                }
            });
        }
    }

    private void fillBasicItemForm(BasicItem item){
        if(item != null){
            editTextBasicItemUnit.setText(item.getUnit());
            editTextBasicItemQuantity.setText(String.format(Locale.getDefault(), "%d", item.getQuantity()));
            editTextBasicItemSeuil.setText(String.format(Locale.getDefault(), "%d", item.getSeuil()));
            onRadioButtonClicked(radioButtonBasicItem);
        }
    }

    private void fillPackItemForm(PackItem item){
        if(item != null){
            editTextPackItemUnit.setText(item.getUnitInPack());
            editTextPackItemQuantityMaxInPack.setText(String.format(Locale.getDefault(), "%d", item.getQuantityMaxInPack()));
            editTextPackItemUnitPack.setText(item.getPackUnit());
            editTextPackItemQuantityOut.setText(String.format(Locale.getDefault(), "%d", item.getQuantityOut()));
            editTextPackItemNbPack.setText(String.format(Locale.getDefault(), "%d", item.getNbPackFull()));
            editTextPackItemSeuil.setText(String.format(Locale.getDefault(), "%d", item.getSeuil()));
            onRadioButtonClicked(radioButtonPackItem);
        }
    }

    private Item createItemFromForm(){
        Item item = null;
        if(radioButtonBasicItem.isChecked()){
            item = new BasicItem(
                    itemID,
                    editTextItemName.getText().toString(),
                    Integer.parseInt(editTextBasicItemQuantity.getText().toString()),
                    editTextBasicItemUnit.getText().toString(),
                    Integer.parseInt(editTextBasicItemSeuil.getText().toString())
            );
        }else if(radioButtonPackItem.isChecked()){
            item = new PackItem(
                    itemID,
                    editTextItemName.getText().toString(),
                    Integer.parseInt(editTextPackItemQuantityOut.getText().toString()),
                    editTextPackItemUnit.getText().toString(),
                    Integer.parseInt(editTextPackItemNbPack.getText().toString()),
                    editTextPackItemUnitPack.getText().toString(),
                    Integer.parseInt(editTextPackItemQuantityMaxInPack.getText().toString()),
                    Integer.parseInt(editTextPackItemSeuil.getText().toString())
            );
        }
        return item;
    }
}