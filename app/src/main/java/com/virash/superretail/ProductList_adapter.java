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

class ProductList_adapter extends RecyclerView.Adapter<ProductList_adapter.ViewHolder> implements Filterable {

    Context context;
    List<ProductList_pojo>ProductList_Adapter;
    ProductList_pojo ProductList_Adapter2;
    List<ProductList_pojo> getProductAdapter_filtered;

    public ProductList_adapter(List<ProductList_pojo> getProductList_adapter, Context context) {

        this.ProductList_Adapter=getProductList_adapter;
        getProductAdapter_filtered = getProductList_adapter;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.productlist_items,parent,false);
        ViewHolder viewHolder =new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ProductList_Adapter2= getProductAdapter_filtered.get(position);
//        holder.tv_product_id.setText(ProductList_Adapter2.getProduct_id());
        holder.tv_product_name.setText(ProductList_Adapter2.getProduct_name());
        holder.tv_barcode_no.setText(ProductList_Adapter2.getBarcode_no());
        holder.tv_group_name.setText(ProductList_Adapter2.getGroup_name());
        holder.tv_quantity.setText(ProductList_Adapter2.getQuantity());
        holder.tv_selling_price.setText(ProductList_Adapter2.getSelling_price());
        holder.pojo3=ProductList_Adapter2;
    }

    @Override
    public int getItemCount() {
        return getProductAdapter_filtered.size();
    }

    @Override
    public Filter getFilter()  {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    getProductAdapter_filtered = ProductList_Adapter;
                } else {
                    List<ProductList_pojo> filteredList = new ArrayList<>();
                    for (ProductList_pojo row : ProductList_Adapter) {
                        if (
                                (row.getBarcode_no().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getGroup_name().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getProduct_name().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getQuantity().toLowerCase().contains(charString.toLowerCase())) ||
                                        (row.getSelling_price().toLowerCase().contains(charString.toLowerCase()))

                        )
                        {
                            filteredList.add(row);
                        }
                    }
                    getProductAdapter_filtered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = getProductAdapter_filtered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                getProductAdapter_filtered = (List<ProductList_pojo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_product_id,tv_barcode_no,tv_product_name,tv_group_name,tv_quantity,tv_selling_price;
        ProductList_pojo pojo3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            tv_product_id=itemView.findViewById(R.id.tv_product_id);
            tv_barcode_no=itemView.findViewById(R.id.tv_barcode_no);
            tv_product_name=itemView.findViewById(R.id.tv_product_name);
            tv_group_name=itemView.findViewById(R.id.tv_group_name);
            tv_quantity=itemView.findViewById(R.id.tv_quantity);
            tv_selling_price=itemView.findViewById(R.id.tv_selling_price);

            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemPosition = getLayoutPosition();
                    Intent i= new Intent(context,ProductDetails.class);
                    i.putExtra("barcode",""+ ProductList_Adapter.get(itemPosition).getBarcode_no());
                    context.startActivity(i);
                }
            });
        }
    }

}
