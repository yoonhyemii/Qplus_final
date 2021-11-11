package com.example.qplus;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class SignActivity extends AppCompatActivity {

    private final String TAG = SignActivity.class.getSimpleName();

    EditText ed_id;
    EditText ed_pass;
    EditText ed_name;
    EditText ed_phonenum;
    Button btn_signin;
    TextView error_email;
    TextView error_pass;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String text;
    String imgname;
    String profile_img;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ed_id  = (EditText)findViewById(R.id.ed_id);
        ed_pass  = (EditText)findViewById(R.id.ed_pass);
        ed_name  = (EditText)findViewById(R.id.ed_name);
        ed_phonenum  = (EditText)findViewById(R.id.ed_phonenum);
        btn_signin = (Button)findViewById(R.id.btn_signin);
        error_email = (TextView)findViewById(R.id.email_error);
        error_pass = (TextView)findViewById(R.id.pass_error);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = ed_id.getText().toString();
                final String password = ed_pass.getText().toString();
                final String name = ed_name.getText().toString();
                final String phonenum = ed_phonenum.getText().toString();

                if(TextUtils.isEmpty(email)){
                    ed_id.setError("이메일을 입력하세요");
                }if(!TextUtils.isEmpty(email) && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    ed_id.setError("이메일 형식이 아닙니다");
                }if(password.length() < 6) {
                    ed_pass.setError("패스워드는 6자리 이상으로 설정해주세요 ");
                    if (TextUtils.isEmpty(password)) {
                        ed_pass.setError("패스워드를 입력하세요");
                    }
                }if(TextUtils.isEmpty(name)){
                    ed_name.setError("이름을 입력하세요");
                }else{
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                //QR코드
                                text = firebaseAuth.getUid();
                                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                                try {
                                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
                                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);

                                    //bitmap to inputStream
                                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                                    byte[] bitmapdata = bytes.toByteArray();
                                    InputStream inputStream = new ByteArrayInputStream(bitmapdata);

                                    //이미지이름설정
                                    imgname = "qr/" + firebaseAuth.getUid() + "_" + "QRcode" + ".png";

                                    //이미지업로드
                                    storageReference = firebaseStorage.getReferenceFromUrl("gs://qplus-55fff.appspot.com").child(imgname);

                                    storageReference.putStream(inputStream).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                            Log.e(TAG,"업로드 완료");
                                        }
                                    });

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                ArrayList<String> CouponList = new ArrayList<>();
                                SetUserDataBase setUserDataBase = new SetUserDataBase(getApplicationContext());
                                setUserDataBase.SetUserSignDatabase(firebaseAuth.getUid(), email, password, name, phonenum, null,0, imgname, null, 0, CouponList);
                                Log.e(TAG,"회원가입 성공");
                                Toast.makeText(SignActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                                finish();

                            }else{
                                Toast.makeText(SignActivity.this, "회원가입 실패", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });

    }
}
