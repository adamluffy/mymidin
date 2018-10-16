package model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;

public class Sales {

    private String salesNumber;
    private Date date;
    private Customer customer;
    private ArrayList<ProductSold> products;
    private double totalAmount;
    private String sellerId;
    private Date updateAt;

    public Sales() {
    }

    public Sales(String salesNumber, Date date, Customer customer, ArrayList<ProductSold> products, double totalAmount, String sellerId) {
        this.salesNumber = salesNumber;
        this.date = date;
        this.customer = customer;
        this.products = products;
        this.totalAmount = totalAmount;
        this.sellerId = sellerId;
    }

    public String getSalesNumber() {
        return salesNumber;
    }

    public void setSalesNumber(String salesNumber) {
        this.salesNumber = salesNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ArrayList<ProductSold> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductSold> products) {
        this.products = products;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void addProduct(ProductSold product){
        this.products.add(product);
    }

    public void removeProduct(int index){
        this.products.remove(index);
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    @Exclude
    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
