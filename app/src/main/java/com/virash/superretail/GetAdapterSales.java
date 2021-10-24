package com.virash.superretail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class GetAdapterSales extends RecyclerView.Adapter<GetAdapterSales.ViewHolder>  implements Filterable {
    Context context;
    List<ShowSales_POJO> getEventsAdapter;
    ShowSales_POJO getEventsAdapter1;
    List<ShowSales_POJO> getEventsAdapter_filtered;


    public GetAdapterSales(List<ShowSales_POJO> getPackageAdapter1, Context context) {
        this.getEventsAdapter=getPackageAdapter1;
        getEventsAdapter_filtered = getPackageAdapter1;
        this.context=context;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.sales_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        getEventsAdapter1 = getEventsAdapter_filtered.get(position);
        ((ViewHolder) holder).tv_id.setText(getEventsAdapter1.getId());
        ((ViewHolder) holder).tv_name.setText(getEventsAdapter1.getName());
        ((ViewHolder) holder).tv_invoice.setText(getEventsAdapter1.getInvoice());
        ((ViewHolder) holder).tv_date.setText(getEventsAdapter1.getDate());
        ((ViewHolder) holder).tv_item.setText(getEventsAdapter1.getItem());
        ((ViewHolder) holder).tv_taxable.setText(getEventsAdapter1.getTaxable());
        ((ViewHolder) holder).tv_quantity.setText(getEventsAdapter1.getQuantity());
        ((ViewHolder) holder).tv_amount.setText(getEventsAdapter1.getAmount());
        ((ViewHolder) holder).tv_payment.setText(getEventsAdapter1.getPayment());
        ((ViewHolder) holder).gt = getEventsAdapter1;
    }

    @Override

    public int getItemCount() {
        return getEventsAdapter_filtered.size();
    }

    @Override
    public Filter getFilter()  {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    getEventsAdapter_filtered = getEventsAdapter;
                } else {
                    List<ShowSales_POJO> filteredList = new ArrayList<>();
                    for (ShowSales_POJO row : getEventsAdapter) {
                        if (
                                (row.getInvoice().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getAmount().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getDate().toLowerCase().contains(charString.toLowerCase())) ||
                                (row.getName().toLowerCase().contains(charString.toLowerCase()))

                        )
                        {
                            filteredList.add(row);
                        }
                    }
                    getEventsAdapter_filtered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = getEventsAdapter_filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                getEventsAdapter_filtered = (List<ShowSales_POJO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_id,tv_name,tv_invoice,tv_date,tv_item,tv_taxable,tv_quantity,tv_amount,tv_payment;
        ShowSales_POJO gt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_invoice = itemView.findViewById(R.id.tv_invoice);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_item = itemView.findViewById(R.id.tv_item);
            tv_taxable = itemView.findViewById(R.id.tv_taxable);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_payment = itemView.findViewById(R.id.tv_payment);
        }
    }
}
