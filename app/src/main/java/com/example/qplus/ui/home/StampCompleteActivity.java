package com.example.qplus.ui.home;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.qplus.MainActivity;
import com.example.qplus.R;

public class
StampCompleteActivity  extends AppCompatActivity {

    Toolbar toolbar;
    ImageView iv_cancle;
    private ProgressDialog progress;
    Intent intent;
    private ListView listview;
    private MiniListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stampcomplete);

        /*// Adapter 생성
        adapter = new MiniListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.mini_listview);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.store1),
                "초코에몽", "10 스탬프");
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.store2),
                "비타 500", "10 스탬프");
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.store3),
                "빙그레 바나나맛 우유", "10 스탬프");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.store4),
                "CU 모바일 상품권 1만원", "20 스탬프");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.store5),
                "gs25 모바일 상품권 3000원", "20 스탬프");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.store6),
                "gs25 모바일 상품권 1만원", "20 스탬프");
        adapter.notifyDataSetChanged(); //어댑터의 변경을 알림
*/
        // 툴바 적용
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        iv_cancle = findViewById(R.id.iv_cancle);


        // 캔슬 버튼 클릭시
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
               finish();
            }
        });

    }


}
