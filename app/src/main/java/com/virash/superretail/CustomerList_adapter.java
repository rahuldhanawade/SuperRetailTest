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

class CustomerList_adapter extends RecyclerView.Adapter<CustomerList_adapter.ViewHolder> implements Filterable {

    Context context;
    List<CustomerList_pojo>customerList_Adapter;
    CustomerList_pojo customerList_Adapter2;
    List<CustomerList_pojo> getCustomerAdapter_filtered;

    public CustomerList_adapter(List<CustomerList_pojo> getCustomerList_adapter, Context context) {
        this.customerList_Adapter=getCustomerList_adapter;
        getCustomerAdapter_filtered = getCustomerList_adapter;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.customerlist_items,parent,false);
        ViewHolder viewHolder =new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        customerList_Adapter2=getCustomerAdapter_filtered.get(position);
        holder.tv_full_name.setText(customerList_Adapter2.getFull_name());
       // holder.tv_date.setText(customerList_Adapter2.getDate());
        holder.tv_mobile.setText(customerList_Adapter2.getMobile());
       // holder.tv_email.setText(customerList_Adapter2.getEmail());
       // holder.tv_area.setText(customerList_Adapter2.getArea());
        holder.pojo3=customerList_Adapter2;
    }

    @Override
    public int getItemCount() {
        return getCustomerAdapter_filtered.size();
    }

    @Override
    public Filter getFilter()  {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    getCustomerAdapter_filtered = customerList_Adapter;
                } else {
                    List<CustomerList_pojo> filteredList = new ArrayList<>();
                    for (CustomerList_pojo row : customerList_Adapter) {
                        if (
                                (row.getMobile().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getEmail().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getDate().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getArea().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getFull_name().toLowerCase().contains(charString.toLowerCase()))

                        )
                        {
                            filteredList.add(row);
                        }
                    }
                    getCustomerAdapter_filtered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = getCustomerAdapter_filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                getCustomerAdapter_filtered = (List<CustomerList_pojo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_full_name,tv_date,tv_mobile,tv_email,tv_area;
        CustomerList_pojo pojo3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_full_name=itemView.findViewById(R.id.tv_full_name);
            //tv_date=itemView.findViewById(R.id.tv_date);
            tv_mobile=itemView.findViewById(R.id.tv_mobile);
            //tv_email=itemView.findViewById(R.id.tv_email);
           // tv_area=itemView.findViewById(R.id.tv_area);

            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    Intent i= new Intent(context,Add_Customer.class);
                    i.putExtra("fullname",""+ customerList_Adapter.get(itemPosition).getFull_name());
                    i.putExtra("mobile",""+ customerList_Adapter.get(itemPosition).getMobile());
                    context.startActivity(i);
                }
        });
    }
    }
}
