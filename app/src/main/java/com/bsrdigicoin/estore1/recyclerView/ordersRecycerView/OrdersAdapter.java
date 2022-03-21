package com.bsrdigicoin.estore1.recyclerView.ordersRecycerView;

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
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bsrdigicoin.estore1.DBproperties.Constants;
import com.bsrdigicoin.estore1.DBproperties.RequestHandler;
import com.bsrdigicoin.estore1.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    // when the item gets clicked we will handle it here
    public static interface Listener {
        void onClick(int position);
    }
    private Listener listener;
    private Context context;
    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout containerView;
        public TextView order_name;
        public TextView order_amt;
        public TextView order_status;
        public TextView applied_promo;
        public TextView final_amt;
        public OrderViewHolder(View view) {
            super(view);
            containerView =
                    view.findViewById(R.id.order_row);
            order_name = view.findViewById(R.id.order_name);
            order_status = view.findViewById(R.id.order_status);
            applied_promo = view.findViewById(R.id.applied_promocode);
            order_amt = view.findViewById(R.id.order_amount);
            final_amt = view.findViewById(R.id.final_amount);
        }
    }// class ends
    private List<Orders> ordersList = new ArrayList<>();

    public OrdersAdapter(Context context) {
        this.context = context;
        ordersList.add(new Orders("Order something", "", "", "", ""));
        loadOrders();
    }

    public List<Orders> loadOrders() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_USER_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("response", response);
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("error") && jsonObject.getBoolean("ordered")) {
                        JSONArray ordersArray = jsonObject.getJSONArray("orders");

                        for(int i=0; i<ordersArray.length(); i++) {
                            JSONObject ord = ordersArray.getJSONObject(i);
                            ordersList.add(new Orders(
                                    "Order" + i,
                                    "Amount = " + ord.getString("amount"),
                                    "Promo code = " + ord.getString("promocode"),
                                    "Status = " + ord.getString("orderaccepted"),
                                    "Final amount = " + ord.getString("finalamt")) );
                        }
                        notifyDataSetChanged();
                    } else  {
                        Toast.makeText(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch(JSONException e) {
                    Log.e("orders", "json error", e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("V", "VOLLEY ERROR", error);
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("type", "1");
                return map;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

        return ordersList;
    }
    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.orders_row,
                parent,
                false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, @SuppressLint("RecyclerView") int position) {
        LinearLayout orderRowView = holder.containerView;
        Orders order = ordersList.get(position);
        holder.order_name.setText(order.getOrder_name());
        holder.order_status.setText(order.getOrder_status());
        holder.order_amt.setText(String.valueOf(order.getOrder_amt()));
        holder.applied_promo.setText(order.getPromocode());
        holder.final_amt.setText(String.valueOf(order.getFinal_amt()));

        // setting the onclick for the row layout
        orderRowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });

        orderRowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
