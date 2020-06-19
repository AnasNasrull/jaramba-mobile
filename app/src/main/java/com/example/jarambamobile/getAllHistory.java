package com.example.jarambamobile;

public class getAllHistory {
    private float rating;
    private String comment;
    private String harga;
    private String pembayaran;
    private String start;
    private String to;
    private String tanggal;
    private String jumlah_penumpang;
    private String status;
    private String key;
    private String rate_status;

    public getAllHistory() {

    }

    public getAllHistory(float rating, String comment, String harga, String pembayaran, String start, String to, String tanggal, String jumlah_penumpang, String status, String rate_status) {
        this.rating = rating;
        this.comment = comment;
        this.harga = harga;
        this.pembayaran = pembayaran;
        this.start = start;
        this.to = to;
        this.tanggal = tanggal;
        this.jumlah_penumpang = jumlah_penumpang;
        this.status = status;
        this.rate_status = rate_status;
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

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getJumlah_penumpang() {
        return jumlah_penumpang;
    }

    public void setJumlah_penumpang(String jumlah_penumpang) {
        this.jumlah_penumpang = jumlah_penumpang;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getRate_status() {
        return rate_status;
    }

    public void setRate_status(String rate_status) {
        this.rate_status = rate_status;
    }
}
