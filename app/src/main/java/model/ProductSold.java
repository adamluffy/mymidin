package model;

public class ProductSold {

    private String productName;
    private double productPrice;
    private int productQty;

    public ProductSold(){}

    public ProductSold(String productName, double productPrice, int productQty) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQty = productQty;
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

    public ProductSold setProductQty(int productQty) {
        this.productQty = productQty;
        return this;
    }
}
