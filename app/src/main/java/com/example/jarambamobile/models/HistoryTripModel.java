package com.example.jarambamobile.models;

public class HistoryTripModel {
    private Double harga;
    private String start,to, status, tanggal, comment, pembayaran, rate_status;
    private float rating;
    private Integer jumlah_penumpang;

    public HistoryTripModel(){}

    public Double getHarga() {
        return harga;
    }

    public void setHarga(Double harga) {
        this.harga = harga;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
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

    public String getPembayaran() {
        return pembayaran;
    }

    public void setPembayaran(String pembayaran) {
        this.pembayaran = pembayaran;
    }

    public Integer getJumlah_penumpang() {
        return jumlah_penumpang;
    }

    public void setJumlah_penumpang(Integer jumlah_penumpang) {
        this.jumlah_penumpang = jumlah_penumpang;
    }

    public String getRate_status() {
        return rate_status;
    }

    public void setRate_status(String rate_status) {
        this.rate_status = rate_status;
    }
}
