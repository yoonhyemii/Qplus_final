package com.example.qplus.ui.user;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qplus.GlideApp;
import com.example.qplus.LoginActivity;
import com.example.qplus.R;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateInfoActivity extends AppCompatActivity {

    private final String TAG = UpdateInfoActivity.class.getSimpleName();
    private static final int GALLERY_CODE = 1;

    long now ;
    Date mDate;
    SimpleDateFormat simpleDate;
    Intent intent;

    ImageView gohome;
    ImageView iv_user_profile;
    Button btn_update;
    Button btn_reset;
    EditText edit_pass;
    private Uri photouri;

    TextView tv_name2;
    TextView tv_msg2;
    TextView tv_phonenum2;
    EditText et_phonenum;
    EditText et_msg;

    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        context = this;
        storage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_update = findViewById(R.id.btn_update);
        btn_reset = findViewById(R.id.btn_reset);
        edit_pass = findViewById(R.id.edit_uppass);
        gohome = findViewById(R.id.gohome);

        tv_name2 = findViewById(R.id.tv_name2);
        tv_phonenum2 = findViewById(R.id.tv_phonenum2);
        tv_msg2 = findViewById(R.id.tv_msg2);

        et_phonenum = findViewById(R.id.et_phonenum);
        et_msg = findViewById(R.id.et_msg);

        iv_user_profile = findViewById(R.id.iv_user_profile);
        iv_user_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(1);
            }
        });

        // ????????? ?????? ??? ?????? ??? ?????? ???????????? ??????
        existProfile();

        //?????? ??????
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //??????
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyProfile();
                if (photouri != null) {
                    storeProfilePhoto(photouri);
                }
                Toast.makeText(UpdateInfoActivity.this, "????????? ?????? ??????", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("User").document(firebaseUser.getUid())
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    //????????? ?????? ??????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_CODE) {
            photouri = data.getData();
        }

        // ????????? uri ??????????????? ??????
        iv_user_profile.setImageURI(photouri);
    }

    // ????????? ?????? ?????? ??????????????? ?????????
    @Override
    protected Dialog onCreateDialog (int id){
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateInfoActivity.this);
        builder.setTitle("????????? ?????? ??????");
        builder.setItems(R.array.user_profile_photo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("which",which +" ");
                switch (which) {
                    case 0:
                        // ????????? ????????????
                        intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                        intent.putExtra("user_profile",100);
                        startActivityForResult(intent, GALLERY_CODE);
                        break;
                    case 1:
                        Toast.makeText(UpdateInfoActivity.this, "????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
        return builder.create();
    }


    // ????????? ?????? ??? ????????? ????????? DB Update
    public void modifyProfile(){
            String message = et_msg.getText().toString();
            String phonenum = et_phonenum.getText().toString();

            DocumentReference docRef = firebaseFirestore.collection("User").document(firebaseUser.getUid());
                    docRef.update("Message", message,"Phonenum", phonenum)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.e("???????????? ??????", "??????");
                            Toast.makeText(getApplicationContext(), "?????????????????????.", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "DB ?????? ??????" );
                        }
                    });
            }


    // Storage??? ??????
    public void storeProfilePhoto(Uri photouri){

        // ???????????? ????????????
        now = System.currentTimeMillis();
        mDate = new Date(now);
        simpleDate = new SimpleDateFormat("yyyy-MM-dd-hh:mm:ss");
        String getTime = simpleDate.format(mDate);

        // Storage??? ????????? ?????? ??????
        storageRef = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/")
                .child("user_profile/" +  firebaseUser.getUid() + "_" + getTime + ".png");

        storageRef.putFile(photouri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(), "????????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
                intent = new Intent(UpdateInfoActivity.this, UpdateInfoActivity.class);
                finish();
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(getApplicationContext(), "????????? ?????? ?????? ??????", Toast.LENGTH_SHORT).show();
            }
        });

        // DB??? ????????? ?????? ???????????? ??????
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DocumentReference docRef = firebaseFirestore.collection("User").document(firebaseUser.getUid());
        docRef.update("Profile_img" ,"user_profile/" + firebaseUser.getUid() + "_" + getTime + ".png")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d(TAG, "DB ?????? ??????" );
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "DB ?????? ??????" );
                    }
                });

    }


    // ????????? ?????? ??? ?????? ???????????? ????????? ????????????
    public void existProfile(){
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseFirestore.collection("User").document(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                tv_name2.setText(document.get("Name").toString());
                tv_phonenum2.setText(document.get("Phonenum").toString());
                if((document.get("Message") != null)){
                    tv_msg2.setText(document.get("Message").toString());
                }else{
                    tv_msg2.setText(" ");
                }
                if(document.get("Profile_img") != null) {
                    Log.d("Profile_img",document.get("Profile_img")+" ");
                    storage = FirebaseStorage.getInstance();
                    storageRef = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/"+document.get("Profile_img"));
                    GlideApp.with(context).load(storageRef).into(iv_user_profile);
                }
            }
        });
    }


}
