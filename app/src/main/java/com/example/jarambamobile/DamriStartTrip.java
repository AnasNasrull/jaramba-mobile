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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DamriStartTrip extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    Dialog dialog;
    EditText etJumlahPenumpang;
    TextView tvTotalInputPenumpang, tvMetodePembayaran, tvAsalPengguna, tvTujuanPengguna, tvTanggal,tvHari, tvWaktu, tvTotalHarga;
    Button btnTambahkanPenumpang, btnDismissPenumpang, btnTambahMetodePembayaran, btnDismissMetode;
    Spinner spinner;
    String text;

    DatabaseReference reference;

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

        tvTanggal = findViewById(R.id.tanggal_kalender);
        tvHari = findViewById(R.id.hari_kata);
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String dayname = new SimpleDateFormat("EEEE").format(new Date());
        tvTanggal.setText(date);
        tvHari.setText(dayname);

        tvWaktu = findViewById(R.id.waktu);
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        String localTime = new SimpleDateFormat("HH:mm a").format(cal.getTime());
        tvWaktu.setText(localTime);

        tvTotalHarga = findViewById(R.id.sum_payment);

        reference = FirebaseDatabase.getInstance().getReference().child();

    }

    public String getHarga (int penumpang, double biaya ){
        Double total = penumpang * biaya;
        return (Double.toString(total));
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
                    tvTotalHarga.setText("Rp." + getHarga(Integer.parseInt(UserInput),3000)+",-");
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
