package com.example.invoicemaker.model;

public class Cart {

    private int cart_id;
    private int bid;
    private int invoice_id;
    private String product_name;
    private float product_price;
    private int quantity;
    private float tax_amount;
    private float total;

    public Cart(int cart_id, int bid, int invoice_id, String product_name, float product_price, int quantity, float tax_amount, float total)
    {
        this.cart_id = cart_id;
        this.bid = bid;
        this.invoice_id = invoice_id;
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.tax_amount = tax_amount;
        this.total = total;
    }

    public Cart(String product_name, float product_price, int quantity, float tax_amount, float total)
    {
        this.product_name = product_name;
        this.product_price = product_price;
        this.quantity = quantity;
        this.tax_amount = tax_amount;
        this.total = total;
    }

    public Cart()
    {

    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getBid() {
        return bid;
    }

    public void setBid(int bid) {
        this.bid = bid;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getProduct_price() {
        return product_price;
    }

    public void setProduct_price(float product_price) {
        this.product_price = product_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(float tax_amount) {
        this.tax_amount = tax_amount;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
