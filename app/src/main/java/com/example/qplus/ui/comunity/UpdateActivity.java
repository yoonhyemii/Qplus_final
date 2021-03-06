package com.example.qplus.ui.comunity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qplus.GetPostDataBase;
import com.example.qplus.GlideApp;
import com.example.qplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UpdateActivity extends AppCompatActivity {

    ImageView update_select_img;
    Intent intent;
    String getimg;
    String getname;
    String getTime;
    String getNum;
    String getUid;
    int pos;
    EditText update_et_letter;

    FirebaseStorage storage;
    StorageReference storageRef;

    String updatetext;

    FirebaseFirestore firebaseFirestore;
    ArrayList<QueryDocumentSnapshot> sendDocument;

    View include;
    ImageView iv_cancle;
    ImageView iv_complete;
    CircleImageView ivupdateprofile;
    TextView tvupdatename;
    TextView tvupdatetime;

    FirebaseUser firebaseUser;
    StorageReference storageRefpr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_update);


        update_select_img = findViewById(R.id.update_select_img);
        ivupdateprofile = findViewById(R.id.iv_updateprofile);
        tvupdatename = findViewById(R.id.tv_updatename);
        tvupdatetime = findViewById(R.id.tv_updatetime);

        include = findViewById(R.id.upload_update_toolbar);
        iv_cancle = include.findViewById(R.id.iv_cancle);
        iv_complete = include.findViewById(R.id.iv_complete);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        intent = getIntent();
        getimg = intent.getExtras().getString("setimg").substring(1, intent.getExtras().getString("setimg").length() - 1);
        getname = intent.getExtras().getString("setname");
        getTime = intent.getExtras().getString("settime");
        pos = intent.getExtras().getInt("setpos");
        getNum = intent.getExtras().getString("setNum");
        getUid = intent.getExtras().getString("setUid");

        String[] splitcommant = getimg.split(",");

        //???????????????
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/"+splitcommant[0]);
        GlideApp.with(getApplicationContext()).load(storageRef).into(update_select_img);

        //????????? ??????, ??????
        tvupdatename.setText(getname);
        tvupdatetime.setText(getTime);

        //????????? ?????????
        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String myimguri = documentSnapshot.get("Profile_img").toString();
                storageRefpr2 = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/" + myimguri);
                GlideApp.with(getApplicationContext()).load(storageRefpr2).error(R.drawable.basic_fill).into(ivupdateprofile);
            }
        });

        //??????
        iv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //????????????
        iv_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore = FirebaseFirestore.getInstance();
                sendDocument = new ArrayList<>();
                update_et_letter = findViewById(R.id.update_et_letter);
                updatetext = update_et_letter.getText().toString();

                GetPostDataBase getPostDataBase = new GetPostDataBase(getApplicationContext());
                getPostDataBase.Update_VolPostDataBase(getUid+"_" + getNum,updatetext);
                getPostDataBase.Update_SorPostDataBase(getUid+"_" + getNum,updatetext);
                finish();


            }
        });


    }

}
