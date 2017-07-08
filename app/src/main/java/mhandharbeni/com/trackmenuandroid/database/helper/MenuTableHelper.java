package mhandharbeni.com.trackmenuandroid.database.helper;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.database.model.MenuTable;

/**
 * Created by root on 16/06/17.
 */

public class MenuTableHelper {
    private static final String TAG = "MenuTableHelper";

    private Realm realm;
    private RealmResults<MenuTable> realmResult;
    public Context context;

    public MenuTableHelper(Context context) {
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

    public void addMenu(MenuTable mt) {
        MenuTable mtN = new MenuTable();

        mtN.setId(mt.getId());
        mtN.setGambar(mt.getGambar());
        mtN.setHarga(mt.getHarga());
        mtN.setKategori(mt.getKategori());
        mtN.setNama(mt.getNama());

        realm.beginTransaction();
        realm.copyToRealm(mtN);
        realm.commitTransaction();
    }

    public void updateStatus(int id, int status) {
        realm.beginTransaction();
        MenuTable mp = realm.where(MenuTable.class).equalTo("id", id).findFirst();
//        mp.setStatus(status);
        realm.copyToRealmOrUpdate(mp);
        realm.commitTransaction();
    }
    public String getAttribut(int id, String attribute){
        String sResult = "nothing";
        switch (attribute){
            case "name" :
                sResult = realm.where(MenuTable.class).equalTo("id", id).findAll().get(0).getNama();
                break;
            case "harga" :
                sResult = realm.where(MenuTable.class).equalTo("id", id).findAll().get(0).getHarga();
                break;
            case "deskripsi" :
                sResult = realm.where(MenuTable.class).equalTo("id", id).findAll().get(0).getNama();
                break;
            case "kategori" :
                sResult = realm.where(MenuTable.class).equalTo("id", id).findAll().get(0).getKategori();
                break;
            case "gambar" :
                sResult = realm.where(MenuTable.class).equalTo("id", id).findAll().get(0).getGambar();
                break;
        }
        return sResult;
    }
    public RealmResults<MenuTable> getMenu(int id) {
        realmResult = realm.where(MenuTable.class).equalTo("id", id).findAll();
        return realmResult;
    }
    public RealmResults<MenuTable> getMenu(String kategori) {
        realmResult = realm.where(MenuTable.class).equalTo("kategori", kategori).findAll();
        return realmResult;
    }
    public RealmResults<MenuTable> getMenu() {
        realmResult = realm.where(MenuTable.class).findAll();
        return realmResult;
    }

    public boolean checkDuplicate(int id) {
        realmResult = realm.where(MenuTable.class)
                .equalTo("id", id)
                .findAll();
        if (realmResult.size() > 0) {
            return true;
        } else {
            return false;
        }
    }
}