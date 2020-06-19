package com.example.jarambamobile;

import androidx.annotation.NonNull;
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
import com.example.jarambamobile.models.HistoryTripModel;
import com.example.jarambamobile.models.PointAddressModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.DatabaseMetaData;
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
    Button btnTambahkanPenumpang, btnDismissPenumpang, btnTambahMetodePembayaran, btnDismissMetode, btnGo;
    Spinner spinner;
    String text, startAddress, destinationAddress, metodePembayaran ="", hari, tanggal, waktu;
    Integer jumlahPenumpang = 0;
    Double totalHarga, startPointLat,startPointLong, destPointLat,destPointLong;

    HistoryTripModel history;
    PointAddressModel startLatLong, destinationLatLong;


    DatabaseReference database;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damri_start_trip);
        tvTotalInputPenumpang = findViewById(R.id.total_penumpang);
        tvMetodePembayaran = findViewById(R.id.cash_emoney);

        Intent intent = getIntent();

        startPointLat = Double.parseDouble(intent.getStringExtra("start_lati"));
        startPointLong = Double.parseDouble(intent.getStringExtra("start_long"));
        destPointLat = Double.parseDouble(intent.getStringExtra("destination_lati"));
        destPointLong = Double.parseDouble(intent.getStringExtra("destination_long"));

        startAddress= intent.getStringExtra("start_address");
        tvAsalPengguna = findViewById(R.id.asal_pengguna);
        tvAsalPengguna.setText(startAddress);

        destinationAddress= intent.getStringExtra("destination_address");
        tvTujuanPengguna = findViewById(R.id.asal_pengguna_to);
        tvTujuanPengguna.setText(destinationAddress);

        setTanggal();
        tvTanggal = findViewById(R.id.tanggal_kalender);
        tvHari = findViewById(R.id.hari_kata);
        tvTanggal.setText(tanggal);
        tvHari.setText(hari);

        setWaktu();
        tvWaktu = findViewById(R.id.waktu);
        tvWaktu.setText(waktu);

        tvTotalHarga = findViewById(R.id.sum_payment);
        database = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        history = new HistoryTripModel();
        startLatLong = new PointAddressModel();
        destinationLatLong = new PointAddressModel();
        btnGo = findViewById(R.id.btn_go);
        final String uid = "sqNxENZFQAga0Qq9MlEyI4aCxQh2";
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(metodePembayaran==""||jumlahPenumpang==0){
                    Toast.makeText(getApplicationContext(), "Silahkan Lengkapi data !", Toast.LENGTH_SHORT).show();
                }else{
                    history.setComment(" ");
                    history.setHarga(totalHarga);
                    history.setRating(" ");
                    history.setJumlah_penumpang(jumlahPenumpang);
                    history.setPembayaran(metodePembayaran);
                    history.setTanggal(tanggal);
                    history.setStart(startAddress);
                    history.setTo(destinationAddress);
                    history.setStatus("Pending");
                    database.child("Mobile_Apps").child("User").child(uid).child("History_Trip_User").child("Trip_User").setValue(history).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Data Berhasil Disimpan !", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Data Gagal Disimpan, silahkan ulangi kembali!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    startLatLong.setLatitude(startPointLat);
                    startLatLong.setLongitude(startPointLong);
                    destinationLatLong.setLatitude(destPointLat);
                    destinationLatLong.setLongitude(destPointLong);
                    database.child("Mobile_Apps").child("User").child("LongLat").push().setValue(startPointLong);
                    database.child("Mobile_Apps").child("User").child("LongLat").push().setValue(destPointLong);

                    //intent to history menu
                    startActivity(new Intent(DamriStartTrip.this, History.class));
                    finish();
                }
            }
        });

    }

    public void setWaktu(){
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+7:00"));
        this.waktu = new SimpleDateFormat("HH:mm a").format(cal.getTime());
    }

    public void setTanggal(){
        this.tanggal = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        this.hari = new SimpleDateFormat("EEEE").format(new Date());
    }

    public String getHarga (int penumpang, double biaya ){
        Double total = penumpang * biaya;
        return (Double.toString(total));
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
                    jumlahPenumpang = Integer.parseInt(UserInput);
                    totalHarga = Double.parseDouble(getHarga(Integer.parseInt(UserInput),3000));
                    tvTotalInputPenumpang.setText(UserInput + " Orang");
                    tvTotalHarga.setText("Rp." + totalHarga.toString() +",-");
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
