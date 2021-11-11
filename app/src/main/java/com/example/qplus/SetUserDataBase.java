package com.example.qplus;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetUserDataBase {
    FirebaseFirestore firebaseFirestore;
    Context context;

    private final String TAG = SignActivity.class.getSimpleName();


    public SetUserDataBase(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void SetUserSignDatabase(String uid, String id, String pass, String name, String phonenum, String message, Integer stamp, String qrimg, String profileimg, Integer post_count,  ArrayList<String> couponList) {

        Map<String, Object> setmapuserimg = new HashMap<>();

        setmapuserimg.put("Uid", uid);
        setmapuserimg.put("Id", id);
        setmapuserimg.put("Pass", pass);
        setmapuserimg.put("Name", name);
        setmapuserimg.put("Phonenum", phonenum);
        setmapuserimg.put("Message", message);
        setmapuserimg.put("StampCount", stamp);
        setmapuserimg.put("Qrimg", qrimg);
        setmapuserimg.put("Profile_img", profileimg);
        setmapuserimg.put("PostCount", post_count);
        setmapuserimg.put("CouponList", couponList);


        firebaseFirestore.collection("User").document(uid)
                .set(setmapuserimg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "성공");
                    }
                });

    }

}