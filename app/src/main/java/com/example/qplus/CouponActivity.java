package com.example.qplus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class CouponActivity extends AppCompatActivity {

    ImageView gohome;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ListView listview;
    CouponListViewAdapter adapter;
    TextView textView;

    ArrayList<String> titlelist;
    ArrayList<String> selist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        //이동
        gohome = findViewById(R.id.gohome);
        textView = findViewById(R.id.textView29);

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titlelist = new ArrayList<>();
        selist = new ArrayList<>();


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                // Adapter 생성
                adapter = new CouponListViewAdapter();

                // 리스트뷰 참조 및 Adapter달기
                listview = (ListView) findViewById(R.id.coupon_list);
                listview.setAdapter(adapter);
                if(documentSnapshot.get("CouponList").toString() != null) {
                    String getCouponList = documentSnapshot.get("CouponList").toString();
                    Log.e("getCouponList",getCouponList);

                    String[] splitCoupon = getCouponList.substring(1, getCouponList.length() - 1).split(",");


                    for (int i = 0; i < splitCoupon.length; i++) {
                        Log.e("splitCoupon[i]" + i,splitCoupon[i]);
                        if (i % 2 == 0) { selist.add(splitCoupon[i]); }
                        if (i % 2 == 1) { titlelist.add(splitCoupon[i]); }
                    }

                    if(selist.size()>0) {
                        for (int i = 0; i < selist.size(); i++) {
                            switch (selist.get(i).trim()){
                                case "Goods":  adapter.addItem("편의점", titlelist.get(i).trim(), "2016"); break;
                                case "Cafe":  adapter.addItem("카페", titlelist.get(i).trim(), "2016"); break;
                                case "Bakery":  adapter.addItem("베이커리", titlelist.get(i).trim(), "2016"); break;
                            }

                        }
                    }
                    textView.setText(selist.size() + " 개의 쿠폰이 있습니다.");
                }else{
                    textView.setText("0 개의 쿠폰이 있습니다.");
                }
            }
        });

    }
}
