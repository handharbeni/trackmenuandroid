package mhandharbeni.com.trackmenuandroid.adapter.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.adapter.model.MenuModel;
import mhandharbeni.com.trackmenuandroid.database.helper.TempTransaksiOrderHelper;
import mhandharbeni.com.trackmenuandroid.database.model.TempTransaksiOrder;

/**
 * Created by root on 16/06/17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private String TAG = "MenuAdapter";
    private List<MenuModel> menuList;
    private Context mContext;
    private TempTransaksiOrderHelper temptoHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitleMenu, txtHargaMenu;
        public Button btnAddMenu;
        public ImageView imageMenu;
        public FoldingCell foldingCell;

        public MyViewHolder(View view) {
            super(view);
            txtTitleMenu = (TextView) view.findViewById(R.id.txtTitleMenu);
            txtHargaMenu = (TextView) view.findViewById(R.id.txtHargaMenu);
            btnAddMenu = (Button) view.findViewById(R.id.btnAddMenu);
            imageMenu = (ImageView) view.findViewById(R.id.imageMenu);
            foldingCell = (FoldingCell) view.findViewById(R.id.folding_cell);
        }
    }

    public MenuAdapter(Context mContext, List<MenuModel> menuList) {
        this.mContext = mContext;
        this.menuList = menuList;
        this.temptoHelper = new TempTransaksiOrderHelper(mContext);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MenuModel m = menuList.get(position);
        holder.txtTitleMenu.setText(m.getNama());
        holder.txtHargaMenu.setText(m.getHarga());
        Picasso.with(mContext).load(m.getGambar()).placeholder(R.mipmap.ic_launcher).into(holder.imageMenu);
        holder.foldingCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.foldingCell.toggle(false);
            }
        });
        holder.btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean duplicate = temptoHelper.checkDuplicate(m.getId());
                Log.d(TAG, "onClick: "+duplicate);
                if (!duplicate){
                    /*insert new*/
                    TempTransaksiOrder to = new TempTransaksiOrder();
                    to.setId(m.getId());
                    to.setId_menu(m.getId());
                    to.setJumlah(1);
                    to.setHarga(Integer.valueOf(m.getHarga()));
                    to.setTotal_harga(Integer.valueOf(m.getHarga())*1);
                    temptoHelper.addItemOrder(to);
                }else{
                    /*update qty + harga total*/
                    /*get last qty*/
                    int lastQty = 1;
                    RealmResults<TempTransaksiOrder> results = temptoHelper.getItemOrder(m.getId());
                    lastQty += results.get(0).getJumlah();
                    /*update to table*/
                    temptoHelper.updateJumlah(m.getId(), lastQty, Integer.valueOf(m.getHarga())*lastQty);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_menu ,parent, false);
        return new MyViewHolder(v);
    }
}