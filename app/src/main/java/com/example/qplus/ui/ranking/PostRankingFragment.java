package com.example.qplus.ui.ranking;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.qplus.GlideApp;
import com.example.qplus.R;
import com.example.qplus.ui.ranking.Adapter.RankingListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PostRankingFragment extends Fragment {

    private final static String TAG = PostRankingFragment.class.getSimpleName();

    ImageView[] iv_ranking = new ImageView[3];
    TextView[] tv_name = new TextView[3];
    TextView[] tv_stamp = new TextView[3];
    TextView[] tv_grade = new TextView[3];
    FirebaseStorage storage;
    StorageReference storageRef_profile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    ArrayList<QueryDocumentSnapshot> sendDocument;
    private ListView listView;
    private RankingListViewAdapter listViewAdapter;

    ArrayList<String> sUid;
    ArrayList<String> sUri;
    ArrayList<String> sRank;
    ArrayList<String> sName;
    ArrayList<String> sProfileuri;
    ArrayList<String> sGrade;
    ArrayList<Integer> sPostCnt;

    Context context;
    int i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_ranking_post, container, false);

        context = getContext();
        iv_ranking[0] = root.findViewById(R.id.iv_ranking1);
        iv_ranking[1] = root.findViewById(R.id.iv_ranking2);
        iv_ranking[2] = root.findViewById(R.id.iv_ranking3);

        tv_name[0] = root.findViewById(R.id.tv_first_name);
        tv_name[1] = root.findViewById(R.id.tv_second_name);
        tv_name[2] = root.findViewById(R.id.tv_third_name);
        tv_stamp[0] = root.findViewById(R.id.tv_first_stamp);
        tv_stamp[1] = root.findViewById(R.id.tv_second_stamp);
        tv_stamp[2] = root.findViewById(R.id.tv_third_stamp);
        tv_grade[0] = root.findViewById(R.id.tv_first_grade);
        tv_grade[1] = root.findViewById(R.id.tv_second_grade);
        tv_grade[2] = root.findViewById(R.id.tv_third_grade);

        listViewAdapter = new RankingListViewAdapter(getContext());
        listView = root.findViewById(R.id.lv_post);

        //데이터베이스 가져오기
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        sendDocument = new ArrayList<>();
        firebaseFirestore.collection("User")
                .orderBy("PostCount")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //문서에 있는 값 모두 가져오기
                        sendDocument.add(0,document);
                    }

                    getImg(sendDocument);
                    Log.e(TAG, "sUid 사이즈 : " + sUid.size());
                } else {
                    Log.d("가져오기 실패", "Error getting documents: ", task.getException());
                }
            }
        });

        return root;
    }

    //Post 값들 가져와야 함
    public void getImg(final ArrayList<QueryDocumentSnapshot> queryDocumentSnapshots) {

        sRank = new ArrayList<>();
        sName = new ArrayList<>();
        sProfileuri = new ArrayList<>();
        sGrade = new ArrayList<>();
        sPostCnt = new ArrayList<>();
        sUid = new ArrayList<>();
        sUri = new ArrayList<>();
        String profileuri;

        //문서에서 필드 getImgUri에 해당하는 값 getimguri에 넣기
        for (int getImgCount = 0; getImgCount < queryDocumentSnapshots.size(); getImgCount++) {
            String name = queryDocumentSnapshots.get(getImgCount).get("Name").toString();
            sName.add(name);

            if(queryDocumentSnapshots.get(getImgCount).get("Profile_img") != null){
                profileuri = queryDocumentSnapshots.get(getImgCount).get("Profile_img").toString();
                sProfileuri.add(profileuri);
            }else{
                // profileuri = null;
                storage = FirebaseStorage.getInstance();
                profileuri = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/user_profile/Profile.png").toString();
                sProfileuri.add(profileuri);
            }

            Integer postCnt  = Integer.parseInt(queryDocumentSnapshots.get(getImgCount).get("PostCount").toString());
            sPostCnt.add(postCnt);

            if(postCnt < 3){
                sGrade.add("브론즈");
            }else if(postCnt >= 3 && postCnt < 7){
                sGrade.add("실버");
            }else if(postCnt >= 7 && postCnt < 10){
                sGrade.add("골드");
            }else if(postCnt >= 10 && postCnt < 15){
                sGrade.add("플래티넘");
            }else if(postCnt > 15 ){
                sGrade.add("마스터");
            }

            Integer rank = getImgCount + 1;
            sRank.add(rank.toString());

            String uid = queryDocumentSnapshots.get(getImgCount).get("Uid").toString();
            sUid.add(uid);

            sUri.add("");
        }


        firebaseFirestore.collection("User")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        for (int i = 0; i < sUid.size(); i++) {
                            if (document.get("Profile_img") != null) {
                                sUri.set(i, document.get("Profile_img").toString());
                            }
                        }
                    }

                    for(i = 0; i <= 2; i++){
                        if(sName.get(i).equals("manager")){
                            continue;
                        }
                        Log.e(TAG,i +1 +"번째 : " +sProfileuri.get(i));

                        storage = FirebaseStorage.getInstance();
                        storageRef_profile = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/"+sProfileuri.get(i));
                        GlideApp.with(context).load(storageRef_profile).into(iv_ranking[i]);

                        tv_name[i].setText(sName.get(i));
                        tv_grade[i].setText(sGrade.get(i));
                        tv_stamp[i].setText(sPostCnt.get(i).toString());

                        if(sGrade.get(i).equals("브론즈")){
                            tv_grade[i].setTextColor(Color.parseColor("#662500"));
                        }else if(sGrade.get(i).equals("실버")){
                            tv_grade[i].setTextColor(Color.parseColor("#a6a6a6"));
                        }else if(sGrade.get(i).equals("골드")){
                            tv_grade[i].setTextColor(Color.parseColor("#f2cb61"));
                        }else if(sGrade.get(i).equals("플래티넘")){
                            tv_grade[i].setTextColor(Color.parseColor("#0055ff"));
                        }else if(sGrade.get(i).equals("마스터")){
                            tv_grade[i].setTextColor(Color.parseColor("#ff2211"));
                        }

                    }


                    for(int i = 3; i < sName.size(); i++){
                        if(sName.get(i).equals("manager")){
                            continue;
                        }
                        listViewAdapter.addItem(sRank.get(i), sProfileuri.get(i), sName.get(i), sPostCnt.get(i), sGrade.get(i));
                        listView.setAdapter(listViewAdapter);
                    }
                } else {
                    Log.d("가져오기 실패", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}
