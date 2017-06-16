package mhandharbeni.com.trackmenuandroid.adapter.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import mhandharbeni.com.trackmenuandroid.R;
import mhandharbeni.com.trackmenuandroid.adapter.model.MenuModel;

/**
 * Created by root on 16/06/17.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private List<MenuModel> menuList;
    private Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitleMenu, txtHargaMenu;
        public Button btnAddMenu;
        public ImageView imageMenu;

        public MyViewHolder(View view) {
            super(view);
            txtTitleMenu = (TextView) view.findViewById(R.id.txtTitleMenu);
            txtHargaMenu = (TextView) view.findViewById(R.id.txtHargaMenu);
            btnAddMenu = (Button) view.findViewById(R.id.btnAddMenu);
            imageMenu = (ImageView) view.findViewById(R.id.imageMenu);
        }
    }

    public MenuAdapter(Context mContext, List<MenuModel> menuList) {
        this.mContext = mContext;
        this.menuList = menuList;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        MenuModel m = menuList.get(position);
        holder.txtTitleMenu.setText(m.getNama());
        holder.txtHargaMenu.setText(m.getHarga());
        Picasso.with(mContext).load(m.getGambar()).placeholder(R.mipmap.ic_launcher).into(holder.imageMenu);
        holder.btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, holder.txtHargaMenu.getText(), Toast.LENGTH_SHORT).show();
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