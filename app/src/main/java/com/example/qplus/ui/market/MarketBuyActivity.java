package com.example.qplus.ui.market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qplus.GlideApp;
import com.example.qplus.R;
import com.example.qplus.ui.user.UpdateInfoActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MarketBuyActivity extends AppCompatActivity {

    private final String TAG = MarketBuyActivity.class.getSimpleName();

    Toolbar toolbar;
    Button buy_btn;
    ImageView iv_cancle;
    ImageView iv_product;
    TextView product_name;
    TextView product_price;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    Integer stampCnt;
    Integer calStamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_choice);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        // 툴바 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buy_btn = findViewById(R.id.buy_btn);
        iv_cancle = findViewById(R.id.iv_cancle);

        iv_product = findViewById(R.id.iv_product);
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);

        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        byte[] arr = intent.getByteArrayExtra("product");
        Bitmap p_image = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        final String p_name = intent.getStringExtra("name");
        final Integer p_price = intent.getIntExtra("price", 0);
        product_name.setText(p_name);
        product_price.setText( p_price + " 스탬프");
        iv_product.setImageBitmap(p_image);

        buy_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("User").document(firebaseUser.getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                DocumentSnapshot document = task.getResult();
                                stampCnt = Integer.parseInt(document.get("StampCount").toString());
                                calStamp = stampCnt - p_price;
                                if(calStamp < 0){
                                    Toast.makeText(getApplicationContext(), "스탬프가 부족하여 구매할 수 없습니다.",Toast.LENGTH_SHORT).show();
                                    Log.e(TAG,"스탬프가 부족하여 구매할 수 없습니다.");
                                    finish();
                                }else{
                                    DocumentReference docRef = firebaseFirestore.collection("User").document(firebaseUser.getUid());
                                    docRef.update("StampCount", calStamp)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Log.e(TAG, "스탬프 개수 변경 성공");
                                                    Intent intent = new Intent(getApplicationContext(), MarketBuyCompleteActivity.class);
                                                    intent.putExtra("p_name", p_name);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e(TAG, " 스탬프 개수 변경 실패");
                                            finish();
                                        }
                                    });
                                }

                            }
                        });
            }
        });

    }



    /*public void cal_Stamp(int calStamp){
        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        Integer stampCnt;
                        stampCnt = Integer.parseInt(document.get("StampCount").toString());

                    }
                });
    }*/
}
