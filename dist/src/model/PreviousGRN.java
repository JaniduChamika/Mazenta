/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ROG STRIX
 */
public class PreviousGRN {
    private String id;
    private String date;
    private String supplier;
    private String scontact;
    private String branch;
    private String total;
    private String method;
    private String paid;
    private String balance;

    public PreviousGRN(String id, String date, String supplier, String scontact, String branch, String total, String method, String paid, String balance) {
        this.id = id;
        this.date = date;
        this.supplier = supplier;
        this.scontact = scontact;
        this.branch = branch;
        this.total = total;
        this.method = method;
        this.paid = paid;
        this.balance = balance;
    }

    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getScontact() {
        return scontact;
    }

    public void setScontact(String scontact) {
        this.scontact = scontact;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
   
}
