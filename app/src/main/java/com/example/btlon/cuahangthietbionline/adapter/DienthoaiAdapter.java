package com.example.btlon.cuahangthietbionline.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.btlon.cuahangthietbionline.R;
import com.example.btlon.cuahangthietbionline.model.Sanpham;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class DienthoaiAdapter extends BaseAdapter {
    Context context;
    ArrayList<Sanpham> phukienArraylist;

    public DienthoaiAdapter(Context context, ArrayList<Sanpham> dienthoaiArraylist) {
        this.context = context;
        this.phukienArraylist = dienthoaiArraylist;
    }

    @Override
    public int getCount() {
        return phukienArraylist.size();
    }

    @Override
    public Object getItem(int i) {
        return phukienArraylist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder =null;
        if(view==null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.dong_dienthoai,null);
            viewHolder.txttenphukien = view.findViewById(R.id.textviewphukien);
            viewHolder.txtgiaphukien = view.findViewById(R.id.textviewgiaphukien);
            viewHolder.txtmotaphukien = view.findViewById(R.id.textviewmotaphukien);
            viewHolder.imgphukien = view.findViewById(R.id.imageviewphukien);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Sanpham sanpham = (Sanpham) getItem(i);
        viewHolder.txttenphukien.setText(sanpham.getTensp());
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.txtgiaphukien.setText("Giá: "+decimalFormat.format(sanpham.getGiasp())+ " Đ");
        viewHolder.txtmotaphukien.setMaxLines(2);
        viewHolder.txtmotaphukien.setEllipsize(TextUtils.TruncateAt.END);
        viewHolder.txtmotaphukien.setText(sanpham.getMotasp());

        Glide.with(context).load(sanpham.getHinhanhsp())
                .into(viewHolder.imgphukien);

        return view;
    }

    public class ViewHolder{
         TextView txttenphukien, txtgiaphukien, txtmotaphukien;
         ImageView imgphukien;

    }
}
