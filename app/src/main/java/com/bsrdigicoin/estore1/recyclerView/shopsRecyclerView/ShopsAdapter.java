package com.bsrdigicoin.estore1.recyclerView.shopsRecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bsrdigicoin.estore1.DBproperties.Constants;
import com.bsrdigicoin.estore1.DBproperties.RequestHandler;
import com.bsrdigicoin.estore1.R;
import com.bsrdigicoin.estore1.recyclerView.ordersRecycerView.OrdersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopsAdapter extends RecyclerView.Adapter<ShopsAdapter.ShopViewHolder> {

    public static interface Listener {
        void onClick(int position);
    }
    private Listener listener;
    private Context context;
    public static class ShopViewHolder extends RecyclerView.ViewHolder{

        public LinearLayout container;
        public TextView shopname;
        public TextView shopcat;
        ShopViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.stores_row);
            shopname = view.findViewById(R.id.s_name);
            shopcat = view.findViewById(R.id.s_category);
        }
    }// class ends

    private List<Shop> shopsList = new ArrayList<>();

    public ShopsAdapter(Context context) {
        this.context = context;
        shopsList.add(new Shop("shop", "category"));
        getStores();
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.stores_row,
                parent,
                false
        );
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder holder, @SuppressLint("RecyclerView") int position) {
        LinearLayout cont = holder.container;
        TextView name = holder.shopname;
        TextView cat = holder.shopcat;
        Shop s = shopsList.get(position);
        name.setText(s.getName());
        cat.setText(s.getCat());

        // when the item is clicked
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shopsList.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void getStores() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SHOP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            if(!jsonObject.getBoolean("error")) {
                                JSONArray allshops = jsonObject.getJSONArray("shops");
                                // iterate over the array
                                for(int i=0; i<allshops.length(); i++) {
                                    JSONObject singleshop = allshops.getJSONObject(i);
                                    String name = singleshop.getString("sname");
                                    String cat = singleshop.getString("scategory");
                                    shopsList.add(new Shop(name, cat));
                                }
                                notifyDataSetChanged();
                            } else{
                                Toast.makeText(context,
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch(JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error getting information:/", Toast.LENGTH_SHORT).show();
                    }
                }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> map = new HashMap<>();
                            map.put("type", "0");
                            return map;
                        }
                };
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);
    }
}
