package com.example.qplus;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetPostDataBase {
    FirebaseFirestore firebaseFirestore;
    Context context;
    private final String TAG = SetPostDataBase.class.getSimpleName();


    public SetPostDataBase(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void Set_VolPostDatabase(Long time, String postNum, final String getUid, String name,  ArrayList<String> ImgUri, String letter, ArrayList<String> likelist) {

        Map<String, Object> setmappostimg = new HashMap<>();

        setmappostimg.put("PostTime", time);
        setmappostimg.put("postNum", postNum);
        setmappostimg.put("getUid", getUid);
        setmappostimg.put("name", name);
        setmappostimg.put("getImgUri", ImgUri);
        setmappostimg.put("letter", letter);
        setmappostimg.put("likeList", likelist);


        firebaseFirestore.collection("Vol_Post").document(getUid + "_" + postNum)
                .set(setmappostimg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("성공 : ", "" + FirebaseAuth.getInstance().getCurrentUser());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").document(getUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        int post_count = Integer.parseInt(document.get("PostCount").toString()) + 1;
                        DocumentReference docRef = firebaseFirestore.collection("User").document(getUid);
                        docRef.update(
                                "PostCount", post_count)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "포스트 카운트 증가 성공");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "포스트 카운트 증가 실패");

                                    }
                                });
                    }
                });
    }

    public void Set_SorPostDatabase(Long time, String postNum, final String getUid, String name,  ArrayList<String> ImgUri, String letter, ArrayList<String> likelist/*, ArrayList<String> commant*/){

        Map<String, Object> setmappostimg = new HashMap<>();

        setmappostimg.put("PostTime", time);
        setmappostimg.put("postNum", postNum);
        setmappostimg.put("getUid", getUid);
        setmappostimg.put("name",name);
        setmappostimg.put("getImgUri", ImgUri);
        setmappostimg.put("letter", letter);
        setmappostimg.put("likeList",likelist);


        firebaseFirestore.collection("Sor_Post").document(getUid+ "_" + postNum)
                .set(setmappostimg)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e("성공 : ", "" + FirebaseAuth.getInstance().getCurrentUser());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("User").document(getUid).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        int post_count = Integer.parseInt(document.get("PostCount").toString()) + 1;
                        DocumentReference docRef = firebaseFirestore.collection("User").document(getUid);
                        docRef.update("PostCount", post_count)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "포스트 카운트 증가 성공");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d(TAG, "포스트 카운트 증가 실패");

                                    }
                                });
                    }
                });
    }

}