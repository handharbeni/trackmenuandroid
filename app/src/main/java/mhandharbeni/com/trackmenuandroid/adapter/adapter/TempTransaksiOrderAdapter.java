package mhandharbeni.com.trackmenuandroid.adapter.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.RealmResults;
import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.adapter.model.TempTransaksiOrderModel;
import mhandharbeni.com.trackmenuandroid.database.helper.MenuTableHelper;
import mhandharbeni.com.trackmenuandroid.database.helper.TempTransaksiOrderHelper;
import mhandharbeni.com.trackmenuandroid.database.model.MenuTable;
import mhandharbeni.com.trackmenuandroid.database.model.TempTransaksiOrder;
import mhandharbeni.com.trackmenuandroid.util.CartUtil;

/**
 * Created by root on 20/06/17.
 */

public class TempTransaksiOrderAdapter extends RecyclerView.Adapter<TempTransaksiOrderAdapter.MyViewHolder> {
    private String TAG = "CartAdapter";
    private List<TempTransaksiOrderModel> menuList;
    private Context mContext;
    private MenuTableHelper menuTableHelper;
    private TempTransaksiOrderHelper tempTransaksiOrderHelper;

    private CartUtil cartUtil;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitleMenu, txtHargaMenu;
        public Button btnDeleteItem;
        public ImageView imageMenu;
        public EditText eQty;

        public MyViewHolder(View view) {
            super(view);
            txtTitleMenu = (TextView) view.findViewById(R.id.txtTitleMenu);
            txtHargaMenu = (TextView) view.findViewById(R.id.txtHargaMenu);
            btnDeleteItem = (Button) view.findViewById(R.id.btnDeleteItem);
            imageMenu = (ImageView) view.findViewById(R.id.imageMenu);
            eQty = (EditText) view.findViewById(R.id.eQty);
        }
    }

    public TempTransaksiOrderAdapter(Context mContext, List<TempTransaksiOrderModel> menuList, CartUtil cartUtil) {
        this.mContext = mContext;
        this.menuList = menuList;
        this.menuTableHelper = new MenuTableHelper(mContext);
        this.tempTransaksiOrderHelper = new TempTransaksiOrderHelper(mContext);
        this.cartUtil = cartUtil;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final TempTransaksiOrderAdapter.MyViewHolder holder, int position) {
        if (position <= getItemCount()){
            final int fPosition = position;
            final TempTransaksiOrderModel m = menuList.get(fPosition);

            RealmResults<MenuTable> menuResult = menuTableHelper.getMenu(m.getId());
            if (menuResult.size() > 0){
                String nameMenu = menuTableHelper.getAttribut(m.getId(), "name");
                String priceMenu = menuTableHelper.getAttribut(m.getId(), "harga");
                String descMenu = menuTableHelper.getAttribut(m.getId(), "deskripsi");
                String katMenu = menuTableHelper.getAttribut(m.getId(), "kategori");
                String gambar = menuTableHelper.getAttribut(m.getId(), "gambar");
                String title = nameMenu;
                String image = gambar;
                holder.txtTitleMenu.setText(title);
                holder.txtHargaMenu.setText(String.valueOf(m.getHarga()));
                holder.eQty.setText(String.valueOf(m.getJumlah()));
                Picasso.with(mContext).load(image).placeholder(R.mipmap.ic_launcher).into(holder.imageMenu);
                holder.eQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (s.length() > 0){
                            if (s.toString().equalsIgnoreCase("0")){
                                deleteItemTemp(m.getId(), holder);
                            }else{
                                updateItemTemp(m.getId(), Integer.valueOf(s.toString()));
                            }
                        }

                    }
                });
                holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItemTemp(m.getId(), holder);
                    }
                });

            }
        }
    }
    public void swap(List<TempTransaksiOrderModel> datas){
        menuList.clear();
        menuList.addAll(datas);
        notifyDataSetChanged();
    }
    public void notifNewData(){
        menuList.clear();
        RealmResults<TempTransaksiOrder> results = tempTransaksiOrderHelper.getItemOrder();
        for (int i=0;i<results.size();i++){
            menuList.add(new TempTransaksiOrderModel(results.get(i).getId(), results.get(i).getId_menu(), results.get(i).getJumlah(), results.get(i).getHarga(), results.get(i).getTotal_harga()));
        }
    }
    public void updateItemTemp(int id, int qty){
        tempTransaksiOrderHelper.updateJumlah(id, qty);
        cartUtil.updateInfo();
    }
    public void deleteItemTemp(int id, final TempTransaksiOrderAdapter.MyViewHolder holder){
        tempTransaksiOrderHelper.deleteItem(id);
        int newPosition = holder.getAdapterPosition();
//        menuList.remove(newPosition);
        notifNewData();
        notifyItemRemoved(newPosition);
        notifyItemRangeChanged(newPosition, menuList.size());
        cartUtil.updateInfo();
    }
    public void deleteItemTemp(int id, int position){
        tempTransaksiOrderHelper.deleteItem(id);
        menuList.remove(position);
//        menuList = tempTransaksiOrderHelper.getItemOrder();
//        swap(menuList);
//        menuList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, menuList.size());
        cartUtil.updateInfo();
    }
    @Override
    public int getItemCount() {
        return menuList.size();
    }

    @Override
    public TempTransaksiOrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart ,parent, false);
        return new TempTransaksiOrderAdapter.MyViewHolder(v);
    }
}