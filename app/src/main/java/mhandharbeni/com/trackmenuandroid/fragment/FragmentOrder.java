package mhandharbeni.com.trackmenuandroid.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.fragment.subfragment.OrderActive;
import mhandharbeni.com.trackmenuandroid.fragment.subfragment.OrderCompleted;

/**
 * Created by root on 06/06/17.
 */

public class FragmentOrder extends Fragment implements TabLayout.OnTabSelectedListener {
    View v;
    TabLayout tabLayout;
    Fragment fragment;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.order_layout, container, false);
        initTab();
        return v;
    }
    public void initTab(){
        tabLayout = (TabLayout) v.findViewById(R.id.MaterialTab);
        tabLayout.removeAllTabs();
        final TabLayout.Tab active = tabLayout.newTab();
        final TabLayout.Tab completed = tabLayout.newTab();
        active.setText("Active");
        completed.setText("Completed");
        tabLayout.addTab(active, 0);
        tabLayout.addTab(completed, 1);

        tabLayout.setTabTextColors(ContextCompat.getColorStateList(getActivity(), android.R.color.black));
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        tabLayout.setOnTabSelectedListener(this);
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        SpannableStringBuilder sbLengkap = null;
        switch (tab.getPosition()){
            case 0:
                /*active*/
                sbLengkap = setToBold("Active", "Active");
                fragment = new OrderActive();
                break;
            case 1:
                /*completd*/
                sbLengkap = setToBold("Completed", "Completed");
                fragment = new OrderCompleted();
                break;
        }
        tab.setText(sbLengkap);
        changeFragment(fragment);
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        SpannableStringBuilder sbLengkap = null;
        switch (tab.getPosition()){
            case 0:
                /*active*/
                sbLengkap = setToThin("Active", "Active");
                break;
            case 1:
                /*completd*/
                sbLengkap = setToThin("Completed", "Completed");
                break;
        }
        tab.setText(sbLengkap);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        SpannableStringBuilder sbLengkap = null;
        switch (tab.getPosition()){
            case 0:
                /*active*/
                sbLengkap = setToBold("Active", "Active");
                fragment = new OrderActive();
                break;
            case 1:
                /*completd*/
                sbLengkap = setToBold("Completed", "Completed");
                fragment = new OrderCompleted();
                break;
        }
        tab.setText(sbLengkap);
        changeFragment(fragment);
    }
    public void changeFragment(Fragment fragment){
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_order, fragment);
        ft.commit();
    }
    public SpannableStringBuilder setToBold(String text, String textToBold){

        SpannableStringBuilder builder=new SpannableStringBuilder();

        if(textToBold.length() > 0 && !textToBold.trim().equals("")){

            String testText = text.toLowerCase(Locale.US);
            String testTextToBold = textToBold.toLowerCase(Locale.US);
            int startingIndex = testText.indexOf(testTextToBold);
            int endingIndex = startingIndex + testTextToBold.length();

            if(startingIndex < 0 || endingIndex <0){
                return builder.append(text);
            }
            else if(startingIndex >= 0 && endingIndex >=0){

                builder.append(text);
                builder.setSpan(new StyleSpan(Typeface.BOLD), startingIndex, endingIndex, 0);
                builder.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.colorAccent)), startingIndex, endingIndex, 0);
            }
        }else{
            return builder.append(text);
        }

        return builder;
    }
    public SpannableStringBuilder setToThin(String text, String textToBold){

        SpannableStringBuilder builder=new SpannableStringBuilder();

        if(textToBold.length() > 0 && !textToBold.trim().equals("")){

            //for counting start/end indexes
            String testText = text.toLowerCase(Locale.US);
            String testTextToBold = textToBold.toLowerCase(Locale.US);
            int startingIndex = testText.indexOf(testTextToBold);
            int endingIndex = startingIndex + testTextToBold.length();
            //for counting start/end indexes

            if(startingIndex < 0 || endingIndex <0){
                return builder.append(text);
            }
            else if(startingIndex >= 0 && endingIndex >=0){

                builder.append(text);
                builder.setSpan(new StyleSpan(Typeface.NORMAL), startingIndex, endingIndex, 0);
                builder.setSpan(new ForegroundColorSpan(getActivity().getResources().getColor(R.color.colorAccent)), startingIndex, endingIndex, 0);
            }
        }else{
            return builder.append(text);
        }

        return builder;
    }

}
