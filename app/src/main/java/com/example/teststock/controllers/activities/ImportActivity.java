package com.example.teststock.controllers.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.teststock.R;
import com.example.teststock.controllers.CustomActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class ImportActivity extends CustomActivity{
    JSONObject jsonObjectToImport;
    String dictionaryAsString, itemListAsString;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        Toolbar toolbar = findViewById(R.id.activityImport_toolbar);
        EditText editText = findViewById(R.id.activityImport_editText);
        Button button = findViewById(R.id.activityImport_button);

        setSupportActionBar(toolbar);

        editText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s){
                try{
                    jsonObjectToImport = new JSONObject(s.toString());
                    dictionaryAsString = jsonObjectToImport.getString(dictionaryManager.getPrefKey());
                    itemListAsString = jsonObjectToImport.getString(itemManager.getPrefKey());
                    button.setEnabled(true);
                }catch(JSONException e){
                    button.setEnabled(false);
                }
            }
        });

        button.setOnClickListener(v->{
            dictionaryManager.saveList(dictionaryManager.convertStringToList(dictionaryAsString));
            itemManager.saveList(itemManager.convertStringToList(itemListAsString));
            Toast.makeText(this, "Liste importée", Toast.LENGTH_LONG).show();
        });
    }
}