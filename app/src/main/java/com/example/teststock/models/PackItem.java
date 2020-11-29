package com.example.teststock.models;

public class PackItem extends Item{
    private Integer quantityOut;
    private final String unitInPack;
    private Integer nbPackFull;
    private final String packUnit;
    private final Integer quantityMaxInPack;

    public PackItem(String name, Integer quantityOut, String unitInPack, Integer nbPackFull, String packUnit, Integer quantityMaxInPack) {
        super(name);
        this.quantityOut = quantityOut;
        this.unitInPack = unitInPack;
        this.nbPackFull = nbPackFull;
        this.packUnit = packUnit;
        this.quantityMaxInPack = quantityMaxInPack;
    }

    private void addQuantity(Integer quantityToAdd){
        if(quantityToAdd > 0){
            quantityOut += quantityToAdd;
            if(quantityOut >= quantityMaxInPack){
                Integer nbPackToAdd = (int)(quantityOut/quantityMaxInPack);
                quantityOut -= nbPackToAdd * quantityMaxInPack;
                addPack(nbPackToAdd);
            }
        }
    }

    private void removeQuantity(Integer quantityToRemove){
        if(quantityToRemove > 0){
            //quantityOut -= quantityToRemove;
            if(quantityOut - quantityToRemove < 0){
                Integer nbPackToRemove = (int)(((quantityToRemove - quantityOut)/quantityMaxInPack)) + 1;
                if(nbPackToRemove <= nbPackFull){
                    quantityOut += nbPackToRemove*quantityMaxInPack - quantityToRemove;
                    if(nbPackToRemove > 0 && nbPackToRemove <= nbPackFull){
                        nbPackFull -= nbPackToRemove;
                    }
                }
            }else{
                quantityOut -= quantityToRemove;
            }
        }
    }

    private void addPack(Integer nbPackToAdd){
        if(nbPackToAdd > 0){
            nbPackFull += nbPackToAdd;
        }
    }

    private void removePack(Integer nbPackToRemove){
        if(nbPackToRemove > 0 && nbPackToRemove <= nbPackFull){
            nbPackFull -= nbPackToRemove;
        }
    }

    public Integer getQuantityOut(){
        return quantityOut;
    }

    public String getUnitInPack(){
        return unitInPack;
    }

    public Integer getNbPackFull(){
        return nbPackFull;
    }

    public String getPackUnit(){
        return packUnit;
    }

    public Integer getQuantityMaxInPack(){
        return quantityMaxInPack;
    }

    @Override
    public String toString(){
        return "PackItem{" +
                "\tID=" + getID() + ",\n" +
                "\tname='" + getName() + "',\n" +
                "\tquantityOut=" + quantityOut + ",\n" +
                "\tunitInPack='" + unitInPack + "',\n" +
                "\tnbPackFull=" + nbPackFull + ",\n" +
                "\tpackUnit='" + packUnit + "',\n" +
                "\tquantityMaxInPack=" + quantityMaxInPack + "\n" +
                "}\n";
    }
}
