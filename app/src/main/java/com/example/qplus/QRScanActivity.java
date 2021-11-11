package com.example.qplus;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class QRScanActivity  extends AppCompatActivity {

    private IntentIntegrator qrScan;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);
        //new IntentIntegrator(this).initiateScan();
        qrScan = new IntentIntegrator(this);
        qrScan.setOrientationLocked(false); // default가 세로모드, 휴대폰 방향에 따라 가로, 세로로 자동 변경
        qrScan.initiateScan();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                // todo
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                try {
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection("User").document(result.getContents()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            Integer upstamp = Integer.parseInt(document.get("StampCount").toString())+1;
                            UpdataStamp(result.getContents(),upstamp);
                            Toast.makeText(getApplicationContext(), "성공" + upstamp, Toast.LENGTH_LONG).show();
                        }
                    });
                    Intent intent2 = new Intent(this, ManagerMainActivity.class); //스캔 후 액티비티 이동
                    startActivity(intent2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void UpdataStamp(String getId, final int updatestamp) {

        firebaseFirestore.collection("User").document(getId)
                .update("StampCount", updatestamp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("업데이트 성공", "성공");

                    }
                });
    }

}
