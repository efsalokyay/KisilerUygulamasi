package com.efsalokyay.kisileruygulamasi;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    private ArrayList<Kisiler> kisilerArrayList;
    private KisilerAdapter adapter;

    private Veritabani vt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.recycler_view);
        fab = findViewById(R.id.fab);

        vt = new Veritabani(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        toolbar.setTitle("Kişiler");
        setSupportActionBar(toolbar);

        kisilerArrayList = new KisilerDao().tumKisiler(vt);

        adapter = new KisilerAdapter(this, kisilerArrayList, vt);
        recyclerView.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertGoster();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.toolbar_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_ara);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        Log.e("onQueryTextSubmit", s);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        Log.e("onQueryTextChange", s);

        kisilerArrayList = new KisilerDao().kisiAra(vt, s);
        adapter = new KisilerAdapter(MainActivity.this, kisilerArrayList, vt);
        recyclerView.setAdapter(adapter);

        return false;
    }

    public void alertGoster() {

        LayoutInflater layout = LayoutInflater.from(this);
        View tasarim = layout.inflate(R.layout.alert_tasarim, null);

        final EditText ad_text = tasarim.findViewById(R.id.kisi_ad_text);
        final EditText tel_text = tasarim.findViewById(R.id.kisi_tel_text);

        final AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle("Kişi Ekle");
        ad.setView(tasarim);
        ad.setPositiveButton("Ekle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String kisi_ad = ad_text.getText().toString().trim();
                String kisi_tel = tel_text.getText().toString().trim();

                new KisilerDao().kisiEkle(vt, kisi_ad, kisi_tel);

                kisilerArrayList = new KisilerDao().tumKisiler(vt);
                adapter = new KisilerAdapter(MainActivity.this, kisilerArrayList, vt);
                recyclerView.setAdapter(adapter);

                Toast.makeText(MainActivity.this, kisi_ad + " - " + kisi_tel, Toast.LENGTH_SHORT).show();

            }
        });
        ad.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        ad.create().show();

    }
}
