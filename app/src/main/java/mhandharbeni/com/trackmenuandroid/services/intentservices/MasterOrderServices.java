package mhandharbeni.com.trackmenuandroid.services.intentservices;

import android.app.IntentService;
import android.content.Intent;
import android.os.StrictMode;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.database.helper.MenuTableHelper;
import mhandharbeni.com.trackmenuandroid.database.model.MenuTable;
import sexy.code.Callback;
import sexy.code.HttPizza;
import sexy.code.Request;
import sexy.code.Response;

/**
 * Created by root on 18/06/17.
 */

public class MasterOrderServices extends IntentService implements ConnectivityChangeListener {
    public String STAT = "stat", KEY = "key", NAMA="nama", EMAIL= "email", PICTURE = "gambar";
    HttPizza client;
    EncryptedPreferences encryptedPreferences;

    String endUri;

    MenuTableHelper mtHelper;

    public MasterOrderServices() {
        super("MASTERORDER SERVICE");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mtHelper = new MenuTableHelper(getApplicationContext());
        encryptedPreferences = new EncryptedPreferences.Builder(getBaseContext()).withEncryptionPassword(getString(R.string.KeyPassword)).build();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        client = new HttPizza();
        endUri = getString(R.string.server)+"/users/menu?token=";
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (encryptedPreferences.getString("NETWORK","0").equalsIgnoreCase("1")){
            syncData();
            sendBroadCast();
            stopSelf();
        }
    }
    public void syncData(){
        String token = encryptedPreferences.getString(KEY, "0");
        if(!token.equalsIgnoreCase("0")){
            String decryptToken = encryptedPreferences.getUtils().decryptStringValue(token);
            endUri += decryptToken;
            Request request = client.newRequest().url(endUri).get().build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        Boolean retur = jsonObject.getBoolean("return");
                        if (retur){
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            if (jsonArray.length() > 0){
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String id = object.getString("id");
                                    String nama = object.getString("nama");
                                    String gambar = object.getString("gambar");
                                    String harga = object.getString("harga");
                                    String kategori = object.getString("kategori");
                                    boolean duplicate = mtHelper.checkDuplicate(Integer.valueOf(id));
                                    if (!duplicate){
                                        /*insert*/
                                        MenuTable mtn = new MenuTable();
                                        mtn.setId(Integer.valueOf(id));
                                        mtn.setNama(nama);
                                        mtn.setHarga(harga);
                                        mtn.setGambar(gambar);
                                        mtn.setKategori(kategori);
                                        mtHelper.addMenu(mtn);
                                    }
                                }
                            }
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }

    }
    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        return true;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    public void sendBroadCast(){
        this.sendBroadcast(new Intent().setAction("UPDATE ORDER").putExtra("MODE", "UPDATE LIST"));
    }
    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if(event.getState().getValue() == ConnectivityState.CONNECTED){
            encryptedPreferences.edit()
                    .putString("NETWORK", "1")
                    .apply();
        }else{
            encryptedPreferences.edit()
                    .putString("NETWORK", "0")
                    .apply();
        }
    }
}
