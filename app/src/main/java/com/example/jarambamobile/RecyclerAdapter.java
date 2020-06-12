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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter {

    private static final String TAG = "RecyclerAdapter";
    ArrayList<DataHistory> moviesList;
    private Context context;

    public RecyclerAdapter(ArrayList<DataHistory> moviesList) {
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
        DataHistory isi = moviesList.get(position);
        if (isi.getStatus().contains("done")) {
            ViewHolderTwo viewHolderTwo = (ViewHolderTwo) holder;
            viewHolderTwo.textViewFrom2.setText(isi.getStart());
            viewHolderTwo.textViewTo2.setText(isi.getTo());

            viewHolderTwo.rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog = new Dialog(context);

                    dialog.setContentView(R.layout.history_rating);

                    Button Submit = dialog.findViewById(R.id.submit_rate);
                    final RatingBar Rating = dialog.findViewById(R.id.ratingBar);
                    final EditText Komentar = dialog.findViewById(R.id.comment_rate);
                    final TextView Harga = dialog.findViewById(R.id.harga_rate);
                    final TextView Pembayaran = dialog.findViewById(R.id.crbyr_rate);

                    Submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String rating = "Rating is : " + Rating.getRating() +"\nKomentar : " + Komentar.getText();

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("history_user_app");

                            myRef.setValue(new history_rating_data(Rating.getRating(), Komentar.getText().toString(), Harga.getText().toString(), Pembayaran.getText().toString()));

                            Toast.makeText(context, rating, Toast.LENGTH_LONG).show();

                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }
            });
        } else {
            final ViewHolderOne viewHolderOne = (ViewHolderOne) holder;
            viewHolderOne.textViewFrom1.setText(isi.getStart());
            viewHolderOne.TextViewTo1.setText(isi.getTo());

            viewHolderOne.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dialog dialog1 = new Dialog(context);

                    dialog1.setContentView(R.layout.history_detail);

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
                            Toast.makeText(context, "You have clicked " + menuItem.getTitle(), Toast.LENGTH_LONG).show();
                            return true;
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
        TextView textViewFrom1, TextViewTo1;
        ImageView info;
        TextView status;

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
        ImageView rating;

        public ViewHolderTwo(@NonNull View itemView) {
            super(itemView);
            textViewFrom2 = itemView.findViewById(R.id.dari2);
            textViewTo2 = itemView.findViewById(R.id.tujuan2);
            rating = itemView.findViewById(R.id.rate);

            context = itemView.getContext();
        }
    }
}