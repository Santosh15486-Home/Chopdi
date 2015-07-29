package com.ssl.san.chopdi.Beans;

/**
 * Created by Santosh on 22-Jul-15.
 */
public class BillBean {
    int id;
    int customer;
    double p_height;
    String sHeight;
    String sWidth;
    double p_width;
    double p_size;
    double p_rate;
    double total;
    double paid;
    double discount;
    double remain;
    boolean paidByCheque;
    double chequeAmt;
    String chequeNo;
    String chequeDate;
    String chequeBank;
    String date;
    CustomerBean customerBean;

    public BillBean(){
        customerBean = new CustomerBean();
    }

    public CustomerBean getCustomerBean() {
        return customerBean;
    }

    public void setCustomerBean(CustomerBean customerBean) {
        this.customerBean = customerBean;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getsHeight() {
        return sHeight;
    }

    public void setsHeight(String sHeight) {
        this.sHeight = sHeight;
    }

    public String getsWidth() {
        return sWidth;
    }

    public void setsWidth(String sWidth) {
        this.sWidth = sWidth;
    }

    public boolean isPaidByCheque() {
        return paidByCheque;
    }

    public void setPaidByCheque(boolean paidByCheque) {
        this.paidByCheque = paidByCheque;
    }

    public double getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(double chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getChequeBank() {
        return chequeBank;
    }

    public void setChequeBank(String chequeBank) {
        this.chequeBank = chequeBank;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public double getP_height() {
        return p_height;
    }

    public void setP_height(double p_height) {
        this.p_height = p_height;
    }

    public double getP_width() {
        return p_width;
    }

    public void setP_width(double p_width) {
        this.p_width = p_width;
    }

    public double getP_size() {
        return p_size;
    }

    public void setP_size(double p_size) {
        this.p_size = p_size;
    }

    public double getP_rate() {
        return p_rate;
    }

    public void setP_rate(double p_rate) {
        this.p_rate = p_rate;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public double getRemain() {
        return remain;
    }

    public void setRemain(double remain) {
        this.remain = remain;
    }
}
