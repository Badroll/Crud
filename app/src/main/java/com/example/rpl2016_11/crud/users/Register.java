package com.example.rpl2016_11.crud.users;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.rpl2016_11.crud.API.RegisterAPI;
import com.example.rpl2016_11.crud.R;
import com.example.rpl2016_11.crud.ViewActivity;
import com.example.rpl2016_11.crud.model.Value;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Register extends AppCompatActivity {

    public static final String URL = "http://192.168.6.179/cud/insert.php/";
    private RadioButton radioJKbutton;
    Spinner Spjurusan;
    String jurusan[] = {"Persiapan Grafika", "Produksi Grafika", "Desain Komunikasi Visual", "Rekayasa Perangkat Lunak", "Animasi"};
    ArrayAdapter<String> spadapter;
    private ProgressDialog progress;
    String id, nama, pasword, Jurusan;
    Button daftar, lihat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final RadioGroup radioGroup = findViewById(R.id.radioJK);
        final EditText editTextid = findViewById(R.id.id2);
        final EditText edittextnama = findViewById(R.id.nama2);
        final EditText editPasword = findViewById(R.id.password2);
        Spjurusan = findViewById(R.id.spinner);
        spadapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, jurusan);
        Spjurusan.setAdapter(spadapter);
        Spjurusan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Jurusan = jurusan[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
    });

        lihat = findViewById(R.id.lihat);
        lihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, ViewActivity.class);
                startActivity(i);
            }
        });

    daftar = findViewById(R.id.button2);

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                progress = new ProgressDialog(Register.this);
                progress.setCancelable(false);
                progress.setMessage("Loading...");
                progress.show();

                id = editTextid.getText().toString();
                nama = edittextnama.getText().toString();
                pasword = editPasword.getText().toString();

                int selected = radioGroup.getCheckedRadioButtonId();
                radioJKbutton = findViewById(selected);
                String jk = radioJKbutton.getText().toString();


                Gson gson = new GsonBuilder()
                        .setLenient()
                        .create();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build();

                RegisterAPI api =retrofit.create(RegisterAPI.class);
                Call<Value> call = api.daftar(id,nama,Jurusan,jk, pasword);

                call.enqueue(new Callback<Value>() {
                    @Override
                    public void onResponse(Call<Value> call, Response<Value> response) {
                        String value =  response.body().getValue();
                        String message = response.body().getMessage();
                        progress.dismiss();
                        if (value.equals("1")){
                            Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Register.this, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Value> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        Log.d("gndhnnn", "onFailure: " + t.getMessage());
                        Toast.makeText(Register.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


}

