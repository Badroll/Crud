package com.example.rpl2016_11.crud.users;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rpl2016_11.crud.R;
import com.example.rpl2016_11.crud.ViewActivity;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    EditText EditTextId, EditTextPassword;
    Button ButtonLogin;
    TextView TextViewSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditTextId = findViewById(R.id.EditTextId);
        EditTextPassword = findViewById(R.id.EditTextPassword);
        ButtonLogin = findViewById(R.id.ButtonLogin);
        TextViewSignUp = findViewById(R.id.TextViewSignUp);

        TextViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(Login.this, Register.class);
                startActivity(in);
            }
        });

        ButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    protected void onStart(){
        super.onStart();
        if (Preferences.getLoggedInStatus(getBaseContext())){
            startActivity(new Intent(getBaseContext(),ViewActivity.class));
            finish();
        }
    }

    private void login(){

        String url = "http://192.168.6.179/cud/loginn.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.trim().equals("succes")){
                    Preferences.setLoggedInUser(getBaseContext(),Preferences.getRegisteredUser(getBaseContext()));
                    Preferences.setLoggedInStatus(getBaseContext(),true);
                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(Login.this, ViewActivity.class);
                    startActivity(in);
                    finish();
                } if (response.trim().equals("error")){
                    Toast.makeText(getApplicationContext(), "Invalid Id or Password", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("id", EditTextId.getText().toString());
                params.put("pasword", EditTextPassword.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}




