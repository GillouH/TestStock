package com.example.teststock.models;

public class BasicItem extends Item{
    private Integer quantity;
    private final String unit;

    public BasicItem(String name, Integer quantity, String unit){
        super(name);
        this.quantity = quantity;
        this.unit = unit;
    }

    public Integer getQuantity(){
        return quantity;
    }

    public void addQuantiy(Integer quantity){
        if(quantity > 0){
            this.quantity += quantity;
        }
    }

    public void removeQuantity(Integer quantity){
        if(quantity > 0 && quantity <= this.quantity){
            this.quantity -= quantity;
        }
    }

    public String getUnit(){
        return this.unit;
    }

    @Override
    public String toString(){
        return "BasicItem{" +
                "\tID=" + getID() + ",\n" +
                "\tname='" + getName() + "',\n" +
                "\tquantity=" + quantity + ",\n" +
                "\tunit='" + unit + "'\n" +
                "}\n";
    }
}