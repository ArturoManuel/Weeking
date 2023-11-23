package com.example.weeking.workers.adaptador;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.weeking.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class GaleriaFotosAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> imageUrls;
    LayoutInflater layoutInflater;
    /*public int[] imageArray={
            R.drawable.im1,
            R.drawable.im2,
            R.drawable.im3,
            R.drawable.im4,
            R.drawable.im5,
    };*/

    public GaleriaFotosAdapter(Context mContext, List<String> imageUrls) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls != null ? imageUrls.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return imageUrls != null && position < imageUrls.size() ? imageUrls.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (layoutInflater == null){
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.adapta_img_galeria, null);
        }
        ImageView gridImage = convertView.findViewById(R.id.gridImage);
        Glide.with(mContext).load(imageUrls.get(position)).into(gridImage);

        return convertView;

    }
}


