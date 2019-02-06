package com.example.rpl2016_11.crud;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.6.179/cud/";
    private RadioButton radioJKbutton;
    Spinner Spjurusan;
    String jurusan[] = {"Persiapan Grafika", "Produksi Grafika", "Desain Komunikasi Visual", "Rekayasa Perangkat Lunak", "Animasi"};
    ArrayAdapter<String> spadapter;
    private ProgressDialog progress;
    String nama, pasword, Jurusan;
    Button update, lihat;
    int id;

    // @BindView(R.id.id22) EditText textViewid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);

        final RadioGroup radioGroup = findViewById(R.id.radioJK);
        final RadioButton radioButtonLaki = findViewById(R.id.rdlaki);
        final RadioButton radioButtonPerempuan = findViewById(R.id.rdperempuan);
        final EditText textViewid = findViewById(R.id.id22);
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

        update = findViewById(R.id.buttonUpdate);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress = new ProgressDialog(UpdateActivity.this);
                progress.setCancelable(false);
                progress.setMessage("Loading...");
                progress.show();


                id = Integer.parseInt(textViewid.getText().toString());
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

                RegisterAPI api = retrofit.create(RegisterAPI.class);
                Call<Void> call = api.ubah(id, nama, Jurusan, jk, pasword);

                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        progress.dismiss();
                        Toast.makeText(UpdateActivity.this, "Berhasil memperbarui!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                        progress.dismiss();
                        Log.d("masgndn", "onFailure: " + t.getMessage());
                        Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        Intent intent = getIntent();
//        String id =
        id = Integer.parseInt(intent.getStringExtra("id"));
        Log.e("id", "onCreate: " + id );
        String nama = intent.getStringExtra("nama");
        String Jk = intent.getStringExtra("jk");
        String pasword = intent.getStringExtra("pasword");

        Log.d("sukei", "onCreate: "+ id);
        textViewid.setText(id + "");

        edittextnama.setText(nama);
        editPasword.setText(pasword);
        if (Jk.equals("Laki-laki")) {
            radioButtonLaki.setChecked(true);
        } else {
            radioButtonPerempuan.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Perhatian!");
                alertDialogBuilder
                        .setMessage("Anda akan menghapus data ini?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int IdNumber = id;
                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(URL)
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();

                                RegisterAPI api = retrofit.create(RegisterAPI.class);
                                Call<Void> call = api.hapus(IdNumber);
                                Log.e("cekidsalah", "onClick: " + IdNumber);
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Log.d("hasilnya", "onResponse: " + response.body());
                                        Toast.makeText(UpdateActivity.this, "sukses hapus", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        t.printStackTrace();
                                         progress.dismiss();
                                        Log.d("cancell", "onFailure: " + t.getMessage());
                                        Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
}

/* public class UpdateActivity extends AppCompatActivity {

    public static final String URL = "http://192.168.43.238/cud/";
    private RadioButton radioJKbutton;
    Spinner Spjurusan;
    String jurusan[] = {"Persiapan Grafika", "Produksi Grafika", "Desain Komunikasi Visual", "Rekayasa Perangkat Lunak", "Animasi"};
    ArrayAdapter<String> spadapter;
    private ProgressDialog progress;
    String id, nama ,pasword,Jurusan;

    @BindView(R.id.radioJK) RadioGroup radioGroup;
    @BindView(R.id.rdlaki) RadioButton radioButtonLaki;
    @BindView(R.id.rdperempuan) RadioButton radioButtonPerempuan;
    @BindView(R.id.id22) TextView editTextId;
    @BindView(R.id.nama2) EditText editTextNama;
    @BindView(R.id.password2) EditText editTextPasword;

    @OnClick(R.id.buttonUpdate) void ubah(){
        progress = new ProgressDialog(UpdateActivity.this);
        progress.setCancelable(false);
        progress.setMessage("Loading...");
        progress.show();

        id = editTextId.getText().toString();
        nama = editTextNama.getText().toString();
        pasword = editTextPasword.getText().toString();

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
        Call<Void> call = api.ubah(id,nama,Jurusan,jk,pasword);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
//                        String value =  response.body().getValue();
//                        String message = response.body().getMessage();
                progress.dismiss();
                Toast.makeText(UpdateActivity.this, "Berhasil memperbarui!", Toast.LENGTH_SHORT).show();
//                        if (value.equals("1")){
//                            Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
//                            finish();
//                        } else {
//                            Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
//                        }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
                progress.dismiss();
                Log.d("masgndn", "onFailure: " + t.getMessage());
                Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);

        Intent intent =  getIntent();
        String id = intent.getStringExtra("id");
        String nama = intent.getStringExtra("nama");
        // String jurusan = intent.getStringExtra("jurusan");
        String Jk = intent.getStringExtra("jk");
        String pasword = intent.getStringExtra("pasword");

        editTextId.setText(id);
        editTextNama.setText(nama);
        editTextPasword.setText(pasword);

        if (Jk.equals("Laki-laki")){
            radioButtonLaki.setChecked(true);
        } else {
            radioButtonPerempuan.setChecked(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Perhatian!");
                alertDialogBuilder
                        .setMessage("Anda akan menghapus data ini?")
                        .setCancelable(false)
                        .setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String IdNumber = editTextId.getText().toString();
                                Gson gson = new GsonBuilder()
                                        .setLenient()
                                        .create();
                                Retrofit retrofit = new Retrofit.Builder()
                                        .baseUrl(URL)
                                        .addConverterFactory(GsonConverterFactory.create(gson))
                                        .build();

                                RegisterAPI api =retrofit.create(RegisterAPI.class);
                                Call<Void> call = api.hapus(IdNumber);
                                Log.e("cekidsalah", "onClick: " + IdNumber );
                                call.enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Log.d("hasilnya", "onResponse: "+response.body());
                                        Toast.makeText(UpdateActivity.this, "sukses hapus", Toast.LENGTH_SHORT).show();
                                        //  String value =  response.body().getValue();
                                        //  String message = response.body().getMessage();
                                        //  progress.dismiss();
                                        //  if (value.equals("1")){
                                        //      Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
                                        //      finish();
                                        //   } else {
                                        //      Toast.makeText(UpdateActivity.this, message, Toast.LENGTH_SHORT).show();
                                        //  }
                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable t) {
                                        t.printStackTrace();
                                        // progress.dismiss();
                                        Log.d("cancell", "onFailure: " + t.getMessage());
                                        Toast.makeText(UpdateActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }
} */
