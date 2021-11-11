package com.example.qplus.ui.comunity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.qplus.GetPostDataBase;
import com.example.qplus.GlideApp;
import com.example.qplus.LoginActivity;
import com.example.qplus.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class RecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<PostInfo> postInfoArrayList = new ArrayList<>();
    private MyViewHolder myViewHolder;
    FirebaseStorage storage;
    StorageReference storageRefpr;
    Context context;

    Intent intent;

    FirebaseUser firebaseUser;
    GetPostDataBase getPostDataBase;


    RecycleAdapter(Context context){
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_commnunity_recyclerview, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        myViewHolder = (MyViewHolder) holder;
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        storage = FirebaseStorage.getInstance();


        //이미지 뷰페이저
        String[] splitimg =  postInfoArrayList.get(position).getImg().split(",");
        ArrayList<String> resimg = new ArrayList<>();
        for(int i=0; i<splitimg.length; i++) {
            resimg.add(splitimg[i].substring(1, 67));
        }
        VPGalleryAdapter vpGalleryAdapter = new VPGalleryAdapter(context, resimg);
        myViewHolder.viewPager.setAdapter(vpGalleryAdapter);
        myViewHolder.circleIndicator.setViewPager(myViewHolder.viewPager);

        myViewHolder.tvname.setText(postInfoArrayList.get(position).getName());
        myViewHolder.tvletter.setText(postInfoArrayList.get(position).getLetter());

        //메뉴 다이얼로그
        myViewHolder.ivmenu.setTag(holder.getAdapterPosition());
        myViewHolder.ivmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(postInfoArrayList.get(position).getUid().equals(firebaseUser.getUid())) {
                    show(view);
                }else{
                    Toast.makeText(view.getContext(), "같은 사용자가아닙니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //프로필사진
        if(postInfoArrayList.get(position).getProfil() != null) {
            storageRefpr = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/" + postInfoArrayList.get(position).getProfil().toString());
            Log.e("proimg",""+postInfoArrayList.get(position).getProfil().toString());
            GlideApp.with(context).load(storageRefpr).error(R.drawable.rankcheck).into(myViewHolder.ivprofile);

        }

        //게시물 시간
        myViewHolder.tvpostcount.setText(postInfoArrayList.get(position).getTime());

        //좋아요
        if(postInfoArrayList.get(position).getCount() != -1){
            myViewHolder.ivlike.setImageResource(R.drawable.basic_fill);
        }else{
            myViewHolder.ivlike.setImageResource(R.drawable.basic);
        }
        if (postInfoArrayList.get(position).getSize() > 0) {
            myViewHolder.tvlike.setText("적립완료");
        } else {
            myViewHolder.tvlike.setText("");
        }

    }



    //다이얼로그
    public void show(final View view){

        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        final int pos = (int) view.getTag();

        builder.setItems(R.array.community_post, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("which",which +" ");
                switch (which) {
                    case 0:
                        notifyItemRemoved(pos);
                        GetPostDataBase getPostDataBase = new GetPostDataBase(view.getContext());
                        getPostDataBase.Remove_VolPostDataBase(pos,postInfoArrayList);
                        getPostDataBase.Remove_SorPostDataBase(pos,postInfoArrayList);
                        Toast.makeText(view.getContext(), "삭제" + postInfoArrayList.get(pos).getLetter(), Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(view.getContext(), "수정", Toast.LENGTH_SHORT).show();

                        intent = new Intent(context, UpdateActivity.class);
                        intent.putExtra("setimg",postInfoArrayList.get(pos).getImg());
                        intent.putExtra("setname",postInfoArrayList.get(pos).getName());
                        intent.putExtra("settime",postInfoArrayList.get(pos).getTime());
                        intent.putExtra("setpos",pos);
                        intent.putExtra("setUid",postInfoArrayList.get(pos).getUid());
                        intent.putExtra("setNum",postInfoArrayList.get(pos).getNum());
                        context.startActivity(intent);
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public int getItemCount() {
        return postInfoArrayList.size();
    }

    /*  public void additem(PostInfo postInfo){

          postInfoArrayList.add(postInfo);

      }
  */
    public void updateAllData(ArrayList<PostInfo> postInfoArrayList) {
        if (this.postInfoArrayList != null || this.postInfoArrayList.size() > 0) {
            this.postInfoArrayList.clear();
            this.postInfoArrayList = null;
            this.postInfoArrayList = new ArrayList<>();
        }
        this.postInfoArrayList = postInfoArrayList;
        notifyDataSetChanged();
    }

    public void updateData(PostInfo postInfo) {

        this.postInfoArrayList.add(postInfo);

        notifyDataSetChanged();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivlike;
        TextView tvlike;
        ImageView ivPicture;
        ImageView ivmenu;
        TextView tvname;
        TextView tvletter;
        ImageView ivprofile;
        //ImageView ivbubble;
        CircleIndicator circleIndicator;
        //TextView tvcommantcount;
        TextView tvpostcount;


        ViewPager viewPager;
        Intent intent;


        MyViewHolder(View view) {
            super(view);
            circleIndicator = view.findViewById(R.id.indicator);
            tvname = view.findViewById(R.id.tv_name);
            tvletter = view.findViewById(R.id.tv_letter);
            ivmenu = view.findViewById(R.id.iv_menu);
            ivlike = view.findViewById(R.id.img_like);
            tvlike = view.findViewById(R.id.tv_like);
            ivprofile = view.findViewById(R.id.iv_profile);
            //ivbubble = view.findViewById(R.id.img_bubble);
            viewPager = view.findViewById(R.id.recycler_viewpager);
            //tvcommantcount = view.findViewById(R.id.tv_commantcount);
            tvpostcount = view.findViewById(R.id.tv_postcount);

            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                //좋아요
                ivlike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int pos = getAdapterPosition();
                        String user = firebaseUser.getUid();

                        if(firebaseUser.getUid().equals("B39RNGdiEWWEzNcUg3AOEx5ouNk2")){
                            if (postInfoArrayList.get(pos).getCount() != -1) {
                                ivlike.setImageResource(R.drawable.basic);
                                getPostDataBase = new GetPostDataBase(view.getContext());

                                getPostDataBase.unlikeclick(pos, user);
                                postInfoArrayList.get(pos).setCount(-1);
                                postInfoArrayList.get(pos).setSize(postInfoArrayList.get(pos).getSize() - 1);
                            } else {
                                ivlike.setImageResource(R.drawable.basic_fill);
                                getPostDataBase = new GetPostDataBase(view.getContext());
                                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                                String client = postInfoArrayList.get(pos).getUid();
                                getPostDataBase.likeclick(pos, user,client);
                                postInfoArrayList.get(pos).setCount(0);
                                postInfoArrayList.get(pos).setSize(postInfoArrayList.get(pos).getSize() + 1);
                            }
                        }else{
                            Toast.makeText(context, " 관리자가 아닙니다.", Toast.LENGTH_SHORT).show();
                        }

                        //Log.e("getCount", postInfoArrayList.get(pos).getCount() + " ");
                        /*if (postInfoArrayList.get(pos).getCount() != -1) {
                            ivlike.setImageResource(R.drawable.basic);
                            getPostDataBase = new GetPostDataBase(view.getContext());
                            getPostDataBase.unlikeclick(pos, user);
                            postInfoArrayList.get(pos).setCount(-1);
                            postInfoArrayList.get(pos).setSize(postInfoArrayList.get(pos).getSize() - 1);
                        } else {
                            ivlike.setImageResource(R.drawable.basic_fill);
                            getPostDataBase = new GetPostDataBase(view.getContext());
                            firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            getPostDataBase.likeclick(pos, user);
                            postInfoArrayList.get(pos).setCount(0);
                            postInfoArrayList.get(pos).setSize(postInfoArrayList.get(pos).getSize() + 1);
                        }*/
                   /* String likeText = postInfoArrayList.get(pos).getSize() + "좋아요";
                    tvlike.setText(likeText);*/
                    }
                });

        }
    }
}
