package mhandharbeni.com.trackmenuandroid.fragment.submap;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import mhandharbeni.com.trackmenuandroid.MainActivity;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentHome;
import mhandharbeni.com.trackmenuandroid.fragment.subcart.Cart;

/**
 * Created by root on 09/07/17.
 */

public class MapsFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private GoogleMap mMap;
    private Geocoder geocoder;
    public View v;
    EncryptedPreferences encryptedPreferences;
    ImageView backsButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        encryptedPreferences = new EncryptedPreferences.Builder(getActivity().getApplicationContext()).withEncryptionPassword(getString(R.string.KeyPassword)).build();

        v = inflater.inflate(R.layout.maps_layout, container, false);
        MapView mapFragment = (MapView) v.findViewById(R.id.map);
        initBack();

        mapFragment.onCreate(savedInstanceState);

        mapFragment.onResume();// needed to get the map to display immediately

        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double longs = Double.valueOf(encryptedPreferences.getString("long", "112.615440"));
        double lats = Double.valueOf(encryptedPreferences.getString("lat", "-7.946527"));
        String addressx = showCurrentAddress(lats, longs);
        LatLng malang = new LatLng(lats, longs);
        mMap.addMarker(new MarkerOptions().position(malang).title(addressx).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(malang, 12.0f));
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                LatLng position = marker.getPosition();
                String address = showCurrentAddress(position.latitude, position.longitude);
                marker.setTitle(address);
            }
        });
    }
    public String showCurrentAddress(double latitude, double longitude){
        List<Address> addresses = null;
        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
        return address;
    }
    @Override
    public void onLocationChanged(Location location) {
        encryptedPreferences.edit()
                .putString("long", String.valueOf(location.getLongitude()))
                .putString("lat", String.valueOf(location.getLatitude()))
                .apply();
    }
    public void initBack() {
        ((MainActivity) getActivity()).showBackButton();
        backsButton = (ImageView) ((MainActivity) getActivity()).findViewById(R.id.imageBack);
        backsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).changeTitle("Cart");
                ((MainActivity) getActivity()).goBack(new Cart());
//                ((MainActivity) getActivity()).hideBackButton();
            }
        });

    }
}
