package com.bsrdigicoin.estore1.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bsrdigicoin.estore1.DBproperties.Constants;
import com.bsrdigicoin.estore1.DBproperties.RequestHandler;
import com.bsrdigicoin.estore1.R;
import com.bsrdigicoin.estore1.recyclerView.ordersRecycerView.Orders;
import com.bsrdigicoin.estore1.recyclerView.ordersRecycerView.OrdersAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OrdersFragment extends Fragment {

    public OrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RecyclerView orderRecycler = (RecyclerView) inflater.inflate(R.layout.fragment_orders, container, false);

        OrdersAdapter adapter = new OrdersAdapter(getActivity());
        orderRecycler.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(container.getContext());
//        StaggeredGridLayoutManager layoutManager =
//                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        //GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        orderRecycler.setLayoutManager(layoutManager);

        adapter.setListener(new OrdersAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getContext(), "clicked order:" + position, Toast.LENGTH_SHORT).show();
            }
        });
        return orderRecycler;
    }

}