package com.example.qplus.ui.market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.qplus.R;
import com.example.qplus.ui.home.StampStatusActivity;

public class MarketBuyCompleteActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView iv_cancle;
    TextView product_name;
    Button see_stampstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_complete);

        product_name = findViewById(R.id.product_name);

        // 툴바 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //마이스탬프 현황 보기(스탬프 사용으로 남은 개수를 stampstatus 페이지에 Intent로 전달)
        see_stampstatus = findViewById(R.id.see_stampstatus);
        see_stampstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StampStatusActivity.class);
                intent.putExtra("textcnt", "0개");
                startActivity(intent);
                finish();
            }
        });

        // 상품이름 넣기
        Intent intent = getIntent();
        String p_name = intent.getStringExtra("p_name");
        product_name.setText(p_name);

        iv_cancle = findViewById(R.id.iv_cancle);
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
