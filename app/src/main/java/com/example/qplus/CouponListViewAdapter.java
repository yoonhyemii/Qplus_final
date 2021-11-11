package com.example.qplus;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

public class CouponListViewAdapter extends BaseAdapter {

    //Adaper에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CouponListViewItem> listViewItemList = new ArrayList<>();

    public CouponListViewAdapter() {

    }
    //Adapter에 사용되는 데이터의 개수를 리턴
    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    //position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.coupon_listview_item, parent, false);
        }

        // 화면에 표시될 View로부터 위젯에 대한 참조 획득
        TextView sectionTextView = convertView.findViewById(R.id.coupon_section);
        TextView nameTextView = convertView.findViewById(R.id.coupon_name);
        TextView dateTextView = convertView.findViewById(R.id.coupon_date);


        CouponListViewItem listViewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        sectionTextView.setText(listViewItem.getCouponSection());
        nameTextView.setText(listViewItem.getCouponName());
        dateTextView.setText(listViewItem.getCouponDate());

        return convertView;
    }

    //지정한 위치(position)에 있는 데이터 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    //아이템 데이터 추가를 위한 함수
    public void addItem(String couponSection, String couponName, String couponDate) {
        CouponListViewItem item = new CouponListViewItem();

        item.setCouponSection(couponSection);
        item.setCouponName(couponName);
        item.setCouponDate(couponDate);

        listViewItemList.add(item);
    }
}