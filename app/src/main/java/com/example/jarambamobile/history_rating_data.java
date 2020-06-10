package com.example.jarambamobile;

public class history_rating_data {
    private float rating;
    private String comment;
    private String harga;
    private String pembayaran;

    private String start;
    private String to;
    private String tanggal;
    private String jumlah_penumpang;

    public history_rating_data() {

    }

    public history_rating_data(float rating, String comment, String harga, String pembayaran) {
        this.rating = rating;
        this.comment = comment;
        this.harga = harga;
        this.pembayaran = pembayaran;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }
}
