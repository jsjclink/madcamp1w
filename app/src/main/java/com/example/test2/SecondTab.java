package com.example.test2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class SecondTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.second_tab, container, false);

        GridView gridview = (GridView) v.findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(getActivity()));

        return v;
    }

    public static SecondTab newInstance(String text) {
        SecondTab f = new SecondTab();
        return f;
    }

    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return mThumbIds.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {  // if it's not recycled, initialize some attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setBackgroundColor(Color.BLACK);
                imageView.setPadding(4, 4, 4, 4);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(mThumbIds[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, GalleryDetailActivity.class);
                    intent.putExtra("name", "drawable://" + mThumbIds[position]);
                    intent.putExtra("picture", mThumbIds[position]);
                    mContext.startActivity(intent);
                }
            });
            return imageView;
        }

        // references to our images
        private Integer[] mThumbIds = {
                R.drawable.sample_1, R.drawable.sample_2,
                R.drawable.sample_3, R.drawable.sample_4,
                R.drawable.sample_5, R.drawable.sample_6,
                R.drawable.sample_7, R.drawable.sample_8,
                R.drawable.sample_9, R.drawable.sample_10,
                R.drawable.sample_11, R.drawable.sample_12,
                R.drawable.sample_13, R.drawable.sample_14,
                R.drawable.sample_15, R.drawable.sample_16,
                R.drawable.sample_17, R.drawable.sample_18,
                R.drawable.sample_19, R.drawable.sample_20
        };
    }
}