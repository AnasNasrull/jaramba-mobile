package com.example.jarambamobile;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class History extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerAdapter recyclerAdapter;

    private ArrayList<DataHistory> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_history);

        DataHistory data = new DataHistory();
        data.setTo("Jl. Terusan");
        data.setStart("Mall Kedaton ");
        data.setStatus("");
        list.add(data);

        data = new DataHistory();
        data.setTo("Jl. Ryacudu");
        data.setStart("kosan");
        data.setStatus("");
        list.add(data);

        data = new DataHistory();
        data.setTo("Jl. nyasar");
        data.setStart("mana ajalah");
        data.setStatus("");
        list.add(data);

        data = new DataHistory();
        data.setTo("Jl. apalah");
        data.setStart("rumah");
        data.setStatus("done");
        list.add(data);

        data = new DataHistory();
        data.setTo("Jl. Terusan Ryacudu ");
        data.setStart("Bumi Kedaton ");
        data.setStatus("done");
        list.add(data);

        recyclerView = findViewById(R.id.rv_dtlist);
        recyclerAdapter = new RecyclerAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(recyclerAdapter);
    }
}