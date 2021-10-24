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

class GetAdapterPurchase extends RecyclerView.Adapter<GetAdapterPurchase.ViewHolder> implements Filterable {

    Context context;
    List<ShowPurchase_POJO> getEventsAdapter;
    ShowPurchase_POJO getEventsAdapter1;
    List<ShowPurchase_POJO> getEventsAdapter_filtered;

    public GetAdapterPurchase(List<ShowPurchase_POJO> getPackageAdapter1, Context context) {
        this.getEventsAdapter=getPackageAdapter1;
        getEventsAdapter_filtered = getPackageAdapter1;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_item, parent, false);
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
        ((ViewHolder) holder).tv_cgst.setText(getEventsAdapter1.getCgst());
        ((ViewHolder) holder).tv_sgst.setText(getEventsAdapter1.getSgst());
        ((ViewHolder) holder).tv_totalItem.setText(getEventsAdapter1.getTotalItem());
        ((ViewHolder) holder).tv_taxableAmt.setText(getEventsAdapter1.getTaxableAmt());
        ((ViewHolder) holder).tv_quantity.setText(getEventsAdapter1.getQuantity());
        ((ViewHolder) holder).tv_amount.setText(getEventsAdapter1.getAmount());
        ((ViewHolder) holder).tv_remark.setText(getEventsAdapter1.getRemark());
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
                    List<ShowPurchase_POJO> filteredList = new ArrayList<>();
                    for (ShowPurchase_POJO row : getEventsAdapter) {
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
                getEventsAdapter_filtered = (List<ShowPurchase_POJO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_id,tv_name,tv_invoice,tv_date,tv_cgst,tv_sgst,tv_totalItem,tv_taxableAmt,tv_quantity,tv_amount,tv_remark;
        ShowPurchase_POJO gt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tv_id = itemView.findViewById(R.id.tv_id);
            tv_invoice = itemView.findViewById(R.id.tv_invoice);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_totalItem = itemView.findViewById(R.id.tv_totalItem);
            tv_cgst = itemView.findViewById(R.id.tv_cgst);
            tv_sgst = itemView.findViewById(R.id.tv_sgst);
            tv_taxableAmt = itemView.findViewById(R.id.tv_taxableAmt);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_quantity = itemView.findViewById(R.id.tv_quantity);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            tv_amount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
