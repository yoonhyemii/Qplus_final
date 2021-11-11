package com.example.qplus.ui.comunity;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.qplus.GetPostDataBase;
import com.example.qplus.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class SortingFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton btn_gallery;
    ImageView iv_back;
    Context context;

    //데이터베이스
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    RecycleAdapter recycleAdapter;
    ArrayList<QueryDocumentSnapshot> sendDocument;
    ArrayList<String> getPostNumList;
    ArrayList<String> getNameList ;
    ArrayList<Uri> getUriList ;

    SwipeRefreshLayout swipeRefreshLayout;

    private static final int GALLERY_IMG = 10;
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_OK = -1;
    private Intent intent;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_commnunity_sorting, container, false);
        context = getContext();

        //데이터베이스
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        mRecyclerView = root.findViewById(R.id.community_listview);

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);

        recycleAdapter = new RecycleAdapter(context);
        mRecyclerView.setAdapter(recycleAdapter);

        sendDocument = new ArrayList<>();
        getPostNumList = new ArrayList<>();
        getNameList = new ArrayList<>();
        getUriList = new ArrayList<>();
        //getLetterList = new ArrayList<>();

        swipeRefreshLayout = root.findViewById(R.id.view30);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                volunteerrefresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_green_light
        );


        // 데이터베이스 get, 어댑터 등록
        GetPostDataBase getPostDataBase = new GetPostDataBase(context);
        getPostDataBase.get_SorPostDataBase(recycleAdapter);

       /* //툴바
        include = root.findViewById(R.id.toolbar);
        toolbar = include.findViewById(R.id.toolbar);
        MainActivity activity = (MainActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setTitle("");*/

        btn_gallery = root.findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uploadimg();
            }
        });


        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case GALLERY_IMG: {
                if (resultCode == RESULT_OK) {
                    ArrayList<String> imageList = new ArrayList<>();

                    if (data.getClipData() == null) {
                        Log.i("1. single choice", String.valueOf(data.getData()));
                        imageList.add(String.valueOf(data.getData()));
                    } else {
                        ClipData clipData = data.getClipData();
                        Log.i("clipdata", String.valueOf(clipData.getItemCount()));
                        if (clipData.getItemCount() > 10) {
                            Toast.makeText(getContext(), "사진은 10개까지 선택가능 합니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        // 멀티 선택에서 하나만 선택했을 경우
                        else if (clipData.getItemCount() == 1) {
                            String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                            Log.i("2. clipdata choice", String.valueOf(clipData.getItemAt(0).getUri()));
                            Log.i("2. single choice", clipData.getItemAt(0).getUri().getPath());
                            imageList.add(dataStr);

                        } else if (clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                            for (int i = 0; i < clipData.getItemCount(); i++) {
                                Log.i("3. single choice", String.valueOf(clipData.getItemAt(i).getUri()));
                                imageList.add(String.valueOf(clipData.getItemAt(i).getUri()));
                            }
                        }
                    }

                    Intent intent = new Intent(getContext(), SortingUploadActivity.class);
                    intent.putExtra("GALLERY_IMG", 100);
                    intent.putStringArrayListExtra("GALLERY_URI", imageList);
                    startActivity(intent);
                    //navView.setSelectedItemId(R.id.navigation_home);

                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(getContext(), "사진 선택 취소", Toast.LENGTH_LONG).show();
                    //navView.setSelectedItemId(R.id.navigation_home);
                }
            }
            break;
        }
    }

    public void Uploadimg() {
        intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, 10);
    }

    public void volunteerrefresh(){
        recycleAdapter.notifyDataSetChanged();
        /*assert getFragmentManager() != null;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.detach(this).attach(this).commit();*/
    }
}
