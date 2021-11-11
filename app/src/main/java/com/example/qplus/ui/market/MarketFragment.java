package com.example.qplus.ui.market;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.qplus.GlideApp;
import com.example.qplus.MainActivity;
import com.example.qplus.R;
import com.example.qplus.SetUserDataBase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;


public class MarketFragment extends Fragment {

    Toolbar toolbar;
    View include;
    ImageView iv_donate;
    ImageView iv_profile;
    ImageView iv_store;
    ImageView iv_cafe;
    ImageView iv_bakery;
    ImageView iv_productArray[] = new ImageView[4];
    TextView tv_name;
    TextView tv_stampcnt;
    Context context;

    ViewPager viewPager;
    CircleIndicator circleIndicator;

    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_market, container, false);

        context = getContext();

        //툴바
        include = root.findViewById(R.id.toolbar);
        toolbar = include.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");

        iv_donate = root.findViewById(R.id.iv_donate);
        iv_store = root.findViewById(R.id.iv_store);
        iv_cafe = root.findViewById(R.id.iv_cafe);
        iv_bakery = root.findViewById(R.id.iv_bakery);
        iv_profile = root.findViewById(R.id.iv_profile);
        tv_name = root.findViewById(R.id.tv_name);
        tv_stampcnt = root.findViewById(R.id.tv_stampcnt);
        iv_productArray[0] = root.findViewById(R.id.iv_buy1);
        iv_productArray[1] = root.findViewById(R.id.iv_buy2);
        iv_productArray[2] = root.findViewById(R.id.iv_buy3);
        iv_productArray[3] = root.findViewById(R.id.iv_buy4);

        circleIndicator = root.findViewById(R.id.circleindicator);
        viewPager = root.findViewById(R.id.vpPager);

        // 저장해놓을 프래그먼트 개수
        viewPager.setOffscreenPageLimit(3);
        // 상태저장 기능 제거
        viewPager.setSaveEnabled(false);

        //adapter로 뷰페이저 연결
        MyPagerAdapter adapter = new MyPagerAdapter(getFragmentManager());
        FirstAddFragment firstAddFragment = new FirstAddFragment();
        adapter.addItem(firstAddFragment);

        SecondAddFragment secondAddFragment = new SecondAddFragment();
        adapter.addItem(secondAddFragment);

        ThirdAddFragment thirdAddFragment = new ThirdAddFragment();
        adapter.addItem(thirdAddFragment);

        viewPager.setAdapter(adapter);
        // 뷰페이저에 indicator 연결
        circleIndicator.setViewPager(viewPager);

        //이름, 프로필사진, 스탬프개수 가져오기
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                tv_name.setText(document.get("Name").toString());
                tv_stampcnt.setText(document.get("StampCount").toString());

                if(document.get("Profile_img") != null) {
                    Log.d("Profile_img",document.get("Profile_img")+" ");
                    storage = FirebaseStorage.getInstance();
                    storageRef = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/"+document.get("Profile_img"));
                    GlideApp.with(context).load(storageRef).into(iv_profile);
            }
            }
        });







        // 각 상품 클릭시 구매창으로 이동
        final int p_image[] = {R.drawable.buy1, R.drawable.buy2, R.drawable.buy3, R.drawable.buy4};
        final String p_name[] = {"아이스 아메리카노", "핫 아메리카노", "CU 1만원 상품권", "베스킨라빈스 파인트"};
        final int p_price[] = {10, 10, 20, 15};
        for (int i = 0; i < p_image.length; i++) {
            final int index;
            index = i;

            iv_productArray[index].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    Intent intent = new Intent(getActivity(),MarketBuyActivity.class);
                    Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(), p_image[index]);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    intent.putExtra("product", byteArray);
                    intent.putExtra("name", p_name[index]);
                    intent.putExtra("price", p_price[index]);
                    startActivity(intent);
                }
            });
        }



        //기부하기 이동
        iv_donate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MarketDonateActivity.class);
                startActivity(intent);


               /* final String p_name[] = {"초코에몽", "CU 모바일 상품권 5천원권", "비타 500", "바나나맛 우유", "GS25 모바일 상품권 3천원권", "GS25 모바일 상품권 1만원권"};
                final String p_img[] = {"store1.jpg", "store4.jpg", "store2.jpg", "store3.jpg", "store5.jpg", "store6.jpg"};
                final int p_price[] = {5, 25, 4, 5, 15, 50};

                Map<String, Object> setmapuserimg = new HashMap<>();
                int num = 1;
                for (int i = 0; i < 6; i++) {

                    setmapuserimg.put("GoodsNum-"+num, i+1);
                    setmapuserimg.put("GoodsName-"+num, p_name[i]);
                    setmapuserimg.put("GoodsImg-"+num, "marketlist/"+ p_img[i]);
                    setmapuserimg.put("Stamp-"+num, p_price[i]);
                    num ++;

                }

                firebaseFirestore.collection("MarketList").document("Goods")
                        .set(setmapuserimg)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.e("성공","");
                            }
                        });*/

            }


        });

        //편의점 이
        iv_store.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MarketStoreActivity.class);
                startActivity(intent);
            }
        });

        //카페 이
        iv_cafe.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MarketCafeActivity.class);
                startActivity(intent);
            }
        });

        //베이커리 이동
        iv_bakery.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MarketBakeryActivity.class);
                startActivity(intent);
            }
        });

        return root;

    }


    class MyPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<Fragment> items = new ArrayList<Fragment>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addItem(Fragment item){
            items.add(item);
        }

        @Override
        public Fragment getItem(int position) {
            return items.get(position);
        }

        @Override
        public int getCount() {
            return items.size();
        }
    }
}