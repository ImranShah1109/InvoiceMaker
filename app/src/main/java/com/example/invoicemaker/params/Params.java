package com.example.invoicemaker.params;

public class Params {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "invoice_maker";
    public static final String TABLE_BUYER_DETAILS = "buyer_details";
    public static final String TABLE_INVOICE = "invoice_master";
    public static final String TABLE_CART = "cart";

    // Buyer Details tables key or columns
    public static final String KEY_BUYER_ID = "bid";
    public static final String KEY_BUYER_NAME = "name";
    public static final String KEY_BUYER_ADDRESS = "address";
    public static final String KEY_BUYER_MOBILE = "mobile";

    public static final String CREATE_TABLE_BUYER_DETAILS = "CREATE TABLE "
                                +TABLE_BUYER_DETAILS+ "(" +KEY_BUYER_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, " +KEY_BUYER_NAME+
                                " TEXT, " +KEY_BUYER_ADDRESS+ " TEXT, " +KEY_BUYER_MOBILE+ " TEXT NOT NULL UNIQUE);";

    // Invoice Master tables key or columns

    public static final String KEY_INVOICE_ID = "invoice_id";
    public static final String KEY_INVOICE_DATE = "invoice_date";
    public static final String KEY_TOTAL_QTY = "total_qty";
    public static final String KEY_TOTAL_AMT = "total_amt";
    public static final String KEY_TOTAL_TAX_AMT = "total_tax_amt";
    public static final String KEY_FINAL_AMT = "final_amt";

    public static final String CREATE_TABLE_INVOICE = "CREATE TABLE "
                                +TABLE_INVOICE+ "(" +KEY_INVOICE_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
                                +KEY_BUYER_ID+ " INTEGER NOT NULL, "
                                +KEY_INVOICE_DATE+ " TEXT," +KEY_TOTAL_QTY+ " INTEGER,"
                                +KEY_TOTAL_AMT+ " REAL," +KEY_TOTAL_TAX_AMT+ " REAL,"
                                +KEY_FINAL_AMT+ " REAL, " +
                                "FOREIGN KEY (" +KEY_BUYER_ID+ ") REFERENCES " +TABLE_BUYER_DETAILS+" ("+KEY_BUYER_ID+"));";

    // Cart tables key or columns

    public static final String KEY_CART_ID = "cart_id";
    public static final String KEY_PRODUCT_NAME = "product_name";
    public static final String KEY_PRODUCT_PRICE = "product_price";
    public static final String KEY_QUANTITY = "quantity";
    public static final String KEY_TAX_AMT = "tax_amount";
    public static final String KEY_TOTAL = "total";

    public static final String CREATE_TABLE_CART = "CREATE TABLE " +TABLE_CART+
                                "(" +KEY_CART_ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                +KEY_BUYER_ID+ " INTEGER NOT NULL, "
                                +KEY_INVOICE_ID+ " INTEGER NOT NULL, "
                                +KEY_PRODUCT_NAME+ " TEXT, " +KEY_PRODUCT_PRICE+ " REAL, "
                                +KEY_QUANTITY+ " INTEGER, " +KEY_TAX_AMT+ " REAL, "
                                +KEY_TOTAL+ " REAL,"+
                                "FOREIGN KEY (" +KEY_BUYER_ID+ ")REFERENCES "+TABLE_BUYER_DETAILS+" ("+KEY_BUYER_ID+")," +
                                "FOREIGN KEY (" +KEY_BUYER_ID+ ")REFERENCES "+TABLE_BUYER_DETAILS+" ("+KEY_BUYER_ID+"));";

}
