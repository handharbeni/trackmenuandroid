package mhandharbeni.com.trackmenuandroid.database.model;

import io.realm.RealmObject;

/**
 * Created by root on 05/06/17.
 */

public class MasterOrder extends RealmObject{
    int id, id_user, id_kurir, status;
    String tanggal_waktu;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public int getId_kurir() {
        return id_kurir;
    }

    public void setId_kurir(int id_kurir) {
        this.id_kurir = id_kurir;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTanggal_waktu() {
        return tanggal_waktu;
    }

    public void setTanggal_waktu(String tanggal_waktu) {
        this.tanggal_waktu = tanggal_waktu;
    }
}
