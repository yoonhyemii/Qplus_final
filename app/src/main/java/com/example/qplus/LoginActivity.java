package com.example.qplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private final String TAG = LoginActivity.class.getSimpleName();
    EditText edtext_id;
    EditText edtext_pass;
    TextView text_error;
    TextView text_sign;
    CheckBox checkBox;

    String name;
    FirebaseAuth firebaseAuth;
    Button btn_login;
    FirebaseApp firebaseApp;

    private boolean saveLoginData;
    private SharedPreferences appData;
    private String id;
    private String pwd;
    Intent intent;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        intent = new Intent(this, LoadingPageActivity.class);
        startActivity(intent);

        edtext_id = (EditText)findViewById(R.id.edtext_id);
        edtext_pass = (EditText)findViewById(R.id.edtext_pass);
        text_error = (TextView)findViewById(R.id.text_error);
        text_sign = (TextView)findViewById(R.id.tv_sign);
        btn_login = (Button)findViewById(R.id.btn_login);
        checkBox = (CheckBox)findViewById(R.id.checkBox);

        appData = getSharedPreferences("appData", MODE_PRIVATE);
        load();

       /* if(reset == 100){
            appData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = appData.edit();
            editor.clear();
            editor.commit();
        }else{*/

        if (saveLoginData) {
            edtext_id.setText(id);
            checkBox.setChecked(saveLoginData);
        }
        

        firebaseAuth = FirebaseAuth.getInstance();

        text_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignActivity.class);
                startActivity(intent);
            }
        });





        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtext_id.getText().toString().trim();
                String password = edtext_pass.getText().toString().trim();

                if(email.isEmpty() == false && password.isEmpty() == false) {

                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        save();
                                        if (email.equals("manager@qplus.com")) {
                                            Toast.makeText(LoginActivity.this, " ???????????? ???????????????", Toast.LENGTH_SHORT).show();
                                            intent = new Intent(getApplicationContext(), ManagerMainActivity.class);
                                        } else {
                                            Toast.makeText(LoginActivity.this, edtext_id.getText() + " ??? ???????????????", Toast.LENGTH_SHORT).show();
                                            intent = new Intent(getApplicationContext(), MainActivity.class);
                                        }
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        edtext_id.setText("");
                                        edtext_pass.setText("");
                                        text_error.setText("????????? ?????? ??????????????? ?????? ??????????????????");
                                    }
                                }
                            });
                }else {
                    text_error.setText("????????? ?????? ??????????????? ??????????????????");
                }

            }
        });

    }

    // ???????????? ???????????? ??????
    private void save() {
        // SharedPreferences ??????????????? ?????? ????????? Editor ??????
        SharedPreferences.Editor editor = appData.edit();

        // ???????????????.put??????( ???????????? ??????, ???????????? ??? )
        // ???????????? ????????? ?????? ???????????? ????????????
        editor.putBoolean("SAVE_LOGIN_DATA", checkBox.isChecked());
        editor.putString("ID", edtext_id.getText().toString().trim());
        //editor.putString("PWD", edtext_pass.getText().toString().trim());

        // apply, commit ??? ????????? ????????? ????????? ???????????? ??????
        editor.apply();
    }

    // ???????????? ???????????? ??????
    private void load() {
        // SharedPreferences ??????.get??????( ????????? ??????, ????????? )
        // ????????? ????????? ???????????? ?????? ??? ?????????
        saveLoginData = appData.getBoolean("SAVE_LOGIN_DATA", false);
        id = appData.getString("ID", "");
        //pwd = appData.getString("PWD", "");
    }

}
