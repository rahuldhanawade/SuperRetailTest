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


public class GetAdapterStock extends RecyclerView.Adapter<GetAdapterStock.ViewHolder> implements Filterable {

    Context context;
    List<ShowStock_POJO> getEventsAdapter;
    ShowStock_POJO getEventsAdapter1;
    List<ShowStock_POJO> getEventsAdapter_filtered;

    public GetAdapterStock(List<ShowStock_POJO> getPackageAdapter1, Context context) {
        this.getEventsAdapter=getPackageAdapter1;
        getEventsAdapter_filtered = getPackageAdapter1;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        getEventsAdapter1 = getEventsAdapter_filtered.get(position);
        ((ViewHolder) holder).tv_product.setText(getEventsAdapter1.getGroup_product());
    //    ((ViewHolder) holder).tv_id.setText(getEventsAdapter1.getGroup_id());
        ((ViewHolder) holder).tv_category.setText(getEventsAdapter1.getGroup_category());
        ((ViewHolder) holder).tv_status.setText(getEventsAdapter1.getGroup_status());
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
                    List<ShowStock_POJO> filteredList = new ArrayList<>();
                    for (ShowStock_POJO row : getEventsAdapter) {
                        if (
                                (row.getGroup_category().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getGroup_product().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getGroup_status().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getGroup_id().toLowerCase().contains(charString.toLowerCase()))

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
                getEventsAdapter_filtered = (List<ShowStock_POJO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_product,tv_id,tv_category,tv_status;
        ShowStock_POJO gt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_product = itemView.findViewById(R.id.tv_product);
            tv_id = itemView.findViewById(R.id.tv_id);
            tv_category = itemView.findViewById(R.id.tv_category);
            tv_status = itemView.findViewById(R.id.tv_status);


        }
    }
}

