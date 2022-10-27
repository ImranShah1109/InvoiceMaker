package com.example.invoicemaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.example.invoicemaker.data.MyDbHandler;
import com.example.invoicemaker.model.Buyer;

public class AddBuyerActivity extends AppCompatActivity{

    public static final String BUYERID  = "com.example.invoicemaker.BUYERID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_buyer);

        getIntent();
    }

    public void AddBuyerInfo(View view)
    {
        MyDbHandler dbHandler = new MyDbHandler(AddBuyerActivity.this);

        EditText name = findViewById(R.id.editTextBuyerName);
        String b_name = name.getText().toString();
        EditText address = findViewById(R.id.editTextBuyerAddress);
        String b_address = address.getText().toString();
        EditText mobile = findViewById(R.id.editTextBuyerPhone);
        String b_mobile = mobile.getText().toString();

        if(b_name.isEmpty())
        {
            name.setError("Please enter name");
        }
        else if(mobile.length() == 0)
        {
            mobile.setError("Mobile No. can't be empty");
        }
        else if(mobile.length() <10)
        {
            mobile.setError("Please enter valid mobile no.");
        }
        else if(dbHandler.checkMobile(b_mobile))
        {
            Intent intent = new Intent(this,NewInvoice.class);
            int bid;
            bid = dbHandler.getBuyerIdByMobile(b_mobile);
            String b = Integer.toString(bid);
            Log.d("bid",b);
            String save = "save";
            intent.putExtra("save_buyer",save);

            intent.putExtra(AddBuyerActivity.BUYERID,b);
            startActivity(intent);
        }
        else
        {
            Buyer buyer = new Buyer();
            buyer.setName(b_name);
            buyer.setAddress(b_address);
            buyer.setMobile(b_mobile);

            dbHandler.addBuyer(buyer);

            int bid;
            bid = dbHandler.getBuyerIdByMobile(b_mobile);
            String b = Integer.toString(bid);
            Log.d("bid",b);

            Intent intent = new Intent(this,NewInvoice.class);
            String save = "save";
            intent.putExtra("save_buyer",save);
            intent.putExtra(AddBuyerActivity.BUYERID,b);
            startActivity(intent);
        }
    }
}