package mhandharbeni.com.trackmenuandroid.database.helper;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.database.model.MasterOrder;

/**
 * Created by root on 18/06/17.
 */

public class MasterOrderHelper {
    private static final String TAG = "MenuTableHelper";

    private Realm realm;
    private RealmResults<MasterOrder> realmResult;
    public Context context;

    public MasterOrderHelper(Context context) {
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

    public void addMasterOrder(MasterOrder mo) {
        MasterOrder mon = new MasterOrder();

        mon.setId(mo.getId());
        mon.setId_kurir(mo.getId_kurir());
        mon.setId_user(mo.getId_user());
        mon.setStatus(mo.getStatus());
        mon.setTanggal_waktu(mo.getTanggal_waktu());

        realm.beginTransaction();
        realm.copyToRealm(mon);
        realm.commitTransaction();
    }

    public void updateStatus(int id, int status) {
        MasterOrder mp = realm.where(MasterOrder.class).equalTo("id", id).findFirst();
        realm.beginTransaction();
        mp.setStatus(status);
        realm.commitTransaction();
    }

    public RealmResults<MasterOrder> getOrder(int id) {
        realmResult = realm.where(MasterOrder.class).equalTo("id", id).findAll();
        return realmResult;
    }
    public RealmResults<MasterOrder> getOrder() {
        realmResult = realm.where(MasterOrder.class).findAll();
        return realmResult;
    }

    public boolean checkDuplicate(int id) {
        realmResult = realm.where(MasterOrder.class)
                .equalTo("id", id)
                .findAll();
        if (realmResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}