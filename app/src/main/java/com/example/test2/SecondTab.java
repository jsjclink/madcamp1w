package com.example.test2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class SecondTab extends Fragment {
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
    private ArrayList<Uri> pictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Picture Uri Array Initialization
        pictures = new ArrayList<>();
        for (Integer i : mThumbIds) {
            pictures.add(Uri.parse("android.resource://com.example.test2/" + i));
        }

        View v = inflater.inflate(R.layout.second_tab, container, false);

        RecyclerView secondTabRV = v.findViewById(R.id.Tab2RV);
        secondTabRV.setAdapter(new SecondTabRVAdapter());
        secondTabRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        // Setting floating action button to fetch image from gallery
        ActivityResultLauncher<String> mGetContent =
                registerForActivityResult(new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri uri) {
                        pictures.add(uri);
                        secondTabRV.requestLayout();
                    }
                });
        FloatingActionButton tab2AddPicture = v.findViewById(R.id.Tab2AddPictureFB);
        tab2AddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGetContent.launch("image/*");
            }
        });

        return v;
    }

    public static SecondTab newInstance(String text) {
        SecondTab f = new SecondTab();
        return f;
    }

    private class SecondTabRVAdapter
            extends RecyclerView.Adapter<SecondTab.SecondTabRVAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView picture;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                picture = itemView.findViewById(R.id.phoneNumberDetailPictureIV);
            }
        }

        @NonNull
        @Override
        public SecondTab.SecondTabRVAdapter.ViewHolder
        onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.phone_number_detail_picture_cell,
                    parent, false);
            return new SecondTab.SecondTabRVAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(
                @NonNull SecondTab.SecondTabRVAdapter.ViewHolder holder,
                int position) {
            holder.picture.setImageURI(pictures.get(position));
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), GalleryDetailActivity.class);
                    int position = holder.getLayoutPosition();
                    intent.putExtra("pictureUri", pictures.get(position).toString());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return pictures.size();
        }
    }
}