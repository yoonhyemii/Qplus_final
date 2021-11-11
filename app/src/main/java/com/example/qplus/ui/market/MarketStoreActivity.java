package com.example.qplus.ui.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.qplus.CouponActivity;
import com.example.qplus.GlideApp;
import com.example.qplus.R;
import com.example.qplus.ui.ranking.Adapter.RankingListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MarketStoreActivity<StoreItem, sss1> extends AppCompatActivity {
    ImageView gohome;

    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    ArrayList<QueryDocumentSnapshot> sendDocument;
    private ListView listView;
    private MarketListViewAdapter listViewAdapter;


    int i = 0;


    ListView listview;
    MarketListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_store);

        //이동
        gohome = findViewById(R.id.gohome);

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        // Adapter 생성
        adapter = new MarketListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.market_listview);

        MarketListClick marketListClick = new MarketListClick(getApplicationContext());
        marketListClick.MarketListClickBuy(adapter,listview,"Goods");




        /*//데이터베이스 가져오기
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sendDocument = new ArrayList<>();
        firebaseFirestore.collection("MarketList").document("Goods")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //문서에 있는 값 모두 가져오기
                        sendDocument.add(0, document);
                    }

                    getImg(sendDocument);
                    Log.e(TAG, "sUid 사이즈 : " + sUid.size());
                } else {
                    Log.d("가져오기 실패", "Error getting documents: ", task.getException());
                }
            }
        });*/




        // db에서 갖고오기
        /*firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("MarketList").document("Goods").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                        // Adapter 생성
                        adapter = new MarketListViewAdapter();

                        // 리스트뷰 참조 및 Adapter달기
                        listview = (ListView) findViewById(R.id.market_listview);
                        listview.setAdapter(adapter);

                        for(int i = 1; i<7; i++ ) {
                            adapter.addItem(document.get("GoodsImg-" + i).toString(), document.get("GoodsName-" + i).toString(), document.get("Stamp-" + i).toString()+" 개");
                        }

                        Log.e("아이템이미지","" + document.get("GoodsImg-"+5).toString() );

                        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView parent, View v, int position, long id) {


                                MarketListViewItem item = (MarketListViewItem) parent.getItemAtPosition(position);

                                //Drawable iconDrawable = item.getIcon();
                                String titleStr = item.getTitle();
                                String descStr = item.getDesc();


                                Intent intent = new Intent(getApplicationContext(), MarketListBuyActivity.class);

                                //intent.putExtra("icon", iconDrawable);
                                intent.putExtra("title", titleStr);
                                intent.putExtra("desc", descStr);

                                startActivity(intent);

                            }
                        });

                    }
                });*/



        /*listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {


                MarketListViewItem item = (MarketListViewItem) parent.getItemAtPosition(position);

                //Drawable iconDrawable = item.getIcon();
                String titleStr = item.getTitle();
                String descStr = item.getDesc();


                Intent intent = new Intent(getApplicationContext(), MarketListBuyActivity.class);

                //intent.putExtra("icon", iconDrawable);
                intent.putExtra("title", titleStr);
                intent.putExtra("desc", descStr);

                startActivity(intent);

            }
        });*/
    }

}
