package com.example.rpl2016_11.crud;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;

import com.example.rpl2016_11.crud.API.RegisterAPI;
import com.example.rpl2016_11.crud.model.Siswa;
import com.example.rpl2016_11.crud.model.Value;
import com.example.rpl2016_11.crud.users.Login;
import com.example.rpl2016_11.crud.users.Preferences;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ViewActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, android.support.v7.widget.SearchView.OnQueryTextListener {

    public static final String URL = "http://192.168.6.179/cud/";
    private List<Siswa> siswa = new ArrayList<>();
    private Adapter viewAdapter;
    RecyclerView recyclerView;
    ProgressBar progres;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recView);
        progres = findViewById(R.id.progress);

        viewAdapter = new Adapter(this, siswa);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(viewAdapter);

        loadData();

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Preferences.clearLoggedInUser(getBaseContext());
                startActivity(new Intent(getBaseContext(),Login.class));
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.view();

        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value = response.body().getValue();
                progres.setVisibility(GONE);
                if (value.equals("1")) {
                    siswa = response.body().getResult();
                    viewAdapter = new Adapter(ViewActivity.this, siswa);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        final MenuItem item = menu.findItem(R.id.action_search);
        final android.support.v7.widget.SearchView search = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(item);
        search.setQueryHint("Cari");
        search.setIconified(false);
        search.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newtext) {
        recyclerView.setVisibility(GONE);
        progres.setVisibility(VISIBLE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RegisterAPI api = retrofit.create(RegisterAPI.class);
        Call<Value> call = api.search(newtext);
        call.enqueue(new Callback<Value>() {
            @Override
            public void onResponse(Call<Value> call, Response<Value> response) {
                String value =  response.body().getValue();
                progres.setVisibility(GONE);
                recyclerView.setVisibility(VISIBLE);
                if (value.equals("1")){
                    siswa = response.body().getResult();
                    viewAdapter = new Adapter(ViewActivity.this,siswa);
                    recyclerView.setAdapter(viewAdapter);
                }
            }

            @Override
            public void onFailure(Call<Value> call, Throwable t) {
                progres.setVisibility(GONE);
            }
        });

        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
