/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author ROG STRIX
 */
public class GrnReport {

    private String mid;
    private String mname;
    private String category;
    private String mfd;
    private String exd;
    private String qty;
    private String bprice;
    private String total;

    public GrnReport(String mid, String mname, String category, String mfd, String exd, String qty, String bprice, String total) {
        this.mid = mid;
        this.mname = mname;
        this.category = category;
        this.mfd = mfd;
        this.exd = exd;
        this.qty = qty;
        this.bprice = bprice;
        this.total = total;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMfd() {
        return mfd;
    }

    public void setMfd(String mfd) {
        this.mfd = mfd;
    }

    public String getExd() {
        return exd;
    }

    public void setExd(String exd) {
        this.exd = exd;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getBprice() {
        return bprice;
    }

    public void setBprice(String bprice) {
        this.bprice = bprice;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
