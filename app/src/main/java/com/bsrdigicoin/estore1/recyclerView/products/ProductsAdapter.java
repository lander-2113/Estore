package com.bsrdigicoin.estore1.recyclerView.products;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bsrdigicoin.estore1.R;
import com.bsrdigicoin.estore1.recyclerView.shopsRecyclerView.ShopsAdapter;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductsViewHolder> {

    public static interface Listener {
        void onClick(int position);
    }
    private ShopsAdapter.Listener listener;
    private Context context;
    public static class ProductsViewHolder extends RecyclerView.ViewHolder{
        TextView prod_name;
        TextView prod_qty;
        TextView prod_price;
        ImageView prod_img;
        LinearLayout container;
        public ProductsViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.products_row);
            prod_name = itemView.findViewById(R.id.prod_name);
            prod_qty = itemView.findViewById(R.id.prod_qty);
            prod_price = itemView.findViewById(R.id.prod_img);
            prod_img = itemView.findViewById(R.id.prod_img);

        }
    }

    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
