package com.example.qplus.ui.market;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qplus.GlideApp;
import com.example.qplus.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MarketListViewAdapter extends BaseAdapter {

    FirebaseStorage storage;
    StorageReference storageRef_profile;
    private ImageView icon;
    private TextView name;
    private TextView stampCnt;
    Context context;

    private ArrayList<MarketListViewItem> listViewItemList = new ArrayList<>();

    public MarketListViewAdapter() { this.context = context; }

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
    }

    public void addItem(String icon, String title, String desc) {
        MarketListViewItem item = new MarketListViewItem();

        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();


        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.market_listview_item, parent, false);
        }


        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.icon);
        TextView titleTextView = (TextView) convertView.findViewById(R.id.name);
        TextView descTextView = (TextView) convertView.findViewById(R.id.stamp_cnt);


        MarketListViewItem listViewItem = listViewItemList.get(pos);

        storage = FirebaseStorage.getInstance();
        storageRef_profile = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/"+listViewItem.getIcon());

        //iconImageView.((Drawable) listViewItem.getIcon());
        titleTextView.setText(listViewItem.getTitle());
        descTextView.setText(listViewItem.getDesc());
        GlideApp.with(context).load(storageRef_profile).into(iconImageView);

        return convertView;
    }









}
