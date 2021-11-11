package com.example.qplus.ui.comunity;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.qplus.GlideApp;
import com.example.qplus.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class VPGalleryAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> imglist;

    FirebaseStorage storage;
    StorageReference storageRef;

    public VPGalleryAdapter(Context context, ArrayList<String> imageList) {
        this.context = context;
        this.imglist = imageList;
    }

    @Override
    public int getCount() {
        return imglist.size();

    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (View) object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //return super.instantiateItem(container, position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.fragment_community_recycler_viewpager, null);

        ImageView imageView = view.findViewById(R.id.vp_img);
        storage = FirebaseStorage.getInstance();

        storageRef = storage.getReferenceFromUrl("gs://qplus-55fff.appspot.com/" + imglist.get(position));
        Log.d("storageRef", "" + storageRef);

        GlideApp.with(context).load(storageRef).into(imageView);

        container.addView(view);

        return view;
    }

}
