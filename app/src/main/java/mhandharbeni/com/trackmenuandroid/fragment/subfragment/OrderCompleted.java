package mhandharbeni.com.trackmenuandroid.fragment.subfragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import mhandharbeni.com.trackmenuandroid.MainActivity;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentOrder;

/**
 * Created by root on 06/06/17.
 */

public class OrderCompleted extends Fragment {
    View v;
    ImageView backsButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.order_complete, container, false);
//        initBack();
        return v;
    }
    public void initBack(){
        ((MainActivity)getActivity()).showBackButton();
        backsButton = (ImageView)((MainActivity)getActivity()).findViewById(R.id.imageBack);
        backsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).setTitleDefault();
                ((MainActivity)getActivity()).goBack(new FragmentOrder());
                ((MainActivity)getActivity()).hideBackButton();
            }
        });

    }
}
