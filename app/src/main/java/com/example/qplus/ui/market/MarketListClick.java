package com.example.qplus.ui.market;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.qplus.R;
import com.example.qplus.SignActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class MarketListClick {
    FirebaseFirestore firebaseFirestore;
    Context context;


    public MarketListClick(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void MarketListClickBuy(final MarketListViewAdapter adapter, final ListView listview, final String section) {

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("MarketList").document(section).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        listview.setAdapter(adapter);
                        Log.e("사이즈",document.getData().size() + "");

                        for (int i = 1; i < document.getData().size()/4 + 1; i++) {
                            adapter.addItem(document.get("GoodsImg-" + i).toString(), document.get("GoodsName-" + i).toString(), document.get("Stamp-" + i).toString() + " 개");
                        }

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView parent, View v, int position, long id) {


                                MarketListViewItem item = (MarketListViewItem) parent.getItemAtPosition(position);

                                //Drawable iconDrawable = item.getIcon();
                                String titleStr = item.getTitle();
                                String descStr = item.getDesc();


                                Intent intent = new Intent(context, MarketListBuyActivity.class);

                                //intent.putExtra("icon", iconDrawable);
                                intent.putExtra("title", titleStr);
                                intent.putExtra("desc", descStr);
                                intent.putExtra("section", section);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                                context.startActivity(intent);

                            }
                        });

                    }
                });
    }
}