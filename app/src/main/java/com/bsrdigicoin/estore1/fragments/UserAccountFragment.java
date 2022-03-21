package com.bsrdigicoin.estore1.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bsrdigicoin.estore1.R;

import org.w3c.dom.Text;


public class UserAccountFragment extends Fragment {

    TextView urername;
    TextView name;
    TextView mobile;
    TextView address;
    public UserAccountFragment() {
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
        return inflater.inflate(R.layout.fragment_user_account, container, false);
    }
}