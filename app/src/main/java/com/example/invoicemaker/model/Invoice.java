package com.example.invoicemaker.model;

import java.util.Date;

public class Invoice {

    private int invoice_id;
    private int bid;
    private String invoice_date;
    private int total_qty;
    private float total_amt;
    private float total_tax_amt;
    private float final_amt;

    public Invoice(int invoice_id, int bid, String invoice_date, int total_qty, float total_amt, float total_tax_amt, float final_amt)
    {
        this.invoice_id = invoice_id;
        this.bid = bid;
        this.invoice_date = invoice_date;
        this.total_qty = total_qty;
        this.total_amt = total_amt;
        this.total_tax_amt = total_tax_amt;
        this.final_amt = final_amt;
    }

    public Invoice(int invoice_id, String invoice_date, int total_qty, float total_amt, float total_tax_amt, float final_amt)
    {
        this.invoice_id = invoice_id;
        this.invoice_date = invoice_date;
        this.total_qty = total_qty;
        this.total_amt = total_amt;
        this.total_tax_amt = total_tax_amt;
        this.final_amt = final_amt;
    }

    public Invoice(String invoice_date, int total_qty, float total_amt, float total_tax_amt, float final_amt)
    {
        this.invoice_date = invoice_date;
        this.total_qty = total_qty;
        this.total_amt = total_amt;
        this.total_tax_amt = total_tax_amt;
        this.final_amt = final_amt;
    }

    public Invoice()
    {

    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    public int getTotal_qty() {
        return total_qty;
    }

    public void setTotal_qty(int total_qty) {
        this.total_qty = total_qty;
    }

    public float getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(float total_amt) {
        this.total_amt = total_amt;
    }

    public float getTotal_tax_amt() {
        return total_tax_amt;
    }

    public void setTotal_tax_amt(float total_tax_amt) {
        this.total_tax_amt = total_tax_amt;
    }

    public float getFinal_amt() {
        return final_amt;
    }

    public void setFinal_amt(float final_amt) {
        this.final_amt = final_amt;
    }
}
