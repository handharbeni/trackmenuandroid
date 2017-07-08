package mhandharbeni.com.trackmenuandroid.database.helper;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.database.model.TempTransaksiOrder;
import mhandharbeni.com.trackmenuandroid.database.model.TransaksiOrder;

/**
 * Created by root on 19/06/17.
 */

public class TempTransaksiOrderHelper {
    private static final String TAG = "MenuTableHelper";

    private Realm realm;
    private RealmResults<TempTransaksiOrder> realmResult;
    public Context context;

    public TempTransaksiOrderHelper(Context context) {
        this.context = context;
        Realm.init(context);
        try {
            realm = Realm.getDefaultInstance();
        } catch (Exception e) {
            RealmConfiguration config = new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()
                    .build();
            realm = Realm.getInstance(config);
        }
    }

    public void addItemOrder(TempTransaksiOrder to) {
        TempTransaksiOrder ton= new TempTransaksiOrder();

        ton.setId(to.getId());
        ton.setId_menu(to.getId_menu());
        ton.setJumlah(to.getJumlah());
        ton.setHarga(to.getHarga());
        ton.setTotal_harga(to.getTotal_harga());

        realm.beginTransaction();
        realm.copyToRealm(ton);
        realm.commitTransaction();
    }

    public void updateJumlah(int id, int jumlah, int harga) {
        TempTransaksiOrder mp = realm.where(TempTransaksiOrder.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        mp.setJumlah(jumlah);
        mp.setTotal_harga(harga);
        realm.commitTransaction();
    }
    public void updateJumlah(int id, int jumlah) {
        TempTransaksiOrder mp = realm.where(TempTransaksiOrder.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        mp.setJumlah(jumlah);
        realm.commitTransaction();
    }

    public RealmResults<TempTransaksiOrder> getItemOrder(int id) {
        realmResult = realm.where(TempTransaksiOrder.class).equalTo("id", id).findAll();
        return realmResult;
    }
    public RealmResults<TempTransaksiOrder> getItemOrder() {
        realmResult = realm.where(TempTransaksiOrder.class).findAll();
        return realmResult;
    }
    public boolean checkDuplicate(int id_order, int id_menu) {
        realmResult = realm.where(TempTransaksiOrder.class)
                .equalTo("id_order", id_order)
                .equalTo("id_menu", id_menu)
                .findAll();
        if (realmResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public void deleteItem(int id){
        realm.beginTransaction();
        RealmResults<TempTransaksiOrder> result = getItemOrder(id);
        result.deleteAllFromRealm();
        realm.commitTransaction();
    }
    public boolean checkDuplicate(int id) {
        realmResult = realm.where(TempTransaksiOrder.class)
                .equalTo("id", id)
                .findAll();
        if (realmResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}