package mhandharbeni.com.trackmenuandroid.database.helper;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.database.model.TransaksiOrder;

/**
 * Created by root on 18/06/17.
 */

public class TransaksiOrderHelper {
    private static final String TAG = "MenuTableHelper";

    private Realm realm;
    private RealmResults<TransaksiOrder> realmResult;
    public Context context;

    public TransaksiOrderHelper(Context context) {
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

    public void addItemOrder(TransaksiOrder to) {
        TransaksiOrder ton= new TransaksiOrder();

        ton.setId(to.getId());
        ton.setId_order(to.getId_order());
        ton.setId_menu(to.getId_menu());
        ton.setJumlah(to.getJumlah());
        ton.setHarga(to.getHarga());
        ton.setTotal_harga(to.getTotal_harga());

        realm.beginTransaction();
        realm.copyToRealm(ton);
        realm.commitTransaction();
    }

    public void updateJumlah(int id, int jumlah) {
        TransaksiOrder mp = realm.where(TransaksiOrder.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        mp.setJumlah(jumlah);
        realm.commitTransaction();
    }

    public RealmResults<TransaksiOrder> getItemOrder(int idOrder) {
        realmResult = realm.where(TransaksiOrder.class).equalTo("id_order", idOrder).findAll();
        return realmResult;
    }
    public RealmResults<TransaksiOrder> getItemOrder() {
        realmResult = realm.where(TransaksiOrder.class).findAll();
        return realmResult;
    }
    public boolean checkDuplicate(int id_order, int id_menu) {
        realmResult = realm.where(TransaksiOrder.class)
                .equalTo("id_order", id_order)
                .equalTo("id_menu", id_menu)
                .findAll();
        if (realmResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
    public boolean checkDuplicate(int id) {
        realmResult = realm.where(TransaksiOrder.class)
                .equalTo("id", id)
                .findAll();
        if (realmResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}