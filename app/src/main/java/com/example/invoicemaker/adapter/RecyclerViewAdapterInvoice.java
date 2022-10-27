package com.example.invoicemaker.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoicemaker.PdfViewerActivity;
import com.example.invoicemaker.R;
import com.example.invoicemaker.data.MyDbHandler;
import com.example.invoicemaker.model.Buyer;
import com.example.invoicemaker.model.Invoice;

import java.util.List;

public class RecyclerViewAdapterInvoice extends RecyclerView.Adapter<RecyclerViewAdapterInvoice.ViewHolder> {

    Context context;
    List<Invoice> invoiceList;

    public RecyclerViewAdapterInvoice(Context context, List<Invoice> invoiceList)
    {
        this.context = context;
        this.invoiceList = invoiceList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapterInvoice.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invoicelist,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapterInvoice.ViewHolder holder, int position)
    {
        Invoice invoice = invoiceList.get(position);
        holder.invoice_no.setText(Integer.toString(invoice.getInvoice_id()));
        holder.datetime.setText(invoice.getInvoice_date());
        holder.billAmount.setText(Float.toString(invoice.getFinal_amt()));

        int b = invoice.getBid();
        MyDbHandler dbHandler = new MyDbHandler(context);
        List<Buyer> buyerList = dbHandler.getBuyerInfo(b);
        for(Buyer buyer : buyerList)
        {
            holder.customerName.setText(buyer.getName());
        }

    }

    @Override
    public int getItemCount()
    {
        return invoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView invoice_no;
        public TextView customerName;
        public TextView datetime;
        public TextView billAmount;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            invoice_no = itemView.findViewById(R.id.invoice_no);
            customerName = itemView.findViewById(R.id.customerName);
            datetime = itemView.findViewById(R.id.datetime);
            billAmount = itemView.findViewById(R.id.billAmount);
        }

        @Override
        public void onClick(View view)
        {
            int position = this.getAbsoluteAdapterPosition();
            Invoice invoice = invoiceList.get(position);
            int iid = invoice.getInvoice_id();
            int bid = invoice.getBid();
            String s_bid = Integer.toString(bid);
            String s_iid = Integer.toString(iid);

            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra("invoice_id",s_iid);
            intent.putExtra("buyer_id",s_bid);
            context.startActivity(intent);
        }
    }
}
