package com.example.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.invoicemaker.data.MyDbHandler;
import com.example.invoicemaker.model.Cart;
import com.example.invoicemaker.model.Invoice;

public class AddProductActivity extends AppCompatActivity
{
    public static final String BUYERID  = "com.example.invoicemaker.BUYERID";
    public static final String INVOICEID = "com.example.invoicemaker.INVOICEID";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        getIntent();
    }

    public void AddProductToCart(View view)
    {
        EditText name = findViewById(R.id.ProductName);
        String product_name = name.getText().toString();
        EditText price = findViewById(R.id.ProductPrice);
        String product_price = price.getText().toString();
        EditText tax = findViewById(R.id.ProductTax);
        String product_tax = tax.getText().toString();
        EditText quantity = findViewById(R.id.ProductQuantity);
        String product_quantity = quantity.getText().toString();

        MyDbHandler dbHandler = new MyDbHandler(AddProductActivity.this);

        Intent intent = getIntent();
        String buyer = intent.getStringExtra(NewInvoice.BUYER);
        System.out.println(buyer);
        int bid = Integer.parseInt(buyer);

        Cart cart = new Cart();


            if(product_name.isEmpty())
            {
                name.setError("Please Enter Product Name");
            }
            else if(product_price.isEmpty())
            {
                price.setError("Please Enter Product Price");
            }
            else if(product_quantity.isEmpty())
            {
                quantity.setError("Please Enter Quantity of Product");
            }
            else
            {
                Invoice invoice = new Invoice();
                invoice.setBid(bid);


                int invoice_id;
                invoice_id = dbHandler.getInvoiceByBuyer(bid);
                if(invoice_id == 0)
                {
                    dbHandler.makeInvoice(invoice);
                }
                invoice_id = dbHandler.getInvoiceByBuyer(bid);
                String iid = Integer.toString(invoice_id);
                Log.d("getInvoiceId", iid);

                float p_price = Float.parseFloat(product_price);
                int p_quantity = Integer.parseInt(product_quantity);

                cart.setBid(bid);
                cart.setInvoice_id(invoice_id);
                cart.setProduct_name(product_name);
                cart.setProduct_price(p_price);
                cart.setQuantity(p_quantity);

                if(product_tax.isEmpty())
                {
                    float p_tax = 0.0f;
                    float total;
                    total = p_price * p_quantity;
                    cart.setTax_amount(p_tax);
                    cart.setTotal(total);
                }
                else
                {
                    float p_tax = Float.parseFloat(product_tax);
                    float total;
                    total = (p_price + p_tax) * p_quantity;
                    cart.setTax_amount(p_tax);
                    cart.setTotal(total);
                }

                dbHandler.addProductToCart(cart);

                intent = new Intent(this,NewInvoice.class);
                intent.putExtra(AddProductActivity.INVOICEID,iid);
                intent.putExtra(AddProductActivity.BUYERID,buyer);
                startActivity(intent);
            }

    }
}
