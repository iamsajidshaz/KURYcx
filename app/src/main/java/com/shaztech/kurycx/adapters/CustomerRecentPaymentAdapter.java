package com.shaztech.kurycx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shaztech.kurycx.models.CustomerPayments;
import com.shaztech.kurycx.R;

import java.util.ArrayList;

public class CustomerRecentPaymentAdapter extends RecyclerView.Adapter<CustomerRecentPaymentAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private final ArrayList<CustomerPayments> customerPaymentsArrayList;
    private final Context context;

    // creating constructor for our adapter class
    public CustomerRecentPaymentAdapter(ArrayList<CustomerPayments> customerPaymentsArrayList, Context context) {
        this.customerPaymentsArrayList = customerPaymentsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cust_recent_payment_entry_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // setting data to our text views from our modal class.
        CustomerPayments customerPayments = customerPaymentsArrayList.get(position);

        holder.cxPaidWeek.setText(String.format("Paid for: %s", customerPayments.getCxPaidWeek()));
        holder.cxPaidAmount.setText(String.format("Amount: %s", customerPayments.getCxPaidAmount()));
        holder.cxPaidOn.setText(String.format("Paid on: %s", customerPayments.getCxPaidOn()));


    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return customerPaymentsArrayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView cxPaidWeek;
        private final TextView cxPaidAmount;
        private final TextView cxPaidOn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            cxPaidWeek = itemView.findViewById(R.id.cxRecentPaymentWeekTvItem);
            cxPaidAmount = itemView.findViewById(R.id.cxRecentPaymentAmountTvItem);
            cxPaidOn = itemView.findViewById(R.id.cxRecentPaymentDateTvItem);
        }
    }
}
