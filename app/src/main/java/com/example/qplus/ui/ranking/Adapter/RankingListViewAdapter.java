package com.example.qplus.ui.ranking.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.qplus.GlideApp;
import com.example.qplus.R;
import com.example.qplus.ui.ranking.RankingListViewItem;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class RankingListViewAdapter  extends BaseAdapter {

    FirebaseStorage storage;
    StorageReference storageRef_profile;
    private ImageView iv_profile;
    private TextView tv_name;
    private TextView tv_stamp;
    private TextView tv_rank;
    private TextView tv_grade;
    Context context;

    private ArrayList<RankingListViewItem> listViewItemList = new ArrayList<>();

    public RankingListViewAdapter(Context context) {
            this.context = context;
    }

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

    public void addItem(String rank, String profileuri, String name, Integer stamp, String grade) {
        RankingListViewItem item = new RankingListViewItem();

        item.setRank(rank);
        item.setProfileUri(profileuri);
        item.setName(name);
        item.setStamp(stamp);
        item.setGrade(grade);

        listViewItemList.add(item);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ranking_listview_item, parent, false);
        }


        iv_profile = view.findViewById(R.id.iv_user_profile);
        tv_name = view.findViewById(R.id.tv_name);
        tv_stamp = view.findViewById(R.id.tv_stamp);
        tv_rank = view.findViewById(R.id.tv_rank);
        tv_grade = view.findViewById(R.id.tv_grade);

        RankingListViewItem listViewItem = listViewItemList.get(pos);

        storage = FirebaseStorage.getInstance();
        storageRef_profile = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/"+listViewItemList.get(pos).getProfileUri());

        if(listViewItem.getGrade().equals("브론즈")){
            tv_grade.setTextColor(Color.parseColor("#662500"));
        }else if(listViewItem.getGrade().equals("실버")){
            tv_grade.setTextColor(Color.parseColor("#a6a6a6"));
        }else if(listViewItem.getGrade().equals("골드")){
            tv_grade.setTextColor(Color.parseColor("#f2cb61"));
        }else if(listViewItem.getGrade().equals("플래티넘")){
            tv_grade.setTextColor(Color.parseColor("#0055ff"));
        }else if(listViewItem.getGrade().equals("마스터")){
            tv_grade.setTextColor(Color.parseColor("#ff2211"));
        }
        tv_grade.setText(listViewItem.getGrade());
        tv_name.setText(listViewItem.getName());
        tv_stamp.setText(listViewItem.getStamp()+ " 개 ");
        tv_rank.setText(listViewItem.getRank());
        GlideApp.with(context).load(storageRef_profile).into(iv_profile);

        return view;
    }

}
