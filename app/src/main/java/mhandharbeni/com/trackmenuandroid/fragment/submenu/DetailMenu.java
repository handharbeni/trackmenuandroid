package mhandharbeni.com.trackmenuandroid.fragment.submenu;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.MainActivity;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.adapter.adapter.MenuAdapter;
import mhandharbeni.com.trackmenuandroid.adapter.decoration.DividerItemDecoration;
import mhandharbeni.com.trackmenuandroid.adapter.model.MenuModel;
import mhandharbeni.com.trackmenuandroid.database.helper.MenuTableHelper;
import mhandharbeni.com.trackmenuandroid.database.model.MenuTable;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentHome;

/**
 * Created by root on 16/06/17.
 */

public class DetailMenu extends Fragment {
    MenuTableHelper mtHelper;
    View v;

    RecyclerView listMenu;
    List<MenuModel> menuList;
    String kategori;
    ImageView backsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mtHelper = new MenuTableHelper(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.list_menu, container, false);
        kategori=getArguments().getString("kategori");
        initBack();
        initData();
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
                ((MainActivity)getActivity()).goBack(new FragmentHome());
                ((MainActivity)getActivity()).hideBackButton();
            }
        });

    }
    public void initData(){
        menuList = new ArrayList<>();
        RealmResults<MenuTable> results = mtHelper.getMenu(kategori);
        if (results.size() > 0){
            for (int i=0;i<results.size();i++){
                menuList.add(
                        new MenuModel(
                                results.get(i).getId(),
                                results.get(i).getNama(),
                                results.get(i).getGambar(),
                                results.get(i).getHarga(),
                                results.get(i).getKategori()
                        )
                );
            }
        }
    }
    public void initAdapter(){
        listMenu = (RecyclerView) v.findViewById(R.id.listMenu);
        MenuAdapter ma = new MenuAdapter(getActivity().getApplicationContext(), menuList);
        listMenu.setAdapter(ma);
    }
    public void initLayoutManager(){
        int decorPriorityIndex = 0;
        Drawable dividerDrawable = ContextCompat.getDrawable(getActivity().getApplicationContext(), R.drawable.divider_menu);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        listMenu.addItemDecoration(dividerItemDecoration, decorPriorityIndex);
        listMenu.setLayoutManager(llm);
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
}
