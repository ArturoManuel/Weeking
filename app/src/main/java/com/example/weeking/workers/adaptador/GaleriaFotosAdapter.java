package com.example.weeking.workers.adaptador;

import android.content.Context;
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
    public int[] imageArray={
            R.drawable.im1,
            R.drawable.im2,
            R.drawable.im3,
            R.drawable.im4,
            R.drawable.im5,
    };

    public GaleriaFotosAdapter(Context mContext, List<String> imageUrls) {
        this.mContext = mContext;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageArray.length;
    }

    @Override
    public Object getItem(int position) {
        return imageArray[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(340, 350));

        // Utiliza Glide para cargar la imagen desde la URL en el ImageView
        Glide.with(mContext)
                .load(imageUrls.get(position))
                .placeholder(R.drawable._36035) // Puedes personalizar el placeholder
                .error(R.drawable._36035) // Puedes personalizar la imagen de error
                .into(imageView);

        return imageView;

    }
}


