package model;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class ProductIn {

    private String docId;
    private Product product;
    private int qty;
    private @ServerTimestamp Date stockInDate;
    private String sellerId;

    public ProductIn() {
    }

    public ProductIn(Product product, int qty, String sellerId) {
        this.product = product;
        this.qty = qty;
        this.sellerId = sellerId;
    }

    @Exclude
    public void setDocId(String docId){
        this.docId = docId;
    }

    public String getDocId(){
        return this.docId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Date getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(Date stockInDate) {
        this.stockInDate = stockInDate;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
