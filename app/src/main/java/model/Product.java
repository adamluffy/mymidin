package model;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;

public class Product {

    private String imageUrl;
    private String name;
    private String type;
    private int quantity;
    private double price;
    private @ServerTimestamp Date createAt;
    private Date updateAt;
    private String sellerId;

    public Product() {
    }

    public Product(String image, String name, String type, int quantity, double price, String sellerId) {
        this.imageUrl = image;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
        this.sellerId = sellerId;
    }

    @ServerTimestamp
    public Date getCreateAt() {

        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    @ServerTimestamp
    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }



    public String getImageUrl() {
        return imageUrl;
    }

    public Product setImage(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public Product setType(String type) {
        this.type = type;
        return this;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public void stockOut (int quantity){

        if(this.quantity>quantity){
            this.quantity-=quantity;
        }

    }

    public double getPrice() {
        return price;
    }

    public Product setPrice(double price) {
        this.price = price;
        return this;
    }



    public String getSellerId() {
        return sellerId;
    }


}
