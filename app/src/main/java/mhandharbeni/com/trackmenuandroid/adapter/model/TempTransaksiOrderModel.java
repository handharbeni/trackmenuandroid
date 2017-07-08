package mhandharbeni.com.trackmenuandroid.adapter.model;

/**
 * Created by root on 20/06/17.
 */

public class TempTransaksiOrderModel {
    int id, id_menu, jumlah, harga, total_harga;

    public TempTransaksiOrderModel(int id, int id_menu, int jumlah, int harga, int total_harga) {
        this.id = id;
        this.id_menu = id_menu;
        this.jumlah = jumlah;
        this.harga = harga;
        this.total_harga = total_harga;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_menu() {
        return id_menu;
    }

    public void setId_menu(int id_menu) {
        this.id_menu = id_menu;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getTotal_harga() {
        return total_harga;
    }

    public void setTotal_harga(int total_harga) {
        this.total_harga = total_harga;
    }
}
