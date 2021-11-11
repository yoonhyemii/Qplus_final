package com.example.qplus.ui.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qplus.LoginActivity;
import com.example.qplus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdatePassActivity extends AppCompatActivity {

    ImageView gohome;
    EditText et_curpass;
    EditText et_uppass;
    Button btn_update;

    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pass);

        et_curpass = (EditText)findViewById(R.id.edit_curpass);
        et_uppass = (EditText)findViewById(R.id.edit_uppass);
        btn_update = (Button)findViewById(R.id.btn_update);
        gohome = (ImageView)findViewById(R.id.gohome);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //이동
        gohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("User").document(firebaseUser.getUid()).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();

                       if(document.get("Pass").toString().equals(et_curpass.getText().toString())){
                           firebaseFirestore.collection("User").document(firebaseUser.getUid())
                                   .update("Pass",et_uppass.getText().toString())
                                   .addOnCompleteListener(new OnCompleteListener<Void>() {
                                       @Override
                                       public void onComplete(@NonNull Task<Void> task) {
                                           firebaseUser.updatePassword(et_uppass.getText().toString());
                                           FirebaseAuth.getInstance().signOut();
                                           Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                           startActivity(intent);
                                           finish();
                                           Log.e("업데이트 성공", "성공");
                                       }
                                   });
                       }else{
                           Toast.makeText(UpdatePassActivity.this,"현재 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                       }
                    }
                });


            }
        });

    }

}
