package com.example.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.invoicemaker.data.MyDbHandler;
import com.example.invoicemaker.model.Buyer;
import com.example.invoicemaker.model.Cart;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

public class NewInvoice extends AppCompatActivity {

    public static final String BUYER = "com.example.invoicemaker.BUYER";
    public static final String INVOICE = "com.example.invoicemaker.INVOICE";

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice);

        MyDbHandler dbHandler = new MyDbHandler(NewInvoice.this);
        Intent intent = getIntent();

        //get buyer id from AddBuyerActivity
        String b;
        b = intent.getStringExtra(AddBuyerActivity.BUYERID);
        System.out.println(b);


        intent.getExtras();
        if(intent.hasExtra("save_buyer"))
        {
            Log.d("msg","come");
            Toast.makeText(NewInvoice.this,"Save successfully!",Toast.LENGTH_SHORT).show();
        }

        if(b != null)
        {
            List<Buyer> buyerList = dbHandler.getBuyerInfo(Integer.parseInt(b));
            String name = null,mobile = null,text;
            for(Buyer buyer : buyerList)
            {
                name = buyer.getName();
                mobile = buyer.getMobile();
            }
            text = name+ " " +mobile;
            TextView textView = (TextView) findViewById(R.id.textView4);
            textView.setText(text);
        }


        String bid = intent.getStringExtra(AddProductActivity.BUYERID);

        if(bid != null)
        {
            int invoice_id = dbHandler.getInvoiceByBuyer(Integer.parseInt(bid));
            ArrayList<String> cart = new ArrayList<>();
            listView = findViewById(R.id.cartlist);

            // For currency symbol
            Currency currency = Currency.getInstance("INR");
            String symbol = currency.getSymbol();

            List<Cart> cartList = dbHandler.getCartListByIds(Integer.parseInt(bid), invoice_id);
            for (Cart cart1 : cartList) {
                cart.add(cart1.getProduct_name() + "\t " + cart1.getProduct_price()+ symbol+ "\t " + cart1.getQuantity());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, cart);
            listView.setAdapter(arrayAdapter);
        }

    }


    public void AddBuyer(View view)
    {
        Intent intent = new Intent(this,AddBuyerActivity.class);
        startActivity(intent);
    }

    public void AddProduct(View view)
    {
        Intent intent = getIntent();
        String bid = "";
        bid = intent.getStringExtra(AddBuyerActivity.BUYERID);
        bid = intent.getStringExtra(AddProductActivity.BUYERID);
        System.out.println(bid);

        if(bid == null)
        {
            //bid = "0";
            Toast.makeText(NewInvoice.this,"Please Enter Buyer Information",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent1 = new Intent(this, AddProductActivity.class);
            intent1.putExtra(NewInvoice.BUYER, bid);
            startActivity(intent1);
        }
    }

    public void makeInvoice(View view)
    {
        Intent intent = getIntent();
        String bid = "", invoice_id ="";
        bid = intent.getStringExtra(AddProductActivity.BUYERID);
        invoice_id = intent.getStringExtra(AddProductActivity.INVOICEID);
        System.out.println( bid+ "and" +invoice_id);

        if(bid == null || invoice_id == null)
        {
            Toast.makeText(NewInvoice.this,"Please Enter Buyer And Product Information", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent1 = new Intent(this, MakeInvoiceActivity.class);
            intent1.putExtra(NewInvoice.BUYER, bid);
            intent1.putExtra(NewInvoice.INVOICE, invoice_id);
            startActivity(intent1);
        }

    }
}