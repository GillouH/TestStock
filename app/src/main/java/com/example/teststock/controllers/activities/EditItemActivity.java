package com.example.teststock.controllers.activities;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.teststock.R;
import com.example.teststock.controllers.OneItemManagerActivity;
import com.example.teststock.models.PersonalLog;
import com.example.teststock.models.items.BasicItem;
import com.example.teststock.models.items.Item;
import com.example.teststock.models.items.PackItem;
import com.example.teststock.models.managers.DictionaryManager;
import com.example.teststock.models.managers.ItemManager;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class EditItemActivity extends OneItemManagerActivity{
    private static final String BUNDLE_KEY_ITEM_PICTURE = "BUNDLE_KEY_ITEM_PICTURE";

    private EditText
            editTextItemName,
            editTextBasicItemQuantity,
            editTextBasicItemSeuil,
            editTextPackItemQuantityMaxInPack,
            editTextPackItemQuantityOut,
            editTextPackItemNbPack,
            editTextPackItemSeuil;
    private final int REQUEST_CODE_DICTIONARY = 1;
    private final int REQUEST_CODE_PICTURE = 2;
    private ImageView imageViewItemPicture;
    private AutoCompleteTextView autoCompleteTextViewBasicItemUnit, autoCompleteTextViewPackItemUnitPack, autoCompleteTextViewPackItemUnit;
    private RadioButton radioButtonBasicItem, radioButtonPackItem;
    private LinearLayout linearLayoutBasicItem, linearLayoutPackItem;
    private TextView textViewPackItemExample;
    private Drawable drawableEdit, drawableAdd;
    private Drawable imageDrawableDefaultItemPicture;
    private String[] unitList;
    private String imageUriStr;
    private Button buttonChoosetemPicture, buttonDeleteItemPicture, buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = findViewById(R.id.activityEditItem_toolbar);
        editTextItemName = findViewById(R.id.activityEditItem_editText_itemName);
        imageViewItemPicture = findViewById(R.id.activityEditItem_imageView_itemPicture);
        buttonChoosetemPicture = findViewById(R.id.activityEditItem_button_chooseItemPicture);
        buttonDeleteItemPicture = findViewById(R.id.activityEditItem_button_deleteItemPicture);
        radioButtonBasicItem = findViewById(R.id.activityEditItem_radioButton_basicItem);
        radioButtonPackItem = findViewById(R.id.activityEditItem_radioButton_packItem);
        linearLayoutBasicItem = findViewById(R.id.activityEditItem_linearLayout_basicItem);
        autoCompleteTextViewBasicItemUnit = findViewById(R.id.activityEditItem_autoCompleteTextView_basicItem_unit);
        ImageButton imageButtonBasicItemUnit = findViewById(R.id.activityEditItem_imageButton_basicItem_unit);
        editTextBasicItemQuantity = findViewById(R.id.activityEditItem_editText_basicItem_quantity);
        TextView textViewBasicItemUnitForQuantity = findViewById(R.id.activityEditItem_textView_basicItem_unit_for_quantity);
        editTextBasicItemSeuil = findViewById(R.id.activityEditItem_editText_basicItem_seuil);
        TextView textViewBasicItemUnitForSeuil = findViewById(R.id.activityEditItem_textView_basicItem_unit_for_seuil);
        linearLayoutPackItem = findViewById(R.id.activityEditItem_linearLayout_packItem);
        autoCompleteTextViewPackItemUnitPack = findViewById(R.id.activityEditItem_autoCompleteTextView_packItem_unitPack);
        ImageButton imageButtonPackItemUnitPack = findViewById(R.id.activityEditItem_imageButton_packItem_unitPack);
        autoCompleteTextViewPackItemUnit = findViewById(R.id.activityEditItem_autoCompleteTextView_packItem_unit);
        ImageButton imageButtonPackItemUnit = findViewById(R.id.activityEditItem_imageButton_packItem_unit);
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

        imageDrawableDefaultItemPicture = ContextCompat.getDrawable(this, Item.drawableIDDefaultItemPicture);
        drawableEdit = ContextCompat.getDrawable(this, R.drawable.ic_baseline_edit_24);
        drawableAdd = ContextCompat.getDrawable(this, R.drawable.ic_baseline_add_24);

        setSupportActionBar(toolbar);

        unitList = dictionaryManager.getUnitArray();

        editTextItemName.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s){
                enableSaveButton();
            }
        });

        buttonChoosetemPicture.setOnClickListener(v->chooseItemPicture());
        buttonDeleteItemPicture.setOnClickListener(v->deleteItemPicture());

        linkElements(
                autoCompleteTextViewBasicItemUnit,
                new NodeEditTextAndTextView[]{
                        new NodeEditTextAndTextView(editTextBasicItemQuantity, textViewBasicItemUnitForQuantity),
                        new NodeEditTextAndTextView(editTextBasicItemSeuil, textViewBasicItemUnitForSeuil)
                },
                imageButtonBasicItemUnit,
                false
        );
        linkElements(
                autoCompleteTextViewPackItemUnitPack,
                new NodeEditTextAndTextView[]{
                        new NodeEditTextAndTextView(editTextPackItemNbPack, textViewPackItemUnitForNbPack)
                },
                imageButtonPackItemUnitPack,
                true
        );
        linkElements(
                autoCompleteTextViewPackItemUnit,
                new NodeEditTextAndTextView[]{
                        new NodeEditTextAndTextView(editTextPackItemQuantityOut, textViewPackItemUnitForQuantityOut),
                        new NodeEditTextAndTextView(editTextPackItemQuantityMaxInPack, textViewPackItemUnitForQuantityMaxInPack),
                        new NodeEditTextAndTextView(editTextPackItemSeuil, textViewPackItemUnitForSeuil)
                },
                imageButtonPackItemUnit,
                true
        );

        if(savedInstanceState == null){
            setPreviewText();
            itemID = getIntent().getIntExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, -1);
            if(itemID != -1){
                Item item = itemManager.get(itemID);
                if(item != null){
                    editTextItemName.setText(item.getName());
                    setNewPicture(item.getImage());
                    if(item.getClass().equals(BasicItem.class)){
                        fillBasicItemForm((BasicItem)item);
                    }else if(item.getClass().equals(PackItem.class)){
                        fillPackItemForm((PackItem)item);
                    }
                }
            }else{
                linearLayoutBasicItem.setVisibility(View.GONE);
                linearLayoutPackItem.setVisibility(View.GONE);
                editTextBasicItemQuantity.setText(String.format(Locale.getDefault(), "%d", 0));
                editTextPackItemQuantityOut.setText(String.format(Locale.getDefault(), "%d", 0));
                editTextPackItemNbPack.setText(String.format(Locale.getDefault(), "%d", 0));
                setNewPicture(null);
            }
        }

        buttonSave.setOnClickListener(v->saveItem());

        enableSaveButton();
    }

    private void setNewPicture(String imageUriStr){
        if(imageUriStr == null || imageUriStr.equals(Item.JSON_VALUE_IMAGE_NULL)){
            this.imageUriStr = Item.JSON_VALUE_IMAGE_NULL;
            imageViewItemPicture.setImageDrawable(imageDrawableDefaultItemPicture);
        }else{
            this.imageUriStr = imageUriStr;
            imageViewItemPicture.setImageURI(Uri.parse(imageUriStr));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull @NotNull Bundle outState){
        outState.putString(BUNDLE_KEY_ITEM_PICTURE, imageUriStr);
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
        setNewPicture(savedInstanceState.getString(BUNDLE_KEY_ITEM_PICTURE));
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

    private void chooseItemPicture(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_CODE_PICTURE);
    }

    private void deleteItemPicture(){
        setNewPicture(null);
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
        isBasicItemFormFill = isBasicItemFormFill && dictionaryManager.getID(autoCompleteTextViewBasicItemUnit.getText().toString()) != -1;
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
        isPackItemFormFill = isPackItemFormFill && dictionaryManager.getID(autoCompleteTextViewPackItemUnit.getText().toString()) != -1;
        isPackItemFormFill = isPackItemFormFill && dictionaryManager.getID(autoCompleteTextViewPackItemUnitPack.getText().toString()) != -1;
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
        int nbPack = 5;

        String unitPack = autoCompleteTextViewPackItemUnitPack.getText().toString();
        if(unitPack.length() == 0 || dictionaryManager.getID(unitPack) == -1){
            unitPack = String.format("[%s]", autoCompleteTextViewPackItemUnitPack.getHint());
        }else{
            unitPack = dictionaryManager.get(unitPack, DictionaryManager.getNombre(nbPack));
        }

        String quantityMaxInPack = editTextPackItemQuantityMaxInPack.getText().toString();
        if(quantityMaxInPack.length() == 0){
            quantityMaxInPack = String.format("[%s]", editTextPackItemQuantityMaxInPack.getHint());
        }
        int value;
        try{
            value = Integer.parseInt(quantityMaxInPack);
        }catch(NumberFormatException e){
            value = 0;
        }

        String unitInPack = autoCompleteTextViewPackItemUnit.getText().toString();
        if(unitInPack.length() == 0 || dictionaryManager.getID(unitInPack) == -1){
            unitInPack = String.format("[%s]", autoCompleteTextViewPackItemUnit.getHint());
        }else{
            unitInPack = dictionaryManager.get(unitInPack, DictionaryManager.getNombre(value));
        }

        textViewPackItemExample.setText(String.format(
                Locale.getDefault(),
                "%d %s de %s %s",
                nbPack,
                unitPack,
                quantityMaxInPack,
                unitInPack
        ));
    }

    private void linkElements(AutoCompleteTextView unitInput, NodeEditTextAndTextView[] valueUnitInputs, ImageButton imageButton, boolean packField){
        if(unitInput != null){
            unitInput.addTextChangedListener(new TextWatcher(){
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after){

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count){

                }

                @Override
                public void afterTextChanged(Editable s){
                    int indexInDictionary = dictionaryManager.getID(s.toString());
                    if(imageButton != null){
                        imageButton.setImageDrawable(indexInDictionary == -1 ? drawableAdd : drawableEdit);
                        imageButton.setContentDescription(indexInDictionary == -1 ? getString(R.string.addInDictionary) : getString(R.string.editInDictionary));
                        imageButton.setVisibility(s.length() != 0 ? View.VISIBLE : View.GONE);
                    }
                    if(valueUnitInputs != null){
                        for(NodeEditTextAndTextView valueUnitInput : valueUnitInputs){
                            if(indexInDictionary == -1){
                                valueUnitInput.getTextView().setText(unitInput.getHint());
                            }else{
                                int value;
                                try{
                                    value = Integer.parseInt(valueUnitInput.getEditText().getText().toString());
                                }catch(NumberFormatException e){
                                    value = 0;
                                }
                                valueUnitInput.getTextView().setText(dictionaryManager.get(indexInDictionary, DictionaryManager.getNombre(value)));
                            }
                        }
                    }
                    if(packField){
                        setPreviewText();
                    }
                    enableSaveButton();
                }
            });
            ArrayAdapter<Object> arrayAdapter = new ArrayAdapter<>(EditItemActivity.this, android.R.layout.simple_list_item_1, unitList);
            unitInput.setAdapter(arrayAdapter);
            unitInput.setThreshold(1);
            if(imageButton != null){
                imageButton.setOnClickListener(v->{
                    Intent intent = new Intent(v.getContext(), DictionaryActivity.class);
                    String unit = unitInput.getText().toString();
                    int unitIDInDictionary = dictionaryManager.getID(unit);
                    boolean newUnit = unitIDInDictionary == -1;
                    intent.putExtra(DictionaryManager.INTENT_EXTRA_DATA_NEW_UNIT, newUnit);
                    if(newUnit){
                        intent.putExtra(DictionaryManager.INTENT_EXTRA_DATA_UNIT, unit);
                    }else{
                        intent.putExtra(DictionaryManager.INTENT_EXTRA_DATA_UNIT_ID, unitIDInDictionary);
                    }
                    intent.putExtra(DictionaryManager.INTENT_EXTRA_DATA_EDIT_TEXT_ID, unitInput.getId());
                    startActivityForResult(intent, REQUEST_CODE_DICTIONARY);
                });
            }
            if(valueUnitInputs != null){
                for(NodeEditTextAndTextView valueUnitInput : valueUnitInputs){
                    valueUnitInput.getEditText().addTextChangedListener(new TextWatcher(){
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after){

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count){

                        }

                        @Override
                        public void afterTextChanged(Editable s){
                            int indexInDictionary = dictionaryManager.getID(unitInput.getText().toString());
                            if(indexInDictionary == -1){
                                valueUnitInput.getTextView().setText(unitInput.getHint());
                            }else{
                                int value;
                                try{
                                    value = Integer.parseInt(s.toString());
                                }catch(NumberFormatException e){
                                    value = 0;
                                }
                                valueUnitInput.getTextView().setText(dictionaryManager.get(indexInDictionary, DictionaryManager.getNombre(value)));
                            }
                            enableSaveButton();
                        }
                    });
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && data != null){
            if(requestCode == REQUEST_CODE_DICTIONARY){
                String unit = data.getStringExtra(DictionaryManager.INTENT_EXTRA_DATA_UNIT);
                int editTextId = data.getIntExtra(DictionaryManager.INTENT_EXTRA_DATA_EDIT_TEXT_ID, -1);
                if(editTextId != -1){
                    ((EditText)findViewById(editTextId)).setText(unit);
                }
                unitList = dictionaryManager.getUnitArray();
            }else if(requestCode == REQUEST_CODE_PICTURE){
                setNewPicture(data.getData().toString());
            }
        }
    }

    private void fillBasicItemForm(BasicItem item){
        if(item != null){
            autoCompleteTextViewBasicItemUnit.setText(item.getUnitSingular());
            editTextBasicItemQuantity.setText(String.format(Locale.getDefault(), "%d", item.getQuantity()));
            editTextBasicItemSeuil.setText(String.format(Locale.getDefault(), "%d", item.getSeuil()));
            onRadioButtonClicked(radioButtonBasicItem);
        }
    }

    private void fillPackItemForm(PackItem item){
        if(item != null){
            autoCompleteTextViewPackItemUnit.setText(item.getUnitInPackSingular());
            editTextPackItemQuantityMaxInPack.setText(String.format(Locale.getDefault(), "%d", item.getQuantityMaxInPack()));
            autoCompleteTextViewPackItemUnitPack.setText(item.getPackUnitSingular());
            editTextPackItemQuantityOut.setText(String.format(Locale.getDefault(), "%d", item.getQuantityOut()));
            editTextPackItemNbPack.setText(String.format(Locale.getDefault(), "%d", item.getNbPackFull()));
            editTextPackItemSeuil.setText(String.format(Locale.getDefault(), "%d", item.getSeuil()));
            onRadioButtonClicked(radioButtonPackItem);
        }
    }

    private void saveItem(){
        personalLog.write(PersonalLog.TYPE.CLICK, getClass(), "clickSaveButton");
        Item item = createItemFromForm();
        if(itemID == -1){
            itemManager.addInList(item);
            Intent intent = new Intent(this, ItemDetailActivity.class);
            intent.putExtra(ItemManager.INTENT_EXTRA_DATA_KEY_ID, item.getID());
            startActivity(intent);
        }else{
            itemManager.replaceInList(item);
        }
        finish();
    }

    private Item createItemFromForm(){
        Item item = null;
        if(radioButtonBasicItem.isChecked()){
            item = itemManager.createBasicItem(
                    itemID,
                    editTextItemName.getText().toString(),
                    Integer.parseInt(editTextBasicItemQuantity.getText().toString()),
                    dictionaryManager.getID(autoCompleteTextViewBasicItemUnit.getText().toString()),
                    Integer.parseInt(editTextBasicItemSeuil.getText().toString()),
                    imageUriStr
            );
        }else if(radioButtonPackItem.isChecked()){
            item = itemManager.createPackItem(
                    itemID,
                    editTextItemName.getText().toString(),
                    Integer.parseInt(editTextPackItemQuantityOut.getText().toString()),
                    dictionaryManager.getID(autoCompleteTextViewPackItemUnit.getText().toString()),
                    Integer.parseInt(editTextPackItemNbPack.getText().toString()),
                    dictionaryManager.getID(autoCompleteTextViewPackItemUnitPack.getText().toString()),
                    Integer.parseInt(editTextPackItemQuantityMaxInPack.getText().toString()),
                    Integer.parseInt(editTextPackItemSeuil.getText().toString()),
                    imageUriStr
            );
        }
        return item;
    }

    private static class NodeEditTextAndTextView{
        private final EditText editText;
        private final TextView textView;

        public NodeEditTextAndTextView(@NotNull EditText editText, @NotNull TextView textView){
            this.editText = editText;
            this.textView = textView;
        }

        public EditText getEditText(){
            return editText;
        }

        public TextView getTextView(){
            return textView;
        }
    }
}