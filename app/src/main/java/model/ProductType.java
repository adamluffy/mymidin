package model;

import java.util.ArrayList;

public enum ProductType {

    GROCERY("Grocery"),
    IT("Mobile,IT & Camera"),
    ClOTHING("Clothing"),
    STATIONARY("Stationary"),
    BAG("Bag & Shoe");

    private String id;

    ProductType(String id){
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static ArrayList<String> getTypes() {

        ArrayList<String> types = new ArrayList<>();

        for (ProductType type: ProductType.values()) {
            types.add(type.getId());
        }

        return types;
    }
}
