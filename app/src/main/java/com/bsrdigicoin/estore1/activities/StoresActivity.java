package com.bsrdigicoin.estore1.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.bsrdigicoin.estore1.R;
import com.bsrdigicoin.estore1.recyclerView.shopsRecyclerView.ShopsAdapter;


public class StoresActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ShopsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stores);

        recyclerView = findViewById(R.id.store_recycler);
        adapter = new ShopsAdapter(getApplicationContext());
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_store);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        adapter.setListener(new ShopsAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getApplicationContext(),"Clicked=" + position , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

}