package com.example.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;

import com.example.invoicemaker.adapter.RecyclerViewAdapter;
import com.example.invoicemaker.data.MyDbHandler;
import com.example.invoicemaker.model.Buyer;

import java.util.ArrayList;
import java.util.List;

public class BuyerListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<Buyer> buyerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MyDbHandler dbHandler = new MyDbHandler(BuyerListActivity.this);

        buyerArrayList = new ArrayList<>();

        List<Buyer> buyerList = dbHandler.getAllBuyer();
        for(Buyer buyer : buyerList)
        {
            buyerArrayList.add(buyer);
        }

        recyclerViewAdapter = new RecyclerViewAdapter(BuyerListActivity.this,buyerArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}