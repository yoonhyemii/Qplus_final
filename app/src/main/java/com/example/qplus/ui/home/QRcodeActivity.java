package com.example.qplus.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qplus.GlideApp;
import com.example.qplus.MainActivity;
import com.example.qplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Locale;

public class QRcodeActivity  extends AppCompatActivity {

    private final String TAG = QRcodeActivity.class.getSimpleName();
    Toolbar toolbar;
    ImageView iv_gohome;
    ImageView iv_qrcode;
    TextView tv_name;
    Button button_cancel;
    //Button tem_check;
    FirebaseStorage storage;
    StorageReference storageRefpr;
    FirebaseUser firebaseUser;
    String imgname;
    Intent intent;
    FirebaseFirestore firebaseFirestore;

    int curStamp;
    boolean chenck;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stamp_qrcode);

        chenck = false;
        // 툴바 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv_gohome = findViewById(R.id.gohome);
        iv_qrcode = findViewById(R.id.iv_qrcode);
        tv_name = findViewById(R.id.tv_name);
        button_cancel = findViewById(R.id.button_cancel);
        //tem_check = findViewById(R.id.tem_check);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                tv_name.setText(document.get("Name").toString());
            }
        });

        // 뒤로가기 클릭 시
        iv_gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // 취소버튼 클릭 시
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /*//QR코드 이미지 클릭할 시 (임시)
        tem_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), StampCompleteActivity.class);
                startActivity(intent);
            }
        });*/

        storage = FirebaseStorage.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //qr코드 사진 불러오기
        imgname = "qr/" + firebaseUser.getUid() + "_" + "QRcode" + ".png";
        Log.e("img",""+ imgname);
        storageRefpr = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/" + imgname);
        GlideApp.with(getApplicationContext()).load(storageRefpr).into(iv_qrcode);

        // 남은 시간 알려주는 타이머. 30초 제한 후 재시작
        final TextView tex_cnt = findViewById(R.id.tex_cnt);
        CountDownTimer countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                tex_cnt.setText(String.format(Locale.getDefault(), "%d", millisUntilFinished / 1000L));
            }

            public void onFinish() {
                cancel();
                this.start();
            }
        }.start();

        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                curStamp = Integer.parseInt(document.get("StampCount").toString());
                chenck = true;

                //qr스캔 후 화면이동
                firebaseFirestore.collection("User").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        Log.e(TAG, "onEvent 실행" );

                        if (error != null) {
                            Log.w(TAG, "2 Listen failed.", error);
                            return;
                        }
                        if (curStamp != Integer.parseInt(value.get("StampCount").toString())) {

                            Log.e(TAG, "현재 스탬프: " + curStamp);
                            Log.e(TAG, "바뀐 스탬프: " + Integer.parseInt(value.get("StampCount").toString()));

                            if(chenck == true) {

                                intent = new Intent(getApplicationContext(), StampCompleteActivity.class);
                                startActivity(intent);
                                finish();
                                chenck = false;
//                            ((MainActivity) MainActivity.mainContext).onResume();
                            }

                        }


                    }
                });
            }
        });



    }

}
