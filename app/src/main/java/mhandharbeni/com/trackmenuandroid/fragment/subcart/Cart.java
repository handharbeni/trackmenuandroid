package mhandharbeni.com.trackmenuandroid.fragment.subcart;

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
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.MainActivity;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.adapter.adapter.TempTransaksiOrderAdapter;
import mhandharbeni.com.trackmenuandroid.adapter.decoration.DividerItemDecoration;
import mhandharbeni.com.trackmenuandroid.adapter.model.TempTransaksiOrderModel;
import mhandharbeni.com.trackmenuandroid.database.helper.TempTransaksiOrderHelper;
import mhandharbeni.com.trackmenuandroid.database.model.TempTransaksiOrder;
import mhandharbeni.com.trackmenuandroid.fragment.FragmentHome;
import mhandharbeni.com.trackmenuandroid.fragment.submenu.DetailMenu;
import mhandharbeni.com.trackmenuandroid.util.CartUtil;

/**
 * Created by root on 20/06/17.
 */

public class Cart extends Fragment  implements CartUtil{
    static TempTransaksiOrderHelper tempTransaksiOrderHelper;
    static View v;

    RecyclerView listCart;
    TextView txtAddCart;
    List<TempTransaksiOrderModel> cartList;
    ImageView backsButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tempTransaksiOrderHelper = new TempTransaksiOrderHelper(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.list_cart, container, false);
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
        initBack();
        initData();
        initAdapter();
        initLayoutManager();
        updateInfoBottom();
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
//        listCart.setLayoutFrozen(true);
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

    @Override
    public void updateInfo() {
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
    public static String numberFormat(double d) {
        Double value = Double.valueOf(d) ;
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("Rp.");
        ((DecimalFormat) formatter).setDecimalFormatSymbols(dfs);
        String gainString = formatter.format(value);
        return gainString;
    }
}
