package com.virash.superretail;

import android.content.Context;
import android.content.Intent;
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

public class GetAdapterVendor extends RecyclerView.Adapter<GetAdapterVendor.ViewHolder> implements Filterable {
    Context context;
    List<ShowVendor_POJO> getEventsAdapter;
    ShowVendor_POJO getEventsAdapter1;
    List<ShowVendor_POJO> getEventsAdapter_filtered;

    public GetAdapterVendor(List<ShowVendor_POJO> getPackageAdapter1, Context context) {
        this.getEventsAdapter=getPackageAdapter1;
        getEventsAdapter_filtered = getPackageAdapter1;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.vendor_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        getEventsAdapter1 = getEventsAdapter_filtered.get(position);
        ((ViewHolder) holder).tv_name.setText(getEventsAdapter1.getName());
        ((ViewHolder) holder).tv_mobile.setText(getEventsAdapter1.getMobile());
        ((ViewHolder) holder).tv_email.setText(getEventsAdapter1.getEmail());
        ((ViewHolder) holder).tv_city.setText(getEventsAdapter1.getCity());
        ((ViewHolder) holder).tv_remark.setText(getEventsAdapter1.getRemark());
        ((ViewHolder) holder).tv_status.setText(getEventsAdapter1.getStatus());
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
                    List<ShowVendor_POJO> filteredList = new ArrayList<>();
                    for (ShowVendor_POJO row : getEventsAdapter) {
                        if (
                                (row.getName().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getMobile().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getCity().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getRemark().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getStatus().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getEmail().toLowerCase().contains(charString.toLowerCase()))

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
                getEventsAdapter_filtered = (List<ShowVendor_POJO>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_name,tv_mobile,tv_email,tv_city,tv_remark,tv_status;
        ShowVendor_POJO gt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_mobile = itemView.findViewById(R.id.tv_mobile);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_city = itemView.findViewById(R.id.tv_city);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            tv_status = itemView.findViewById(R.id.tv_status);

            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    Intent i= new Intent(context,VendorDetails_Activity.class);
                    i.putExtra("name",""+ getEventsAdapter.get(itemPosition).getName());
                    i.putExtra("mobile",""+ getEventsAdapter.get(itemPosition).getMobile());
                    i.putExtra("email",""+ getEventsAdapter.get(itemPosition).getEmail());
                    i.putExtra("city",""+ getEventsAdapter.get(itemPosition).getCity());
                    context.startActivity(i);
                }
            });

        }
    }
}
