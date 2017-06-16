package mhandharbeni.com.trackmenuandroid.adapter.model;

/**
 * Created by root on 16/06/17.
 */

public class MenuModel {
    int id;
    String nama, gambar, harga, kategori;

    public MenuModel(int id, String nama, String gambar, String harga, String kategori) {
        this.id = id;
        this.nama = nama;
        this.gambar = gambar;
        this.harga = harga;
        this.kategori = kategori;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
}
