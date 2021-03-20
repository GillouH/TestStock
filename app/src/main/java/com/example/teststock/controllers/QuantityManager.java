package com.example.teststock.controllers;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.teststock.R;

public class QuantityManager extends RelativeLayout{

    private Button validtionButton;
    private EditText numberEditText;
    private String validationTextAdd, validationTextRemove;
    private String numberEditTextDefaultValue;

    public QuantityManager(Context context){
        super(context);
    }

    public QuantityManager(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public QuantityManager(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet){
        LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater != null ? layoutInflater.inflate(R.layout.quantity_manager, findViewById(R.id.QuantityManager_Root)) : null;
        addView(view);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.QuantityManager);

        if(typedArray.getString(R.styleable.QuantityManager_validationTextAdd) != null)
            validationTextAdd = typedArray.getString(R.styleable.QuantityManager_validationTextAdd);

        if(typedArray.getString(R.styleable.QuantityManager_validationTextRemove) != null)
            validationTextRemove = typedArray.getString(R.styleable.QuantityManager_validationTextRemove);

        typedArray.recycle();

        Button minusButton = findViewById(R.id.QuantityManager_ButtonMinus);
        Button plusButton = findViewById(R.id.QuantityManager_ButtonPlus);
        validtionButton = findViewById(R.id.QuantityManager_ButtonValidation);
        validtionButton.setText(validationTextAdd);
        numberEditText = findViewById(R.id.QuantityManager_EditTextNumber);

        numberEditText.addTextChangedListener(new TextWatcher(){
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){
            }

            @Override
            public void afterTextChanged(Editable s){
                try{
                    validtionButton.setText(Integer.parseInt(s.toString()) >= 0 ? validationTextAdd : validationTextRemove);
                }catch(NumberFormatException e){
                    validtionButton.setText(validationTextAdd);
                }
                try{
                    validtionButton.setEnabled(Integer.parseInt(s.toString()) != 0);
                }catch(NumberFormatException e){
                    validtionButton.setEnabled(false);
                }
            }
        });

        minusButton.setOnClickListener(v1->{
            try{
                numberEditText.setText(String.valueOf(getQuantity() - 1));
            }catch(NumberFormatException e){
                numberEditText.setText(null);
            }
        });

        plusButton.setOnClickListener(v->{
            try{
                numberEditText.setText(String.valueOf(getQuantity() + 1));
            }catch(NumberFormatException e){
                numberEditText.setText(null);
            }
        });

        numberEditTextDefaultValue = numberEditText.getHint().toString();
    }

    public void setOnClickListener(@Nullable OnClickListener l){
        validtionButton.setOnClickListener(l);
    }

    public int getQuantity(){
        int quantity;
        try{
            quantity = Integer.parseInt(numberEditText.getText().toString());
        }catch(NumberFormatException e){
            quantity = Integer.parseInt(numberEditTextDefaultValue);
        }
        numberEditText.setText(null);
        return quantity;
    }

    public void setText(CharSequence text){
        numberEditText.setText(text);
    }
}
