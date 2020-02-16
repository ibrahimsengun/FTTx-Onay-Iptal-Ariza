package com.example.ftthonay;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class Adapter extends BaseAdapter {

    Context mContext;
    List<Tesis> mData;

    public Adapter(Context mContext, List<Tesis> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext, R.layout.custom_listview, null);

        TextView tv_is = view.findViewById(R.id.tv_isIsim);
        TextView tv_tarih = view.findViewById(R.id.tv_tarih);

        tv_is.setText(mData.get(position).getImageName());
        tv_tarih.setText(mData.get(position).getTarih());
        return view;
    }
}