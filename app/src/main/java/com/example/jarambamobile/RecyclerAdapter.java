package com.example.jarambamobile;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecyclerAdapter extends RecyclerView.Adapter {

    ArrayList<getAllHistory> moviesList;
    private Context context;
    FirebaseAuth firebaseAuth;

    public RecyclerAdapter(ArrayList<getAllHistory> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public int getItemViewType(int position) {
        if (moviesList.get(position).getStatus().contains("done")) {
            return 1;
        } else {
            return 0;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view;

        if (viewType == 1) {
            view = layoutInflater.inflate(R.layout.view_list_item_done, parent, false);
            return new ViewHolderTwo(view);
        } else {
            view = layoutInflater.inflate(R.layout.view_list_item_status, parent, false);
            return new ViewHolderOne(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final getAllHistory isi = moviesList.get(position);
        if (isi.getStatus().contains("done")) {
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;

            String mulai = isi.getStart();
            if(mulai.length()>17) {
                viewHolderTwo.textViewFrom2.setText(ShortChar(mulai));
            } else {
                viewHolderTwo.textViewFrom2.setText(isi.getStart());
            }

            String sampai = isi.getTo();
            if(sampai.length()>17) {
                viewHolderTwo.textViewTo2.setText(ShortChar(sampai));
            } else {
                viewHolderTwo.textViewTo2.setText(isi.getTo());
            }

            if (isi.getRate_status().contains("not")) {
                viewHolderTwo.rating.setEnabled(true);
                viewHolderTwo.rating.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.history_rating);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        Button Submit = dialog.findViewById(R.id.submit_rate);
                        final RatingBar Rating = dialog.findViewById(R.id.ratingBar);
                        final EditText Komentar = dialog.findViewById(R.id.comment_rate);
                        final TextView Harga = dialog.findViewById(R.id.harga_rate);
                        final TextView Pembayaran = dialog.findViewById(R.id.crbyr_rate);

                        Harga.setText(isi.getHarga().toString());
                        Pembayaran.setText(isi.getPembayaran());

                        Submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference();
                                firebaseAuth = FirebaseAuth.getInstance();
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                String uid = user.getUid();

                                myRef.child("Mobile_Apps").child("User").child(uid).child("History_Trip_User").child(isi.getKey()).setValue(new getAllHistory(Rating.getRating(), Komentar.getText().toString(), isi.getHarga(), isi.getPembayaran(), isi.getStart(), isi.getTo(), isi.getTanggal(), isi.getJumlah_penumpang(), isi.getStatus(), "done"));
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
            }

            viewHolderTwo.info2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog2 = new Dialog(context);
                    dialog2.setContentView(R.layout.history_detail);
                    dialog2.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    final TextView start = dialog2.findViewById(R.id.start_data);
                    final TextView to = dialog2.findViewById(R.id.to_data);
                    final TextView tanggal = dialog2.findViewById(R.id.tgl_data_det);
                    final TextView jumlah = dialog2.findViewById(R.id.jml_data);
                    final TextView harga = dialog2.findViewById(R.id.harga_det);
                    final TextView pembayaran = dialog2.findViewById(R.id.crbyr_det);

                    start.setText(isi.getStart());
                    to.setText(isi.getTo());
                    tanggal.setText(isi.getTanggal());
                    jumlah.setText(isi.getJumlah_penumpang().toString());
                    harga.setText(isi.getHarga().toString());
                    pembayaran.setText(isi.getPembayaran());

                    dialog2.show();
                }
            });
        } else {
            final ViewHolderOne viewHolderOne = (ViewHolderOne) holder;

            String mulai = isi.getStart();
            if(mulai.length()>20) {
                viewHolderOne.textViewFrom1.setText(ShortChar(mulai));
            } else {
                viewHolderOne.textViewFrom1.setText(isi.getStart());
            }

            String sampai = isi.getTo();
            if(sampai.length()>20) {
                viewHolderOne.TextViewTo1.setText(ShortChar(sampai));
            } else {
                viewHolderOne.TextViewTo1.setText(isi.getTo());
            }

            viewHolderOne.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog1 = new Dialog(context);
                    dialog1.setContentView(R.layout.history_detail);
                    dialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                    final TextView start = dialog1.findViewById(R.id.start_data);
                    final TextView to = dialog1.findViewById(R.id.to_data);
                    final TextView tanggal = dialog1.findViewById(R.id.tgl_data_det);
                    final TextView jumlah = dialog1.findViewById(R.id.jml_data);
                    final TextView harga = dialog1.findViewById(R.id.harga_det);
                    final TextView pembayaran = dialog1.findViewById(R.id.crbyr_det);

                    start.setText(isi.getStart());
                    to.setText(isi.getTo());
                    tanggal.setText(isi.getTanggal());
                    jumlah.setText(isi.getJumlah_penumpang().toString());
                    harga.setText(isi.getHarga().toString());
                    pembayaran.setText(isi.getPembayaran());

                    dialog1.show();
                }
            });

            viewHolderOne.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu dropDownMenu = new PopupMenu(context, viewHolderOne.status);
                    dropDownMenu.getMenuInflater().inflate(R.menu.list_status, dropDownMenu.getMenu());
                    dropDownMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference();
                            firebaseAuth = FirebaseAuth.getInstance();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String uid = user.getUid();

                            Date tanggal = new Date();

                            SimpleDateFormat format = new SimpleDateFormat("H");
                            String tgl = isi.getTanggal();
                            String jam = format.format(tanggal);
                            SimpleDateFormat format2 = new SimpleDateFormat("m");
                            String menit = format2.format(tanggal);
                            SimpleDateFormat format3 = new SimpleDateFormat("s");
                            String detik = format3.format(tanggal);
                            char[] dd = {tgl.charAt(0), tgl.charAt(1)};
                            char[] mm = {tgl.charAt(3), tgl.charAt(4)};
                            char[] yyyy = {tgl.charAt(6), tgl.charAt(7), tgl.charAt(8), tgl.charAt(9)};
                            String day = new String(dd);
                            String month = new String(mm);
                            String year = new String(yyyy);
                            String key = year + month + day + jam + menit + detik;

                            if (menuItem.getTitle().equals("Done")) {
                                myRef.child("Mobile_Apps").child("User").child(uid).child("History_Trip_User").child(key).setValue(new getAllHistory(isi.getRating(), isi.getComment(), isi.getHarga(), isi.getPembayaran(), isi.getStart(), isi.getTo(), isi.getTanggal(), isi.getJumlah_penumpang(), "done", isi.getRate_status()));
                                myRef.child("Mobile_Apps").child("User").child(uid).child("History_Trip_User").child(isi.getKey()).removeValue();
                            } else if (menuItem.getTitle().equals("Cancel")) {
                                myRef.child("Mobile_Apps").child("User").child(uid).child("History_Trip_User").child(isi.getKey()).removeValue();
                            }
                            return false;
                        }
                    });
                    dropDownMenu.show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        TextView textViewFrom1, TextViewTo1, status;
        ImageView info;

        public ViewHolderOne(@NonNull View itemView) {
            super(itemView);
            textViewFrom1 = itemView.findViewById(R.id.dari1);
            TextViewTo1 = itemView.findViewById(R.id.tujuan1);
            info = itemView.findViewById(R.id.iconinfo1);
            status = itemView.findViewById(R.id.status_list);

            context = itemView.getContext();
        }
    }

    class ViewHolderTwo extends RecyclerView.ViewHolder {
        TextView textViewFrom2, textViewTo2;
        ImageView rating, info2;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            textViewFrom2 = itemView.findViewById(R.id.dari2);
            textViewTo2 = itemView.findViewById(R.id.tujuan2);
            rating = itemView.findViewById(R.id.rate);
            info2 = itemView.findViewById(R.id.iconinfo2);

            context = itemView.getContext();
        }
    }

    public String ShortChar(String address) {
        String cut;
        char[] cutChar = new char[23];

        for (int i=0; i<23; i++) {
            if (i<20) {
                cutChar[i] = address.charAt(i);
            } else {
                cutChar[i] = '.';
            }
        }
        cut = new String(cutChar);
        return cut;
    }
}