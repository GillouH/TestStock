package com.example.teststock.models;

public class PackItem extends Item{
    private Float quantityOut;
    private final String unitInPack;
    private Integer nbPackFull;
    private final String packUnit;
    private final Float quantityMaxInPack;

    public PackItem(String name, Float quantityOut, String unitInPack, Integer nbPackFull, String packUnit, Float quantityMaxInPack) {
        super(name);
        this.quantityOut = quantityOut;
        this.unitInPack = unitInPack;
        this.nbPackFull = nbPackFull;
        this.packUnit = packUnit;
        this.quantityMaxInPack = quantityMaxInPack;
    }

    private void addQuantity(Float quantityToAdd){
        if(quantityToAdd > 0){
            quantityOut += quantityToAdd;
            if(quantityOut >= quantityMaxInPack){
                Integer nbPackToAdd = (int)(quantityOut/quantityMaxInPack);
                quantityOut -= nbPackToAdd * quantityMaxInPack;
                addPack(nbPackToAdd);
            }
        }
    }

    private void removeQuantity(Float quantityToRemove){
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

    public Float getQuantityOut(){
        return quantityOut;
    }

    public String getQuantityOutStr(){
        if(quantityOut == quantityOut.intValue()){
            return Integer.toString(quantityOut.intValue());
        }else{
            return quantityOut.toString();
        }
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

    public Float getQuantityMaxInPack(){
        return quantityMaxInPack;
    }

    public String getQuantityMaxInPackStr(){
        if(quantityMaxInPack == quantityMaxInPack.intValue()){
            return Integer.toString(quantityMaxInPack.intValue());
        }else{
            return quantityMaxInPack.toString();
        }
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
