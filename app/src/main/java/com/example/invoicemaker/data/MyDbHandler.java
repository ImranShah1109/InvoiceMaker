package com.example.invoicemaker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.example.invoicemaker.model.Buyer;
import com.example.invoicemaker.model.Cart;
import com.example.invoicemaker.model.Invoice;
import com.example.invoicemaker.params.Params;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MyDbHandler extends SQLiteOpenHelper
{
    public MyDbHandler(Context context) {
        super(context, Params.DB_NAME, null, Params.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Params.CREATE_TABLE_BUYER_DETAILS);
        Log.d("buyertable", "created successfully :" + Params.CREATE_TABLE_BUYER_DETAILS);
        db.execSQL(Params.CREATE_TABLE_INVOICE);
        Log.d("invoicetable", "created successfully :" + Params.CREATE_TABLE_INVOICE);
        db.execSQL(Params.CREATE_TABLE_CART);
        Log.d("carttable", "created successfully :" + Params.CREATE_TABLE_CART);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {

    }

    //Below all functions are related to buyer

    public void addBuyer(Buyer buyer)
    {
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_BUYER_NAME, buyer.getName());
        values.put(Params.KEY_BUYER_ADDRESS, buyer.getAddress());
        values.put(Params.KEY_BUYER_MOBILE, buyer.getMobile());

        database.insert(Params.TABLE_BUYER_DETAILS, null, values);
        Log.d("insertbuyer", "inserted successfully in buyer");
        database.close();
    }

    public boolean checkMobile(String mobile)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String sqlquery = "SELECT * FROM " + Params.TABLE_BUYER_DETAILS + " WHERE " + Params.KEY_BUYER_MOBILE + " = " + mobile + ";";
        Log.d("check", sqlquery);
        Cursor cursor = db.rawQuery(sqlquery, null);

        if (cursor.moveToFirst()) {
            String result;
            result = cursor.getString(cursor.getColumnIndex(Params.KEY_BUYER_MOBILE));
            Log.d("Result", result);
            return true;
        }
        else
        {
            return false;
        }
    }

    public int getBuyerIdByMobile(String mobile)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + Params.KEY_BUYER_ID + " FROM " + Params.TABLE_BUYER_DETAILS + " WHERE " + Params.KEY_BUYER_MOBILE + " = " + mobile + ";";
        Log.d("query", query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            int bid;
            bid = cursor.getInt(cursor.getColumnIndex(Params.KEY_BUYER_ID));
            String b = Integer.toString(bid);
            Log.d("buyer", b);
            return bid;
        }
        return 0;
    }

    public List<Buyer> getBuyerInfo(int bid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " +Params.TABLE_BUYER_DETAILS+ " WHERE " +Params.KEY_BUYER_ID+ " = " +bid+ ";";
        Cursor cursor = db.rawQuery(query,null);

        List<Buyer> buyerList = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            Buyer buyer = new Buyer();
            buyer.setBid(cursor.getInt(0));
            buyer.setName(cursor.getString(1));
            buyer.setAddress(cursor.getString(2));
            buyer.setMobile(cursor.getString(3));

            buyerList.add(buyer);
        }

        return buyerList;
    }

    public List<Buyer> getAllBuyer()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " +Params.TABLE_BUYER_DETAILS+ ";";
        Cursor cursor = db.rawQuery(query,null);

        List<Buyer> buyerList = new ArrayList<>();
        if(cursor.moveToFirst())
        {
            do
            {
                Buyer buyer = new Buyer();
                buyer.setBid(cursor.getInt(0));
                buyer.setName(cursor.getString(1));
                buyer.setAddress(cursor.getString(2));
                buyer.setMobile(cursor.getString(3));

                buyerList.add(buyer);

            }while(cursor.moveToNext());
        }
        return buyerList;
    }

    //Below all functions to related to invoice

    public void makeInvoice(Invoice invoice)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Params.KEY_BUYER_ID, invoice.getBid());

        db.insert(Params.TABLE_INVOICE, null, values);
        Log.d("makeInvoice", "invoice make successfully!");
        db.close();
    }

    public int getInvoiceByBuyer(int bid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " +Params.KEY_INVOICE_ID+ " FROM " +Params.TABLE_INVOICE+ " WHERE " +Params.KEY_BUYER_ID + " = " +bid+
                        " AND " +Params.KEY_FINAL_AMT+ " IS NULL;";

        Log.d("invoicebybuyer", query);
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            Log.d("invoicebybuyer","run query successfully");
            int invoice_id;
            invoice_id = cursor.getInt(cursor.getColumnIndex(Params.KEY_INVOICE_ID));

            String iid = Integer.toString(invoice_id);
            Log.d("invoice_id",iid);

            return invoice_id;
        }
        return 0;
    }

    public void completeInvoice(Invoice invoice,int invoice_id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Params.KEY_INVOICE_DATE,getDateTime());
        values.put(Params.KEY_TOTAL_QTY,invoice.getTotal_qty());
        values.put(Params.KEY_TOTAL_AMT,invoice.getTotal_amt());
        values.put(Params.KEY_TOTAL_TAX_AMT,invoice.getTotal_tax_amt());
        values.put(Params.KEY_FINAL_AMT,invoice.getFinal_amt());

        db.update(Params.TABLE_INVOICE,values,Params.KEY_INVOICE_ID+ " = ?", new  String[]{String.valueOf(invoice_id)});
        Log.d("complete","invoice completed");
        db.close();

    }

    public List<Invoice> getInvoice(int invoice_id)
    {
        List<Invoice> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " +Params.TABLE_INVOICE+ " WHERE " +Params.KEY_INVOICE_ID+ " = " +invoice_id+ ";";
        Log.d("invoice",query);
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            Invoice invoice = new Invoice();
            invoice.setInvoice_id(cursor.getInt(0));
            invoice.setBid(cursor.getInt(1));
            invoice.setInvoice_date(cursor.getString(2));
            invoice.setTotal_qty(cursor.getInt(3));
            invoice.setTotal_amt(cursor.getFloat(4));
            invoice.setTotal_tax_amt(cursor.getFloat(5));
            invoice.setFinal_amt(cursor.getFloat(6));

            list.add(invoice);
            Log.d("getInvoice",invoice.getInvoice_date());
        }
        return list;
    }

    public List<Invoice> getAllInvoice()
    {
        List<Invoice> invoiceList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = " SELECT * FROM " +Params.TABLE_INVOICE+ ";";
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do
            {
                Invoice invoice = new Invoice();
                invoice.setInvoice_id(cursor.getInt(0));
                invoice.setBid(cursor.getInt(1));
                invoice.setInvoice_date(cursor.getString(2));
                invoice.setFinal_amt(cursor.getFloat(6));

                invoiceList.add(invoice);

            }while(cursor.moveToNext());
        }
        return invoiceList;
    }

    //Below all functions are related to cart

    public void addProductToCart(Cart cart)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //db.execSQL(Params.CREATE_TABLE_CART);
        //Log.d("created cart","Successfully");
        ContentValues values = new ContentValues();

        values.put(Params.KEY_BUYER_ID, cart.getBid());
        values.put(Params.KEY_INVOICE_ID, cart.getInvoice_id());
        values.put(Params.KEY_PRODUCT_NAME, cart.getProduct_name());
        values.put(Params.KEY_PRODUCT_PRICE, cart.getProduct_price());
        values.put(Params.KEY_QUANTITY, cart.getQuantity());
        values.put(Params.KEY_TAX_AMT, cart.getTax_amount());
        values.put(Params.KEY_TOTAL, cart.getTotal());

        db.insert(Params.TABLE_CART, null, values);
        Log.d("cart", "inserterd into cart success");
        db.close();

    }

    public List<Cart> getCartListByIds(int bid,int invoice_id)
    {
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " +Params.TABLE_CART+ " WHERE " +Params.KEY_BUYER_ID+ " = " +bid+
                        " AND " +Params.KEY_INVOICE_ID+ " = " +invoice_id+ ";";

        Log.d("getcart",query);
        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst())
        {
            do
            {
                Cart cart = new Cart();
                cart.setCart_id(cursor.getInt(0));
                cart.setBid(cursor.getInt(1));
                cart.setInvoice_id(cursor.getInt(2));
                cart.setProduct_name(cursor.getString(3));
                cart.setProduct_price(cursor.getFloat(4));
                cart.setQuantity(cursor.getInt(5));
                cart.setTax_amount(cursor.getFloat(6));
                cart.setTotal(cursor.getFloat(7));

                cartList.add(cart);

            }while(cursor.moveToNext());
        }

        return cartList;
    }

    private String getDateTime()
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MM-yyyy HH:mm aa", Locale.getDefault());

        Date date = new Date();
        return dateFormat.format(date);
    }
}