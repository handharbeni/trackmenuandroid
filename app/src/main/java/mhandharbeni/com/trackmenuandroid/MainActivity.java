package mhandharbeni.com.trackmenuandroid;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.golovin.fluentstackbar.FluentSnackbar;
import com.google.android.gms.location.LocationRequest;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.stephentuso.welcome.WelcomeHelper;
import com.zplesac.connectionbuddy.ConnectionBuddy;
import com.zplesac.connectionbuddy.ConnectionBuddyConfiguration;
import com.zplesac.connectionbuddy.interfaces.ConnectivityChangeListener;
import com.zplesac.connectionbuddy.models.ConnectivityEvent;
import com.zplesac.connectionbuddy.models.ConnectivityState;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentAkun;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentHome;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentOrder;
import mhandharbeni.com.trackmenuandroid.fragment.subcart.Cart;
import mhandharbeni.com.trackmenuandroid.services.MainServices;
import sexy.code.Callback;
import sexy.code.FormBody;
import sexy.code.HttPizza;
import sexy.code.Request;
import sexy.code.RequestBody;
import sexy.code.Response;

public class MainActivity
        extends EasyLocationAppCompatActivity
        implements BottomNavigation.OnMenuItemSelectionListener, ConnectivityChangeListener {

    public String TAG = this.getClass().getSimpleName().toString();
    private FluentSnackbar mFluentSnackbar;
    public String STAT = "stat", KEY = "key", NAMA="nama", EMAIL= "email", PICTURE = "gambar";
    BottomNavigation bottomNavigation;
    Fragment fragment = null;
    WelcomeHelper welcomeScreen;

    EncryptedPreferences encryptedPreferences;
    ConnectionBuddyConfiguration networkInspectorConfiguration;
    HttPizza client;

    RelativeLayout mainLayout, signinLayout, signupLayout;
    MaterialEditText txtEmail, txtPassword, txtDNama, txtDEmail, txtDPassword;
    TextView askLogin, askAccount;
    Button btnSignin, btnSignup;
    ImageView backButton, cartButton;

    String endUri, uriLogin, uriDaftar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] permissions = new String[11];
        permissions[0] = Manifest.permission.CAMERA;
        permissions[1] = Manifest.permission.INTERNET;
        permissions[2] = Manifest.permission.WAKE_LOCK;
        permissions[3] = Manifest.permission.LOCATION_HARDWARE;
        permissions[4] = Manifest.permission.ACCESS_COARSE_LOCATION;
        permissions[5] = Manifest.permission.ACCESS_FINE_LOCATION;
        permissions[6] = Manifest.permission.READ_PHONE_STATE;
        permissions[7] = Manifest.permission.ACCESS_NETWORK_STATE;
        permissions[8] = Manifest.permission.ACCESS_WIFI_STATE;
        permissions[9] = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        permissions[10] = Manifest.permission.READ_EXTERNAL_STORAGE;

        ActivityCompat.requestPermissions(
                this,
                permissions,
                5
        );
        startTracking();
        encryptedPreferences = new EncryptedPreferences.Builder(this).withEncryptionPassword(getString(R.string.KeyPassword)).build();
        networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        client = new HttPizza();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        endUri = getString(R.string.server);
        uriDaftar = endUri+"/users/daftar";
        uriLogin = endUri+"/users/login";

        welcomeScreen = new WelcomeHelper(this, MyWelcomeActivity.class);
        welcomeScreen.show(savedInstanceState);

        setContentView(R.layout.activity_main);

        cartButton = (ImageView) findViewById(R.id.imageCart);
        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTitle("Cart");
                changeFragment(new Cart());
            }
        });

        initLayout();
        initItemLayout();
        checkSession();
    }
    public void startTracking(){
        LocationRequest locationRequest = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(5000)
                .setFastestInterval(5000);
        EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                .setLocationRequest(locationRequest)
                .setFallBackToLastLocationTime(3000)
                .build();

        requestLocationUpdates(easyLocationRequest);
    }
    public void startServices(){
        if (!MainServices.serviceRunning){
            startService(new Intent(getApplicationContext(), MainServices.class));
        }
    }
    public void stopServices(){
        stopService(new Intent(this, MainServices.class));

        ConnectionBuddy.getInstance().clearNetworkCache(this);
        ConnectionBuddy.getInstance().notifyConnectionChange(true, this);

        networkInspectorConfiguration = new ConnectionBuddyConfiguration.Builder(this).build();
        ConnectionBuddy.getInstance().init(networkInspectorConfiguration);

    }
    public void changeTitle(String title){
        TextView customTitle = (TextView) findViewById(R.id.customTitle);
        customTitle.setText(title);
    }
    public void initLayout(){
        mainLayout = (RelativeLayout) findViewById(R.id.mainlayout);
        signinLayout = (RelativeLayout) findViewById(R.id.signinLayout);
        signupLayout = (RelativeLayout) findViewById(R.id.signupLayout);
    }
    public void initItemLayout(){
        txtEmail = (MaterialEditText) findViewById(R.id.txtEmail);
        txtPassword = (MaterialEditText) findViewById(R.id.txtPassword);
        txtDNama = (MaterialEditText) findViewById(R.id.txtDNama);
        txtDEmail = (MaterialEditText) findViewById(R.id.txtDEmail);
        txtDPassword = (MaterialEditText) findViewById(R.id.txtDPassword);


        askAccount = (TextView) findViewById(R.id.askAccount);
        askLogin = (TextView) findViewById(R.id.askLogin);

        btnSignin = (Button) findViewById(R.id.btnSignin);
        btnSignup = (Button) findViewById(R.id.btnSignup);

    }

    public void initBottomMenu(){
        bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        bottomNavigation.setOnMenuItemClickListener(this);
    }
    public void checkSession(){
        // 0 : logout (default)
        // 1 : register
        // 2 : main layout

        String loggedin = encryptedPreferences.getString(STAT, "0");
        setView(Integer.valueOf(loggedin));
    }

    public void setView(int loggedin){
        mainLayout.setVisibility(View.GONE);
        signinLayout.setVisibility(View.GONE);
        signupLayout.setVisibility(View.GONE);
        switch (loggedin){
            case 0 :
                /*logout*/
                signinLayout.setVisibility(View.VISIBLE);
                btnSignin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doSignin();
                    }
                });
                break;
            case 1 :
                /*register*/
                signupLayout.setVisibility(View.VISIBLE);
                btnSignup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        doSignup();
                    }
                });
                break;
            case 2 :
                /*main layout*/
                mainLayout.setVisibility(View.VISIBLE);
                startServices();
                initBottomMenu();
                bottomNavigation.setSelectedIndex(0, true);
                Fragment fragment = new FragmentHome();
                changeFragment(fragment);
                break;
            default:
                break;
        }
    }
    public void setTitleDefault(){
        TextView customTitle = (TextView) findViewById(R.id.customTitle);
        customTitle.setText(getString(R.string.app_name));
    }
    public void setTitleDefault(String title){
        TextView customTitle = (TextView) findViewById(R.id.customTitle);
        customTitle.setText(title);
    }
    public void showBackButton(){
        backButton = (ImageView) findViewById(R.id.imageBack);
        backButton.setVisibility(View.VISIBLE);
    }
    public void hideBackButton(){
        backButton = (ImageView) findViewById(R.id.imageBack);
        backButton.setVisibility(View.GONE);
    }
    public void goBack(){
        Fragment fr = new FragmentHome();
        changeFragment(fr);
    }
    public void goBack(Fragment fragment){
        changeFragment(fragment);
    }
    public void setSession(String key, String nama, String email, String pos){
        encryptedPreferences.edit()
                .putString(KEY, key)
                .putString(NAMA, nama)
                .putString(EMAIL, email)
                .putString(STAT, pos)
                .apply();
        checkSession();
    }
    public void doSignin(){
        if (encryptedPreferences.getString("NETWORK", getString(R.string.stateConnected)).equalsIgnoreCase(getString(R.string.stateConnected))){
            RequestBody formBody = new FormBody.Builder()
                    .add("email", txtEmail.getText().toString())
                    .add("password", txtPassword.getText().toString())
                    .build();
            Request request = client.newRequest()
                    .url(uriLogin)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    /*nama, email, access_token*/
                    try {
                        JSONObject jsonReturn = new JSONObject(response.body().string());
                        boolean returns = jsonReturn.getBoolean("return");
                        if (returns){
                            /*login sukses*/
                            String nama = jsonReturn.getString("nama");
                            String email = jsonReturn.getString("email");
                            String token = encryptedPreferences.getUtils().encryptStringValue(jsonReturn.getString("access_token"));
                            setSession(token, nama, email, "2");
                        }else{
                            /*login gagal*/
                            showSnackBar(getString(R.string.logingagal));
                        }
                    } catch (IOException|JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }else{
            showSnackBar(getString(R.string.notifnoconnection));
        }
    }
    public void doSignup(){
        if (encryptedPreferences.getString("NETWORK", getString(R.string.stateConnected)).equalsIgnoreCase(getString(R.string.stateConnected))){
            RequestBody formBody = new FormBody.Builder()
                    .add("nama", txtDNama.getText().toString())
                    .add("email", txtDEmail.getText().toString())
                    .add("password", txtDPassword.getText().toString())
                    .build();
            Request request = client.newRequest()
                    .url(uriDaftar)
                    .post(formBody)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onResponse(Response response) {
                    try {
                        String message;
                        JSONObject jsonReturn = new JSONObject(response.body().string());
                        boolean result = jsonReturn.getBoolean("return");
                        if (result){
                            /*berhasil*/
                            message = jsonReturn.getString("message");
                        }else{
                            /*gagal*/
                            message = jsonReturn.getString("error_message");
                        }
                        showSnackBar(message);
                    } catch (IOException|JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Throwable t) {

                }
            });
        }else{
            showSnackBar(getString(R.string.notifnoconnection));
        }
    }
    public void showSnackBar(String message){
        mFluentSnackbar = FluentSnackbar.create(this);
        mFluentSnackbar.create(message)
                .maxLines(2)
                .backgroundColorRes(R.color.colorPrimary)
                .textColorRes(android.R.color.black)
                .duration(Snackbar.LENGTH_SHORT)
                .actionText(message)
                .actionTextColorRes(R.color.colorAccent)
                .important()
                .show();
    }
    public void changeFragment(Fragment fragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_container, fragment);
        ft.commit();
    }
    @Override
    public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {
        switch (i1){
            case 0:
                /*home*/
                fragment = new FragmentHome();
                break;
            case 1:
                /*order*/
                fragment = new FragmentOrder();
                break;
            case 2:
                /*account*/
                fragment = new FragmentAkun();
                break;
            default:
                break;
        }
        changeFragment(fragment);
    }

    @Override
    public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
        switch (i1){
            case 0:
                /*home*/
                fragment = new FragmentHome();
                break;
            case 1:
                /*order*/
                fragment = new FragmentOrder();
                break;
            case 2:
                /*account*/
                fragment = new FragmentAkun();
                break;
            default:
                break;
        }
        changeFragment(fragment);
    }

    @Override
    public void onConnectionChange(ConnectivityEvent event) {
        if(event.getState().getValue() == ConnectivityState.CONNECTED){
            // device has active internet connection
            encryptedPreferences.edit()
                    .putString("NETWORK", getString(R.string.stateConnected))
                    .apply();
        }
        else{
            // there is no active internet connection on this device
            encryptedPreferences.edit()
                    .putString("NETWORK", getString(R.string.stateDisconnected))
                    .apply();
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        ConnectionBuddy.getInstance().registerForConnectivityEvents(this, this);
    }

    @Override
    public void onStop() {
        ConnectionBuddy.getInstance().unregisterFromConnectivityEvents(this);
        super.onStop();
    }

    @Override
    public void onLocationPermissionGranted() {

    }

    @Override
    public void onLocationPermissionDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS Service are Disabled");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    @Override
    public void onLocationReceived(Location location) {
        encryptedPreferences.edit()
                .putString("latitude", String.valueOf(location.getLatitude()))
                .putString("longitude", String.valueOf(location.getLongitude()))
                .putString("altitude", String.valueOf(location.getAltitude()))
                .putString("accuracy", String.valueOf(location.getAccuracy()))
                .apply();
    }

    @Override
    public void onLocationProviderEnabled() {

    }

    @Override
    public void onLocationProviderDisabled() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS Service are Disabled");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });
        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
