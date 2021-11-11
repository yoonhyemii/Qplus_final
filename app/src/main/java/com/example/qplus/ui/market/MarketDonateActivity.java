package com.example.qplus.ui.market;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qplus.CouponActivity;
import com.example.qplus.R;

public class MarketDonateActivity extends AppCompatActivity {

    ImageView gohome;
    Button btn_donate;
    EditText et_donate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_donate);


        //이동
        gohome = findViewById(R.id.gohome);

        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //기부하기 버튼 클릭

        btn_donate = findViewById(R.id.btn_donate);
        et_donate = findViewById(R.id.et_donate);

        btn_donate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String donate = et_donate.getText().toString();
                Toast.makeText(getApplicationContext(), donate + "원 기부가 완료되었습니다.", Toast.LENGTH_LONG).show();
                Log.e("기부 액티비티", donate + " 원 기부 성공");
                finish();
            }
        });
    }
}
