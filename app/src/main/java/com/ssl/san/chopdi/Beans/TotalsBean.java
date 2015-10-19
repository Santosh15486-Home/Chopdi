package com.ssl.san.chopdi.Beans;

/**
 * Created by Santosh on 26-Jul-15.
 */
public class TotalsBean {
    Double total;
    Double remain;
    Double discount;
    Double paid;
    Double chequeAmt;
    Double expense;
    Double collection;

    public Double getCollection() {
        return collection;
    }

    public void setCollection(Double collection) {
        this.collection = collection;
    }

    public Double getExpense() {
        return expense;
    }

    public void setExpense(Double expense) {
        this.expense = expense;
    }

    public Double getChequeAmt() {
        return chequeAmt;
    }

    public void setChequeAmt(Double chequeAmt) {
        this.chequeAmt = chequeAmt;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Double getRemain() {
        return remain;
    }

    public void setRemain(Double remain) {
        this.remain = remain;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getPaid() {
        return paid;
    }

    public void setPaid(Double paid) {
        this.paid = paid;
    }
}
