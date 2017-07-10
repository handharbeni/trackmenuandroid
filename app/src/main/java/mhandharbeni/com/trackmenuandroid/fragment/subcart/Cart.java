package mhandharbeni.com.trackmenuandroid.fragment.subcart;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;
import com.schibstedspain.leku.LocationPickerActivity;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.MainActivity;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.adapter.adapter.TempTransaksiOrderAdapter;
import mhandharbeni.com.trackmenuandroid.adapter.decoration.DividerItemDecoration;
import mhandharbeni.com.trackmenuandroid.adapter.model.TempTransaksiOrderModel;
import mhandharbeni.com.trackmenuandroid.database.helper.TempTransaksiOrderHelper;
import mhandharbeni.com.trackmenuandroid.database.model.TempTransaksiOrder;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentHome;
import mhandharbeni.com.trackmenuandroid.fragment.submap.MapsFragment;
import mhandharbeni.com.trackmenuandroid.fragment.submenu.DetailMenu;
import mhandharbeni.com.trackmenuandroid.util.CartUtil;
import sexy.code.Callback;
import sexy.code.FormBody;
import sexy.code.HttPizza;
import sexy.code.Request;
import sexy.code.RequestBody;
import sexy.code.Response;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by root on 20/06/17.
 */

public class Cart extends Fragment  implements CartUtil, ConnectivityChangeListener {

    public String STAT = "stat", KEY = "key", NAMA="nama", EMAIL= "email", PICTURE = "gambar";
    private String ADDRESS = "ADDRESS", LATITUDE = "LATITUDE", LONGITUDE = "LONGITUDE", PREADDRESS = "PREADDRESS", PRELATITUDE = "PRELATITUDE", PRELONGITUDE = "PRELONGITUDE", DISTANCE = "DISTANCE", PREPOSTALCODE = "PREPOSTALCODE";
    private String ENDURIORDER = "";

