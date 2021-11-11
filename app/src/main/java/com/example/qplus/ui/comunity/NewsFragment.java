package com.example.qplus.ui.comunity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.qplus.R;
import com.example.qplus.ui.market.MarketListBuyActivity;
import com.example.qplus.ui.market.MarketListViewItem;

public class NewsFragment extends Fragment implements AdapterView.OnItemClickListener {

    ImageView main_news;

    private String[] NewsLinkArray = {"http://m.hani.co.kr/arti/society/environment/954997.html",
            "https://www.google.co.kr/amp/s/m.yna.co.kr/amp/view/AKR20201111052400017",
            "https://www.google.co.kr/amp/s/imnews.imbc.com/replay/2020/nwdesk/article/5929161_32525.html",
            "https://www.google.co.kr/amp/s/m.mk.co.kr/news/business/view-amp/2020/11/1170942/",
            "http://www.newscape.co.kr/m/view.php?idx=55799",
            "http://www.greenpostkorea.co.kr/news/articleView.html?idxno=123158"};

    String newsUrl;
    private ListView listview;
    private CommunityListViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_community_news, container, false);

        // Adapter 생성
        adapter = new CommunityListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = root.findViewById(R.id.community_listview);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.news1),
                "플라스틱 80% 감축해도 2040년 7억t 오염",
                "플라스틱 소비와 쓰레기 줄이기에 국제적으로 \n" +
                        "즉각적인 공동행동에 나서면 20년 동안 " +
                        "\n플라스틱 오염률이 현재 상태보다 80% 가까이" +
                        "\n줄어들 것이라는 분석이 나왔다.\n");
        // 두 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.news2),
                "일회용 플라스틱 컵 줄이기에 정부 기업 나서다",
                "정부, 지자체, 커피 전문점, 기업 등이 플라스틱컵 사용을 " +
                        "\n줄이기 위해 민관 연합체를 결성했다.");
        // 세 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.news3),
                "자고 나면 새로 솟는 '쓰레기산' 1년 새 '100개'",
                "외신에까지 소개돼서 큰 망신을 샀던 경북 의성의 " +
                        "\n불법 쓰레기산입니다. 그런데 그 이후에도 이런 " +
                        "\n쓰레기산이 전국에서 새로 100곳 넘게 생겨났습니다. ");
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.news4),
                "[진짜인가요] \"코로나19 이후 \n일회용 플라스틱 사용량? 최악이다",
                "이렇게까지 오래갈 줄은 누가 예상이나 했을까. " +
                        "\n신종 코로나바이러스(코로나19) 감염증 얘기다...");
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.news5),
                "환경부, ‘플라스틱 줄이기’ 캠페인… \n실생활서 지구 온난화 막는다",
                "국민권익위원회(이하 국민권익위)와 환경부는 " +
                        "\n 11월 23일부터 12월 13일까지 국민생각함에서... ");
        adapter.addItem(ContextCompat.getDrawable(getContext(), R.drawable.news6),
                "[줄여야 산다 일회용품 ①] 코로나19 속 \n‘일회용’의 습격...위생과 환경 균형잡기",
                "코로나19 위생 조치 속, 일회용품 사용 규제 완화 \n" +
                        "‘잘 하고 있었는데’...일회용 사용 권하는 세계...");
        adapter.notifyDataSetChanged(); //어댑터의 변경을 알림

        listview.setOnItemClickListener(this);

         main_news = root.findViewById(R.id.main_news);
         main_news.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(Intent.ACTION_VIEW,
                         Uri.parse("https://www.greenpeace.org/korea/update/6707/blog-plastic-10-tips-to-go-plastic-zero/"));
                 startActivity(intent);
             }
         });
        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    // 클릭된 아이템의 포지션을 이용해 어댑터뷰에서 아이템을 꺼내온다.
        newsUrl = parent.getItemAtPosition(position).toString();
        // 클릭된 아이템의 포지션을 이용해 스트링어레이에서 아이템을 꺼내온다.
        newsUrl = NewsLinkArray[position];
        Uri newsUri = Uri.parse(newsUrl);

        Intent intent = new Intent(Intent.ACTION_VIEW, newsUri);
        startActivity(intent);
    }
}