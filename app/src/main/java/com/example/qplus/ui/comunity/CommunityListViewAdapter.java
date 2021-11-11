package com.example.qplus.ui.comunity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qplus.R;

import java.util.ArrayList;

public class CommunityListViewAdapter  extends BaseAdapter {

    //Adaper에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<CommunityListViewItem> listViewItemList = new ArrayList<CommunityListViewItem>();

    public CommunityListViewAdapter() {

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
            convertView = inflater.inflate(R.layout.community_lv_item, parent, false);
        }

        // 화면에 표시될 View로부터 위젯에 대한 참조 획득
        ImageView iconImageView = convertView.findViewById(R.id.newsImg);
        TextView titleTextView = convertView.findViewById(R.id.newsTitle);
        TextView descTextView = convertView.findViewById(R.id.newsContents);


        CommunityListViewItem listViewItem = listViewItemList.get(position);

        //아이템 내 각 위젯에 데이터 반영
        iconImageView.setImageDrawable((Drawable) listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());

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
    public void addItem(Drawable icon, String title, String desc) {
        CommunityListViewItem item = new CommunityListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        listViewItemList.add(item);
    }
}