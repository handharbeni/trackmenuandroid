package mhandharbeni.com.trackmenuandroid.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mhandharbeni.com.trackmenuandroid.R;

/**
 * Created by root on 06/06/17.
 */

public class FragmentAkun extends Fragment {
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.akun_layout, container, false);
        return v;
    }
}

