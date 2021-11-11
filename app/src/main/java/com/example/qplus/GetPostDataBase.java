package com.example.qplus;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.qplus.ui.comunity.PostInfo;
import com.example.qplus.ui.comunity.RecycleAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class GetPostDataBase {

    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Context context;

    ArrayList<QueryDocumentSnapshot> sendDocument;
    Query query;
    Query query2;
    CollectionReference collectionReference;
    CollectionReference collectionReference2;
    DocumentReference documentReference;

    PostInfo postInfo;


    private static class TIME_MAXIMUM {
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }

    public GetPostDataBase(Context context) {
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("Vol_Post").orderBy("postNum");
        query2 = firebaseFirestore.collection("Sor_Post").orderBy("postNum");
        collectionReference = firebaseFirestore.collection("Vol_Post");
        collectionReference2 = firebaseFirestore.collection("Sor_Post");

    }

    // 봉사활동 추가
    public void  get_VolPostDataBase(final RecycleAdapter recycleAdapter) {
        sendDocument = new ArrayList<>();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) { sendDocument.add(0, document); }
                    int post;
                    for (post = 0; post < sendDocument.size(); post++) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        documentReference = firebaseFirestore.collection("User").document(sendDocument.get(post).get("getUid").toString());
                        if (documentReference != null) {
                            final int finalPost = post;
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.get("Profile_img") != null) {
                                        setPostInfo(document, finalPost, recycleAdapter);
                                    }else {
                                        setPostInfo(document, finalPost, recycleAdapter); }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    // 분리수거 추가
    public void  get_SorPostDataBase(final RecycleAdapter recycleAdapter) {
        sendDocument = new ArrayList<>();

        query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) { sendDocument.add(0, document); }
                    int post;
                    for (post = 0; post < sendDocument.size(); post++) {
                        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        documentReference = firebaseFirestore.collection("User").document(sendDocument.get(post).get("getUid").toString());
                        if (documentReference != null) {
                            final int finalPost = post;
                            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.get("Profile_img") != null) {
                                        setPostInfo(document, finalPost, recycleAdapter);
                                    }else {
                                        setPostInfo(document, finalPost, recycleAdapter); }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public void setPostInfo(DocumentSnapshot document, int finalPost, RecycleAdapter recycleAdapter) {
        postInfo = new PostInfo();
        long time = Long.parseLong(sendDocument.get(finalPost).get("PostTime").toString());
        long curTime = System.currentTimeMillis();
        long diffTime = (curTime - time) / 1000;
        String msg = null;

        if (diffTime < TIME_MAXIMUM.SEC) {
            msg = "방금 전";
        } else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN) {
            msg = diffTime + "분 전";
        } else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR) {
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY) {
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH) {
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        if(document.get("Profile_img")!= null) {
            postInfo.setProfil(Uri.parse(document.get("Profile_img").toString()));
        }
        if (document.get("Name") != null) {
            postInfo.setName(document.get("Name").toString());
        }
        postInfo.setNum(sendDocument.get(finalPost).get("postNum").toString());
        postInfo.setUid(sendDocument.get(finalPost).get("getUid").toString());
        postInfo.setLetter(sendDocument.get(finalPost).get("letter").toString());
        postInfo.setImg(sendDocument.get(finalPost).get("getImgUri").toString());
        postInfo.setCount(sendDocument.get(finalPost).get("likeList").toString().indexOf(firebaseUser.getUid()));
        postInfo.setSize(sendDocument.get(finalPost).get("likeList").toString().length() / 30);
        postInfo.setTime(msg);
        recycleAdapter.updateData(postInfo);
    }

    //게시물 삭제
    public void Remove_VolPostDataBase(final int position, final ArrayList<PostInfo> postInfoArrayList) {

        sendDocument = new ArrayList<>();

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) { sendDocument.add(0, document); }
                    String getId = sendDocument.get(position).getId();
                    String postNum = sendDocument.get(position).get("postNum").toString();

                    if (postInfoArrayList.get(position).getNum().equals(postNum)) {
                        collectionReference.document(getId).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e("TAG", "DocumentSnapshot successfully deleted!" + postInfoArrayList.get(position).getImg());
                                        ((MainActivity) MainActivity.mainContext).onResume();
                                    }
                                });
                    }

                    firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseUser  = firebaseAuth.getCurrentUser();
                    firebaseFirestore.collection("User").document(firebaseUser.getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    int post_count = Integer.parseInt(document.get("PostCount").toString()) - 1;
                                    DocumentReference docRef = firebaseFirestore.collection("User").document(document.get("Uid").toString());
                                    docRef.update("PostCount", post_count)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });
                                }
                            });
                }
            }
        });
    }

    public void Remove_SorPostDataBase(final int position, final ArrayList<PostInfo> postInfoArrayList) {

        sendDocument = new ArrayList<>();

        query2.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) { sendDocument.add(0, document); }
                    String getId = sendDocument.get(position).getId();
                    String postNum = sendDocument.get(position).get("postNum").toString();

                    if (postInfoArrayList.get(position).getNum().equals(postNum)) {
                        collectionReference2.document(getId)
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.e("TAG", "DocumentSnapshot successfully deleted!" + postInfoArrayList.get(position).getImg());
                                        ((MainActivity) MainActivity.mainContext).onResume();
                                    }
                                });
                    }
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseAuth = FirebaseAuth.getInstance();
                    firebaseUser  = firebaseAuth.getCurrentUser();
                    firebaseFirestore.collection("User").document(firebaseUser.getUid()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    DocumentSnapshot document = task.getResult();
                                    int post_count = Integer.parseInt(document.get("PostCount").toString()) - 1;
                                    DocumentReference docRef = firebaseFirestore.collection("User").document(document.get("Uid").toString());
                                    docRef.update("PostCount", post_count)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                }
                                            });
                                }
                            });
                }
            }
        });

    }


    //게시물 수정 업데이트
    public void Update_VolPostDataBase(String getId, final String updatetext) {
        sendDocument = new ArrayList<>();

        collectionReference.document(getId)
                .update("letter", updatetext)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("업데이트 성공", "성공");
                        ((MainActivity) MainActivity.mainContext).onResume();

                    }
                });
    }

    //게시물 수정 업데이트
    public void Update_SorPostDataBase(String getId, final String updatetext) {
        sendDocument = new ArrayList<>();

        collectionReference2.document(getId)
                .update("letter", updatetext)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("업데이트 성공", "성공");
                        ((MainActivity) MainActivity.mainContext).onResume();

                    }
                });
    }

    // 적립
    public void likeclick(final int position, final String user, final String client) {
        sendDocument = new ArrayList<>();
        firebaseFirestore.collection("User").document(client).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                Integer upstamp = Integer.parseInt(document.get("StampCount").toString())+1;
                UpdataStamp(client,upstamp);
                Toast.makeText(context, "성공" + upstamp, Toast.LENGTH_LONG).show();
            }
        });
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) { sendDocument.add(0, document); }
                    String getId = sendDocument.get(position).getId();
                    collectionReference.document(getId)
                            .update("likeList", FieldValue.arrayUnion(user))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("업데이트 성공", "성공");
                                }
                            });
                }
            }

        });
    }

    public void UpdataStamp(String getId, final int updatestamp) {

        firebaseFirestore.collection("User").document(getId)
                .update("StampCount", updatestamp)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("업데이트 성공", "성공");

                    }
                });
    }

    public void unlikeclick(final int position, final String user) {
        sendDocument = new ArrayList<>();
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) { sendDocument.add(0, document); }
                    String getId = sendDocument.get(position).getId();
                    collectionReference.document(getId)
                            .update("likeList", FieldValue.arrayRemove(user))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("업데이트 성공", "성공");
                                }
                            });
                }
            }

        });
    }
}
