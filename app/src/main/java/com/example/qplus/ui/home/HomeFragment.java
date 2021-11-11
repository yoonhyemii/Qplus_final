package com.example.qplus.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.qplus.CouponActivity;
import com.example.qplus.GlideApp;
import com.example.qplus.MainActivity;
import com.example.qplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class HomeFragment extends Fragment {

    Toolbar toolbar;
    View include;
    ImageView iv_stamp;
    ImageView iv_qrcode;
    Intent intent;
    TextView possess_stamp;
    TextView couponBox, couponBoxtxt;
    String getCouponList;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        include = root.findViewById(R.id.toolbar);

        //툴바
        toolbar = include.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");

        iv_stamp = root.findViewById(R.id.iv_stamp);
        iv_qrcode = root. findViewById(R.id.iv_qrcode);
        possess_stamp = root.findViewById(R.id.possess_stamp);
        couponBox = root.findViewById(R.id.coupon_have);
        couponBoxtxt = root.findViewById(R.id.coupon_have_txt);

        //스탬프갯수 변경
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.get("StampCount") != null) {
                            possess_stamp.setText(document.get("StampCount").toString());
                        }

                    }
                });

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();

                if (documentSnapshot.get("CouponList") != null) {
                    String getCouponList = documentSnapshot.get("CouponList").toString();
                    String[] splitCoupon = getCouponList.substring(1, getCouponList.length() - 1).split(",");

                    couponBox.setText(splitCoupon.length/2 + "");
                } else {
                    couponBox.setText(0 + "");
                }
            }
        });

        iv_stamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), StampStatusActivity.class);
                startActivity(intent);
            }
        });

        iv_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), QRcodeActivity.class);
                startActivity(intent);
            }
        });

        couponBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), CouponActivity.class);
                startActivity(intent);
            }
        });

        couponBoxtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), CouponActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }



    public void homerefresh(){
        Log.d("fragment","refresh");
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }
}