package com.example.teststock.models;

import java.io.Serializable;

public abstract class Item implements Serializable{
    private static Integer IDCount = 0;
    private final Integer ID;
    private final String name;

    public Item(String name){
        this.ID = IDCount++;
        this.name = name;
    }

    public Integer getID(){
        return ID;
    }

    public String getName(){
        return name;
    }

    @Override
    public String toString(){
        return "Item{\n" +
                "\tID=" + ID + ",\n" +
                "\tname='" + name + "'\n" +
                "}\n";
    }
}
