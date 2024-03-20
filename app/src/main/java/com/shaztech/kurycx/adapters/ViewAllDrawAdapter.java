package com.shaztech.kurycx.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shaztech.kurycx.R;
import com.shaztech.kurycx.models.Customers;

import java.util.ArrayList;

public class ViewAllDrawAdapter extends RecyclerView.Adapter<ViewAllDrawAdapter.ViewHolder>{
    private ArrayList<Customers> customersArrayList;
    private Context context;

    public ViewAllDrawAdapter(ArrayList<Customers> customersArrayList, Context context) {
        this.customersArrayList = customersArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.all_draw_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Customers customers = customersArrayList.get(position);

        holder.alldrawName.setText("Name: " + customers.getName());
        holder.alldrawLocation.setText("Address: " + customers.getAddress());
        holder.alldrawMonth.setText("Draw Week: " + customers.getDraw_week());


    }

    @Override
    public int getItemCount() {
        return customersArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView alldrawName;
        private final TextView alldrawLocation;
        private final TextView alldrawMonth;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            alldrawName = itemView.findViewById(R.id.singleAllDrawViewName);
            alldrawLocation = itemView.findViewById(R.id.singleAllDrawViewPlace);
            alldrawMonth = itemView.findViewById(R.id.singleAllDrawViewWeek);

        }
    }
}
