package com.example.qplus;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class GetUserDataBase {

    FirebaseFirestore firebaseFirestore;
    Context context;

    ArrayList<QueryDocumentSnapshot> sendDocument;
    Query query;
    CollectionReference collectionReference;
    DocumentReference documentReference;

    FirebaseUser firebaseUser;


    public GetUserDataBase(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        collectionReference = firebaseFirestore.collection("User");

    }


    public String getName(final String Uid) {
        final String[] name = new String[1];
        collectionReference.document(Uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                name[0] = document.get("Name").toString();
            }
        });

        return name[0];
    }

}
