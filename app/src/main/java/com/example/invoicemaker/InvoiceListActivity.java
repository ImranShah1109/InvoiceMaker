package com.example.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.invoicemaker.adapter.RecyclerViewAdapter;
import com.example.invoicemaker.adapter.RecyclerViewAdapterInvoice;
import com.example.invoicemaker.data.MyDbHandler;
import com.example.invoicemaker.model.Invoice;

import java.util.ArrayList;
import java.util.List;

public class InvoiceListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapterInvoice recyclerViewAdapter;
    private ArrayList<Invoice> invoiceArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);

        MyDbHandler dbHandler = new MyDbHandler(InvoiceListActivity.this);

        recyclerView = findViewById(R.id.i_recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        invoiceArrayList = new ArrayList<>();

        List<Invoice> invoiceList = dbHandler.getAllInvoice();
        for(Invoice invoice : invoiceList)
        {
            invoiceArrayList.add(invoice);
        }

        recyclerViewAdapter = new RecyclerViewAdapterInvoice(InvoiceListActivity.this,invoiceList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}