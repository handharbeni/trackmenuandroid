package mhandharbeni.com.trackmenuandroid.fragment.subfragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import mhandharbeni.com.trackmenuandroid.MainActivity;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.adapter.adapter.MenuAdapter;
import mhandharbeni.com.trackmenuandroid.adapter.decoration.DividerItemDecoration;
import mhandharbeni.com.trackmenuandroid.adapter.model.MenuModel;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentHome;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentOrder;

/**
 * Created by root on 06/06/17.
 */

public class OrderActive extends Fragment {
    View v;
    RecyclerView rvActive;
    ImageView backsButton;

    List<MenuModel> menuList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.order_active, container, false);
        rvActive = (RecyclerView) v.findViewById(R.id.listActive);
//        initBack();
        dummyData();
        initAdapter();
        initLayoutManager();
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
    public void dummyData(){
        menuList = new ArrayList<>();
        for (int i=0;i<10;i++){
            menuList.add(new MenuModel(i,"test"+i,"http://i.imgur.com/DvpvklR.png",String.valueOf(20000*i),"Makanan"));
        }
    }
    public void initAdapter(){
        MenuAdapter ma = new MenuAdapter(getActivity().getApplicationContext(), menuList);
        rvActive.setAdapter(ma);
    }
    public void initLayoutManager(){
        int decorPriorityIndex = 0;
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.divider_menu);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rvActive.addItemDecoration(dividerItemDecoration, decorPriorityIndex);
        rvActive.setLayoutManager(llm);
    }
}
