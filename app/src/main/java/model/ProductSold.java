package model;

import java.util.HashMap;
import java.util.Map;

public class ProductSold {

    private String productName;
    private double productPrice;
    private String productType;
    private int productQty;

    public ProductSold(){}

    public ProductSold(String productName, double productPrice, int productQty, String productType) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public ProductSold setProductName(String productName) {
        this.productName = productName;
        return this;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public ProductSold setProductPrice(double productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public int getProductQty() {
        return productQty;
    }

    public void setProductQty(int productQty) {
        this.productQty = productQty;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }


}
