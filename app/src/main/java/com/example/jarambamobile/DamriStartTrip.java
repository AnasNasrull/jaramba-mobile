package com.example.jarambamobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jarambamobile.fragment.DatePickerFragment;

import java.text.DateFormat;
import java.util.Calendar;

public class DamriStartTrip extends AppCompatActivity implements AdapterView.OnItemSelectedListener, DatePickerDialog.OnDateSetListener {


    Dialog dialog;
    EditText etJumlahPenumpang;
    TextView tvTotalInputPenumpang, tvMetodePembayaran, tvAsalPengguna, tvTujuanPengguna, tvTanggal;
    Button btnTambahkanPenumpang, btnDismissPenumpang, btnTambahMetodePembayaran, btnDismissMetode;
    Spinner spinner;
    String text;

    RelativeLayout tanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damri_start_trip);
        tvTotalInputPenumpang = findViewById(R.id.total_penumpang);
        tvMetodePembayaran = findViewById(R.id.cash_emoney);

        Intent intent = getIntent();

        String start_point= intent.getStringExtra("start_point");
        tvAsalPengguna = findViewById(R.id.asal_pengguna);
        tvAsalPengguna.setText(start_point);

        String destination_point= intent.getStringExtra("destination_point");
        tvTujuanPengguna = findViewById(R.id.asal_pengguna_to);
        tvTujuanPengguna.setText(destination_point);

        tanggal = findViewById(R.id.rel_tanggal);
        tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
        tvTanggal = findViewById(R.id.tanggal_kalender);
    }



    public void letsGo(View view) {
        Toast.makeText(getApplicationContext(), "Let's Go Trip With Jaramba Apps", Toast.LENGTH_SHORT).show();
    }

    public void addMetodePembayaran(View view) {
        dialog = new Dialog(this);
        showAddMetodePembayaranDialog();
    }

    private void showAddMetodePembayaranDialog() {
        dialog.setContentView(R.layout.popup_jenis_pembayaran);
        spinner = dialog.findViewById(R.id.spinner1);
        btnTambahMetodePembayaran = dialog.findViewById(R.id.btn_tambah_metode);
        btnDismissMetode = dialog.findViewById(R.id.btn_dismiss_metode);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.metode_pembayaran, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        btnDismissMetode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnTambahMetodePembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    tvMetodePembayaran.setText(text);
                    dialog.dismiss();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

    }

    public void addJumlahPenumpang(View view) {
        dialog = new Dialog(this);
        showAddJumlahPenumpangDialog();
    }

    private void showAddJumlahPenumpangDialog() {
        dialog.setContentView(R.layout.popup_jumlah_penumpang);
        etJumlahPenumpang = dialog.findViewById(R.id.et_jumlah_penumpang);
        btnTambahkanPenumpang = dialog.findViewById(R.id.btn_tambah_penumpang);
        btnDismissPenumpang = dialog.findViewById(R.id.btn_dismiss_penumpang);

        btnDismissPenumpang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        btnTambahkanPenumpang.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String UserInput = etJumlahPenumpang.getText().toString().trim();

                if(!UserInput.isEmpty()){
                    tvTotalInputPenumpang.setText(UserInput + " Orang");
                } else {
                    Toast.makeText(getApplicationContext(), "Mohon isikan data dengan benar", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        text =  parent.getItemAtPosition(position).toString().trim();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
