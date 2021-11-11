package com.example.qplus.ui.user;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.qplus.GetUserDataBase;
import com.example.qplus.GlideApp;
import com.example.qplus.LoginActivity;
import com.example.qplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class UserFragment extends Fragment {

    Button btn_info;
    Button btn_pass;
    Button btn_logout;
    TextView tv_name;
    TextView tv_message;
    ImageView iv_profile;
    Intent intent;
    Context context;
    CheckBox cb_locate;

    StorageReference storageRef;
    FirebaseStorage storage;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        context = getContext();
        btn_info = root.findViewById(R.id.btn_info);
        btn_pass = root.findViewById(R.id.btn_pass);
        btn_logout = root.findViewById(R.id.btn_logout);
        tv_name = root.findViewById(R.id.tv_name);
        tv_message = root.findViewById(R.id.tv_message);
        iv_profile = root.findViewById(R.id.iv_profile);
        cb_locate = root.findViewById(R.id.cb_locate);

        //유저이름, 프로필 사진 가져오기
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                tv_name.setText(document.get("Name").toString());
                if (document.get("Message") != null) {
                    tv_message.setText(document.get("Message").toString());
                }
                if (document.get("Profile_img") != null) {
                    Log.d("Profile_img", document.get("Profile_img") + " ");
                    storage = FirebaseStorage.getInstance();
                    storageRef = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/" + document.get("Profile_img"));
                    GlideApp.with(context).load(storageRef).into(iv_profile);
                }
            }
        });

        //개인정보수정 클릭
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), UpdateInfoActivity.class);
                startActivity(intent);
            }
        });

        //비밀번호변경 클릭
        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), UpdatePassActivity.class);
                startActivity(intent);
            }
        });

        //로그아웃
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        permissioncheck();
        cb_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cb_locate.isChecked()) {

                } else {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.fromParts("package", getActivity().getPackageName(), null));
                    startActivityForResult(intent, 0);

                }
            }
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 0:
                // 할일 작성
                permissioncheck();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void permissioncheck() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            // 권한 ㅇㅇ
            cb_locate.setChecked(true);
        } else {
            // 권한 ㄴㄴ
            cb_locate.setChecked(false);
        }
    }

    public void userrefresh(){
        Log.d("fragment","refresh");
        assert getFragmentManager() != null;
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

}