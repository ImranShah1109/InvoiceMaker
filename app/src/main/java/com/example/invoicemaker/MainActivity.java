package com.example.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void NewInvoice(View view)
    {
        Intent intent = new Intent(this,NewInvoice.class);
        startActivity(intent);
    }

    public void BuyerList(View view)
    {
        Intent intent = new Intent(this,BuyerListActivity.class);
        startActivity(intent);
    }

    public void InvoiceList(View view)
    {
        Intent intent = new Intent(this,InvoiceListActivity.class);
        startActivity(intent);
    }
}