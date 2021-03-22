package com.example.teststock.controllers;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.example.teststock.R;

public class QuantityManager extends RelativeLayout{
    private static final String BUNDLE_STATE_SUPER_STATE = "BUNDLE_STATE_SUPER_STATE";
    private static final String BUNDLE_STATE_NUMBER = "BUNDLE_STATE_QUANTITY";

    private Button validtionButton;
    private EditText numberEditText;
    private String validationTextAdd, validationTextRemove;
    private int numberDefaultValue;

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

        if(typedArray.getString(R.styleable.QuantityManager_validationTextAdd) != null){
            validationTextAdd = typedArray.getString(R.styleable.QuantityManager_validationTextAdd);
            validationTextAdd = getResources().getString(R.string.quantityManager_add);
        }

        if(typedArray.getString(R.styleable.QuantityManager_validationTextRemove) != null){
            validationTextRemove = typedArray.getString(R.styleable.QuantityManager_validationTextRemove);
            validationTextRemove = getResources().getString(R.string.quantityManager_remove);
        }

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
                validtionButton.setText(getNumberSimple() >= 0 ? validationTextAdd : validationTextRemove);
                validtionButton.setEnabled(getNumberSimple() != 0);
            }
        });

        minusButton.setOnClickListener(v1->setNumber(getNumberSimple() - 1));

        plusButton.setOnClickListener(v->setNumber(getNumberSimple() + 1));

        numberDefaultValue = Integer.parseInt(numberEditText.getHint().toString());
    }

    public void setOnClickListener(@Nullable OnClickListener l){
        validtionButton.setOnClickListener(l);
    }

    private Integer getNumberSimpleMayBeNull(){
        try{
            return Integer.parseInt(numberEditText.getText().toString());
        }catch(NumberFormatException e){
            return null;
        }
    }

    private int getNumberSimple(){
        Integer number = getNumberSimpleMayBeNull();
        return number == null ? numberDefaultValue : number;
    }

    public int getNumber(){
        int number = getNumberSimple();
        setNumber(null);
        return number;
    }

    public void setNumber(int number){
        numberEditText.setText(String.valueOf(number));
    }

    public void setNumber(String text){
        try{
            setNumber(Integer.parseInt(text));
        }catch(NumberFormatException e){
            numberEditText.setText(null);
        }
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState(){
        Bundle bundle = new Bundle();
        bundle.putParcelable(BUNDLE_STATE_SUPER_STATE, super.onSaveInstanceState());
        bundle.putParcelable(BUNDLE_STATE_NUMBER, numberEditText.onSaveInstanceState());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state){
        if(state instanceof Bundle){
            Bundle bundle = (Bundle)state;
            state = bundle.getParcelable(BUNDLE_STATE_SUPER_STATE);
            numberEditText.onRestoreInstanceState(bundle.getParcelable(BUNDLE_STATE_NUMBER));
        }
        super.onRestoreInstanceState(state);
    }

    @Override
    protected void dispatchSaveInstanceState(SparseArray<Parcelable> container){
        dispatchFreezeSelfOnly(container);
    }

    @Override
    protected void dispatchRestoreInstanceState(SparseArray<Parcelable> container){
        dispatchThawSelfOnly(container);
    }
}
