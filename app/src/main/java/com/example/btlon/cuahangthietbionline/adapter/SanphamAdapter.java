package com.example.btlon.cuahangthietbionline.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.btlon.cuahangthietbionline.R;
import com.example.btlon.cuahangthietbionline.activity.ChiTietSanPham;
import com.example.btlon.cuahangthietbionline.model.Sanpham;
import com.example.btlon.cuahangthietbionline.ultil.CheckConnection;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class SanphamAdapter extends RecyclerView.Adapter<SanphamAdapter.ItemHolder> {
    Context context;
    ArrayList<Sanpham> sanphamArrayList;

    public SanphamAdapter(Context context, ArrayList<Sanpham> sanphamArrayList) {
        this.context = context;
        this.sanphamArrayList = sanphamArrayList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_sanphammoinhat,null);
        ItemHolder itemHolder = new ItemHolder(view);
        return itemHolder;

    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Sanpham sanpham = sanphamArrayList.get(position);
        holder.txttensanpham.setText(sanpham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        holder.txtgiasanpham.setText("Giá: "+decimalFormat.format(sanpham.getGiasp())+ " Đ");

        Glide.with(context).load(sanpham.getHinhanhsp())
                .into(holder.imghinhsanpham);


    }

    @Override
    public int getItemCount() {
        return sanphamArrayList.size() ;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        public ImageView imghinhsanpham;
        public TextView txttensanpham,txtgiasanpham;

        public ItemHolder(View itemView) {
            super(itemView);
            imghinhsanpham = itemView.findViewById(R.id.imageviewsanpham);
            txtgiasanpham = itemView.findViewById(R.id.textviewgiasanpham);
            txttensanpham = itemView.findViewById(R.id.textviewtensanpham);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChiTietSanPham.class);
                    intent.putExtra("thongtinsanpham",sanphamArrayList.get(getLayoutPosition()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   // CheckConnection.ShowToast_Short(context,sanphamArrayList.get(getLayoutPosition()).getTensp());
                    context.startActivity(intent);

                }
            });
        }
    }
}