    static TempTransaksiOrderHelper tempTransaksiOrderHelper;
    static View v;
    private Geocoder geocoder;
    EncryptedPreferences encryptedPreferences;
    ConnectionBuddyConfiguration networkInspectorConfiguration;
    HttPizza client;
    RecyclerView listCart;
    TextView txtAddCart, txtchangeDestination, txtaddress, txtdistance;
    List<TempTransaksiOrderModel> cartList;
    ImageView backsButton;
    Button btnOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tempTransaksiOrderHelper = new TempTransaksiOrderHelper(getActivity().getApplicationContext());
        encryptedPreferences = new EncryptedPreferences.Builder(getActivity().getApplicationContext()).withEncryptionPassword(getString(R.string.KeyPassword)).build();
        networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder(getActivity().getApplicationContext()).build();
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);
        client = new HttPizza();
        ENDURIORDER = getString(R.string.server)+"/users/order";
        encryptedPreferences.edit().putString(ADDRESS, "Burger Tahu Malang Suhat 2").apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.list_cart, container, false);
        txtaddress = (TextView) v.findViewById(R.id.address);
        txtdistance = (TextView) v.findViewById(R.id.distance);
        txtchangeDestination = (TextView) v.findViewById(R.id.changeDestination);
        txtchangeDestination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDestination();
                ((MainActivity)getActivity()).changeTitle("Lokasi");
            }
        });
        txtAddCart = (TextView) v.findViewById(R.id.addMore);
        txtAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragmentMenu = new DetailMenu();
                Bundle bundle = new Bundle();
                bundle.putString("kategori", "Makanan");
                fragmentMenu.setArguments(bundle);
                ((MainActivity)getActivity()).changeTitle("Makanan");
                ((MainActivity)getActivity()).goBack(fragmentMenu);
            }
        });
        btnOrder = (Button) v.findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postOrder();
            }
        });
        initBack();
        initData();
        initAdapter();
        initLayoutManager();
        updateDestination();
        updateInfoBottom();
        updateInfo();
        return v;
    }

    public void initBack() {
        ((MainActivity) getActivity()).showBackButton();
        backsButton = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.imageBack);
        backsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).setTitleDefault();
                ((MainActivity) getActivity()).goBack(new FragmentHome());
                ((MainActivity) getActivity()).hideBackButton();
            }
        });

    }

    public void initData() {
        cartList = new ArrayList<>();
        RealmResults<TempTransaksiOrder> results = tempTransaksiOrderHelper.getItemOrder();
        if (results.size() > 0) {
            for (int i = 0; i < results.size(); i++) {
                cartList.add(
                        new TempTransaksiOrderModel(
                                results.get(i).getId(),
                                results.get(i).getId_menu(),
                                results.get(i).getJumlah(),
                                results.get(i).getHarga(),
                                results.get(i).getTotal_harga()
                        )
                );
            }
        }
    }

    public void initAdapter() {
        listCart = (RecyclerView) v.findViewById(R.id.listCart);
        TempTransaksiOrderAdapter ma = new TempTransaksiOrderAdapter(getActivity().getApplicationContext(), cartList, this);
        listCart.setAdapter(ma);
        listCart.setNestedScrollingEnabled(false);
    }

    public void initLayoutManager() {
        int decorPriorityIndex = 0;
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.divider_menu);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listCart.addItemDecoration(dividerItemDecoration, decorPriorityIndex);
        listCart.setLayoutManager(llm);
    }

    public static void updateInfoBottom(){
        RealmResults<TempTransaksiOrder> results = tempTransaksiOrderHelper.getItemOrder();
        int total_harga = 0;
        if (results.size() > 0){
            for (int i=0;i<results.size();i++){
                int total = 0;
                total = results.get(i).getJumlah() * results.get(i).getHarga();
                total_harga += total;
            }
        }
        TextView totalSemua = (TextView) v.findViewById(R.id.totalSemua);
        totalSemua.setText(numberFormat(Double.valueOf(String.valueOf(total_harga))));
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getDistance(){
        String address = encryptedPreferences.getString(ADDRESS, "Burger Tahu Malang Suhat 2");
        String preaddress = encryptedPreferences.getString(PREADDRESS, "MALANG");
        if (encryptedPreferences.getString("NETWORK", getString(R.string.stateDisconnected)).equalsIgnoreCase(getString(R.string.stateConnected))){
            /*url */
            String urlx = "https://maps.googleapis.com/maps/api/distancematrix/json?origins="+address+"&destinations="+preaddress+"&key=AIzaSyAkJ0mr5hfKYcvYg87CaBrN2T-8ML6KIl8";
            Log.d("ROUTE", "getDistance: "+urlx);
            Request request = client.newRequest()
                    .url(urlx)
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase(getString(R.string.statusok))){
                            JSONArray rowss = jsonObject.getJSONArray("rows");
                            if (rowss.length() > 0){
                                for (int i=0;i<rowss.length();i++){
                                    JSONObject objectElement = rowss.getJSONObject(i);
                                    JSONArray arrayElement = objectElement.getJSONArray("elements");
                                    if (arrayElement.length() > 0){
                                        int valuesm = 0;
                                        for (int x=0;x<arrayElement.length();x++){
                                            JSONObject element = arrayElement.getJSONObject(x);
                                            JSONObject distance = element.getJSONObject("distance");
                                            valuesm = valuesm + distance.getInt("value");
                                            encryptedPreferences.edit().putInt(DISTANCE, valuesm).apply();
                                        }
                                        updateDestination();
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
    @Override
    public void updateInfo() {
        RealmResults<TempTransaksiOrder> results = tempTransaksiOrderHelper.getItemOrder();
        int total_harga_item = 0;
        int biaya_kirim = 0;
        int total_harga = 0;
        if (results.size() > 0){
            for (int i=0;i<results.size();i++){
                int total = 0;
                total = results.get(i).getJumlah() * results.get(i).getHarga();
                total_harga_item += total;
            }
        }

        total_harga = total_harga_item + biaya_kirim;

        TextView txtTotalHargaItem = (TextView) v.findViewById(R.id.totalhargaitem);
        TextView txtDeliveryFee = (TextView) v.findViewById(R.id.deliveryfee);
        TextView txtTotalHarga = (TextView) v.findViewById(R.id.totalharga);

        txtTotalHargaItem.setText(numberFormat(Double.valueOf(String.valueOf(total_harga_item))));
        txtDeliveryFee.setText(numberFormat(Double.valueOf(String.valueOf(biaya_kirim))));
        txtTotalHarga.setText(numberFormat(Double.valueOf(String.valueOf(total_harga))));

        TextView totalSemua = (TextView) v.findViewById(R.id.totalSemua);
        totalSemua.setText(numberFormat(Double.valueOf(String.valueOf(total_harga))));
    }
    public void updateDestination(){
        String sAddress = encryptedPreferences.getString(PREADDRESS, "MALANG");
        String sDistance = String.valueOf(encryptedPreferences.getInt(DISTANCE, 0)/1000) + " Km";
        txtaddress.setText(sAddress);
        txtdistance.setText(sDistance);
    }
    public String showCurrentAddress(double latitude, double longitude){
        List<Address> addresses = null;
        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        return address;
    }
    public void postOrder(){
        if (encryptedPreferences.getString("NETWORK", getString(R.string.stateDisconnected)).equalsIgnoreCase(getString(R.string.stateConnected))){
            String token = encryptedPreferences.getUtils().decryptStringValue(encryptedPreferences.getString(KEY, "0"));
            RequestBody formBody = new FormBody.Builder()
                    .add("token", token)
                    .add("method", "new_order")
                    .add("alamat", encryptedPreferences.getString(PREADDRESS, "Burger Tahu Suhat 2 Malang"))
                    .add("latitude", encryptedPreferences.getString(PRELATITUDE, "-7,96662"))
                    .add("longitude", encryptedPreferences.getString(PRELONGITUDE, "112,633"))
                    .build();
            Request request = client.newRequest()
                    .url(ENDURIORDER)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    /*insert child item*/
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }else{
            /*show snackbar tidak ada koneksi*/
        }
    }
    public static String numberFormat(double d) {
        Double value = d;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp.");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(dfs);
        return formatter.format(value);
    }
    public void changeDestination(){
        double lati = Double.valueOf(encryptedPreferences.getString(PRELATITUDE, "-7,96662"));
        double longi = Double.valueOf(encryptedPreferences.getString(PRELONGITUDE, "112,633"));
        LatLng latLng = new LatLng(lati, longi);
        Intent intent = new LocationPickerActivity.Builder()
                .withLocation(latLng)
                .withGeolocApiKey(getString(R.string.google_api_key))
                .withSearchZone("en_EN")
                .shouldReturnOkOnBackPressed()
//                .withStreetHidden()
//                .withCityHidden()
//                .withZipCodeHidden()
//                .withSatelliteViewHidden()
                .build(getActivity().getApplicationContext());
        startActivityForResult(intent, 1);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            Toast.makeText(getActivity().getApplicationContext(), String.valueOf(resultCode), Toast.LENGTH_SHORT).show();
            if(resultCode == RESULT_OK){
                double latitude = data.getDoubleExtra(LocationPickerActivity.LATITUDE, 0);
                double longitude = data.getDoubleExtra(LocationPickerActivity.LONGITUDE, 0);
                String address = data.getStringExtra(LocationPickerActivity.LOCATION_ADDRESS);
                String postalcode = data.getStringExtra(LocationPickerActivity.ZIPCODE);
                encryptedPreferences.edit()
                        .putString(PRELATITUDE, String.valueOf(latitude))
                        .putString(PRELONGITUDE, String.valueOf(longitude))
                        .putString(PREADDRESS, showCurrentAddress(latitude, longitude))
                        .putString(PREPOSTALCODE, postalcode)
                        .apply();


                getDistance();

                Address fullAddress = data.getParcelableExtra(LocationPickerActivity.ADDRESS);
                if(fullAddress != null)
                    Log.d("FULL ADDRESS****", fullAddress.toString());
            }
            if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if(event.getState().getValue() == ConnectivityState.CONNECTED){
            encryptedPreferences.edit()
                    .putString("NETWORK", getString(R.string.stateConnected))
                    .apply();
        }
        else{
            encryptedPreferences.edit()
                    .putString("NETWORK", getString(R.string.stateDisconnected))
                    .apply();
        }
    }
}
