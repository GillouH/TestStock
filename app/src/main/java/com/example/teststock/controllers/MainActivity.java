package com.example.teststock.controllers;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.teststock.R;
import com.example.teststock.adapters.ItemAdapter;
import com.example.teststock.models.BasicItem;
import com.example.teststock.models.Item;
import com.example.teststock.models.PackItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private final List<Item> listItem = new ArrayList<>();
    private ItemAdapter itemAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        // Activity initialization.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.activityMain_toolbar);
        setSupportActionBar(toolbar);

        // Item list initialization.
        listItem.add(new BasicItem("Gel WC", 4, "bouteille(s)"));
        listItem.add(new BasicItem("Lingettes bébé", 2, "boîte(s)"));
        listItem.add(new BasicItem("Lingettes desinfectante", 2, "boîte(s)"));
        listItem.add(new BasicItem("Mouchoirs", 5, "boîte(s)"));
        listItem.add(new BasicItem("Gants", 8, "boîte(s)"));
        listItem.add(new PackItem("Papier toilette", 3, "rouleau(x)", 1, "packet(s)", 8));
        listItem.add(new PackItem("Essuie main", 2, "rouleau(x)", 3, "packet(s)", 6));
        listItem.add(new BasicItem("Sac poubelle (petit noir)", 5, "rouleau(x)"));
        listItem.add(new BasicItem("Sac poubelle (grand noir)", 3, "rouleau(x)"));
        listItem.add(new BasicItem("Sac poubelle (blanc)", 1, "rouleau(x)"));
        itemAdapter = new ItemAdapter(listItem);

        // RecyclerView initialization.
        recyclerView = findViewById(R.id.activityMain_recyclerView_listItem);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(itemAdapter);
    }
}