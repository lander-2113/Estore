package com.bsrdigicoin.estore1.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bsrdigicoin.estore1.DBproperties.Constants;
import com.bsrdigicoin.estore1.DBproperties.RequestHandler;
import com.bsrdigicoin.estore1.DBproperties.SharedPrefManager;
import com.bsrdigicoin.estore1.MainActivity;
import com.bsrdigicoin.estore1.R;
import com.bsrdigicoin.estore1.activities.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {


    private EditText s_user_name, s_fname, s_lname, s_pswd, s_email, s_mobno;
    private Button sbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        if(SharedPrefManager.getInstance(this).isLoogedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }

        try{
            TextView nav_header_username = (TextView)findViewById(R.id.nav_head_username);
            nav_header_username.setText(SharedPrefManager.getInstance(getApplicationContext()).getUsername());
        } catch(Exception e) {
            Log.e("NH", e.getMessage(), e);
        }

        s_user_name = findViewById(R.id.sign_user_name_text);
        s_fname = findViewById(R.id.signfnametext);
        s_lname = findViewById(R.id.sign_lnametext);
        s_pswd = findViewById(R.id.sign_pswdtext);
        s_email = findViewById(R.id.sign_emailtext);
        s_mobno = findViewById(R.id.sign_mnotext);
        sbtn = findViewById(R.id.btnsignup);

        sbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_user(getApplicationContext(),
                        s_user_name.getText().toString().trim(),
                        s_fname.getText().toString().trim(),
                        s_lname.getText().toString().trim(),
                        s_pswd.getText().toString().trim(),
                        s_email.getText().toString().trim(),
                        s_mobno.getText().toString().trim());
            }
        });



    }

    public void register_user(Context context, final String user, final String fname,
                              final String lname, final String pswd,
                              final String email, final String phone){

        if(user.length() <= 0 || fname.length() <= 0 || lname.length() <= 0
                || pswd.length() <= 0 || email.length() <= 0 || phone.length() <= 0) {
            Toast.makeText(getApplicationContext(),
                    "All the fields are required",
                    Toast.LENGTH_SHORT).show();
        } else {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.URL_REGISTER, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //resetFields();

                    // here the user can go to the login page or the dashboard.
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean("error")) {
                            finish();
                            startActivity(new Intent(context, MainActivity.class));
                        } else{
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }
                    } catch(JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //resetFields();
                    Log.e("volleyerror", "some error", error);
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", user);
                    map.put("fname", fname);
                    map.put("lname", lname);
                    map.put("password", pswd);
                    map.put("email", email);
                    map.put("mobile", phone);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        map.put("timecreated", LocalDateTime.now().toString());
                    }
                    return map;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        }
    }// ends
}