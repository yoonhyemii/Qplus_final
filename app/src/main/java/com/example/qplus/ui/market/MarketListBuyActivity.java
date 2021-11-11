package com.example.qplus.ui.market;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.qplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

public class MarketListBuyActivity extends Activity {

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    Button mOnbuy;
    TextView title;
    TextView desc;
    String storestamp;
    String storetitle;
    String storesection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //UI 객체생성
        setContentView(R.layout.activity_market_popup);


        Intent intent = getIntent();
        /* ImageView icon = (ImageView) findViewById(R.id.iv_product); */
        title = findViewById(R.id.product_name);
        desc = findViewById(R.id.product_price);
        mOnbuy = findViewById(R.id.mOnbuy);

        storetitle = intent.getStringExtra("title");
        storestamp = intent.getStringExtra("desc");
        storesection = intent.getStringExtra("section");

        title.setText(storetitle);
        desc.setText(storestamp);

        //구매 버튼 클릭
        mOnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        String stamp = documentSnapshot.get("StampCount").toString();
                        String[] array = storestamp.split(" ");

                        final String coupon = storesection + "," +storetitle;

                        int newstamp = Integer.parseInt(stamp) - Integer.parseInt(array[0]);
                        if(Integer.parseInt(stamp) > Integer.parseInt(array[0]) ){

                            firebaseFirestore.collection("User").document(firebaseUser.getUid()).update("StampCount", newstamp).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("업데이트 성공", "성공");
                                    firebaseFirestore.collection("User").document(firebaseUser.getUid()).update("CouponList", FieldValue.arrayUnion(coupon)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast msg = Toast.makeText(getApplicationContext(), "구매가 완료되었습니다." + storetitle, Toast.LENGTH_SHORT);
                                            msg.show();
                                            finish();
                                        }
                                    });
                                }
                            });
                        }else{
                            Toast msg = Toast.makeText(getApplicationContext(), "스탬프 개수가 부족합니다.", Toast.LENGTH_SHORT);
                            msg.show();
                        }
                    }
                });
            }
        });

        //Drawable drawable = getResources().getDrawable(R.drawable.store5);

        //icon.setImageDrawable((Drawable) intent.getExtras().get("image"));



    }


    //취소 버튼 클릭
    public void mOnClose(View v) {
        //데이터 전달하기
        Intent intent = new Intent();
        intent.putExtra("result", "Close Popup");
        setResult(RESULT_OK, intent);

        //액티비티(팝업) 닫기
        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }


}
