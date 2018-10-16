package model;

import com.google.firebase.firestore.Exclude;

import java.util.Date;

public class Seller {

    private String docId;
    private String username;
    private String email;
    private Date createdAt;


    private static Seller seller = new Seller();

    private Seller(){}

    private Seller(String username, String email, Date createdAt) {
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    @Exclude
    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }


    public Date getCreatedAt() {
        return createdAt;
    }




}
