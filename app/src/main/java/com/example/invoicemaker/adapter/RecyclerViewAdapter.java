package com.example.invoicemaker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.invoicemaker.R;
import com.example.invoicemaker.model.Buyer;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Buyer> buyerList;

    public RecyclerViewAdapter(Context context, List<Buyer> buyerList)
    {
        this.context = context;
        this.buyerList = buyerList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position)
    {
        Buyer buyer = buyerList.get(position);
        holder.name.setText(buyer.getName());
        holder.contact.setText(buyer.getMobile());
    }

    @Override
    public int getItemCount()
    {
        return buyerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public TextView name;
        public TextView contact;

        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            itemView.setOnClickListener(this);

            name = itemView.findViewById(R.id.name);
            contact = itemView.findViewById(R.id.contact);
        }

        @Override
        public void onClick(View view)
        {
            int position = this.getAbsoluteAdapterPosition();
//            Buyer buyer = buyerList.get(position);
//            int bid = buyer.getBid();
//            Toast.makeText(context,"Position :"+String.valueOf(position),Toast.LENGTH_SHORT).show();
//
//            Intent intent = new Intent(context, NewInvoice.class);
//            intent.putExtra("buyer",bid);
//            context.startActivity(intent);
        }
    }

}
