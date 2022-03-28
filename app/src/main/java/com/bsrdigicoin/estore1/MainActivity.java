package com.bsrdigicoin.estore1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bsrdigicoin.estore1.DBproperties.Constants;
import com.bsrdigicoin.estore1.DBproperties.RequestHandler;
import com.bsrdigicoin.estore1.DBproperties.SharedPrefManager;
import com.bsrdigicoin.estore1.activities.HomeActivity;
import com.bsrdigicoin.estore1.activities.SignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText loginusername, loginpass;
    TextView takemetosignup;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(SharedPrefManager.getInstance(this).isLoogedIn()) {
            finish();
            startActivity(new Intent(this, HomeActivity.class));
            return;
        }

        loginusername = findViewById(R.id.loginusername);
        loginpass = findViewById(R.id.loginpass);
        takemetosignup = findViewById(R.id.takemetosignup);
        loginButton = findViewById(R.id.btnlogin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user_login();
            }
        });

        takemetosignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    public void user_login() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
        return;
        /*
        String username = loginusername.getText().toString().trim();
        String password = loginpass.getText().toString().trim();

        if(username.length() > 0 && password.length() > 0){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    //resetFields();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        if(!jsonObject.getBoolean("error")) {
                            SharedPrefManager.getInstance(getApplicationContext())
                                    .userLogin(
                                            jsonObject.getInt("id"),
                                            jsonObject.getString("username"),
                                            jsonObject.getString("email"));
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                            finish();
                        } else {
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
                    Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", username);
                    map.put("password", password);
                    return map;
                }
            };
            RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(getApplicationContext(), "Both the fields are required",
                    Toast.LENGTH_SHORT).show();
        }

         */
    }// function ends
}