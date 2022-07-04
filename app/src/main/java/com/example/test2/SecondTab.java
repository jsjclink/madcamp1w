package com.example.test2;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Locale;

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
    public static ArrayList<Uri> pictures;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Picture Uri Array Initialization
        pictures = new ArrayList<>();
        for (Integer i : mThumbIds) {
            pictures.add(Uri.parse("android.resource://com.example.test2/" + i));
        }

        //OnCheckPermission();

        //실험
        Log.d("stringpath", "HI");
        String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        Log.d("path", path);
        File f = new File(path);

        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".jpg");
            }
        });
        for(File file : files){
            Log.d("uriabsolutepath", file.getAbsolutePath());
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +
                    File.separator + "Pictures" + File.separator + file.getName() + File.separator);
            pictures.add(uri);
        }



        View v = inflater.inflate(R.layout.second_tab, container, false);

        RecyclerView secondTabRV = v.findViewById(R.id.Tab2RV);
        secondTabRV.setAdapter(new SecondTabRVAdapter());
        secondTabRV.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        // Setting floating action button to fetch image from gallery
        ActivityResultLauncher<Intent> mGetContent =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        ClipData clipData = result.getData().getClipData();
                            if (clipData != null) {
                                for (int i = 0; i < clipData.getItemCount(); i++) {
                                    pictures.add(clipData.getItemAt(i).getUri());
                                }
                            } else {
                                Uri uri = result.getData().getData();
                                pictures.add(uri);
                            }
                        secondTabRV.requestLayout();
                    }
                });
        FloatingActionButton tab2AddPicture = v.findViewById(R.id.Tab2AddPictureFB);
        tab2AddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fetchPicture = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                fetchPicture.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                mGetContent.launch(fetchPicture.setType("image/*"));
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((RecyclerView)getView().findViewById(R.id.Tab2RV)).getAdapter().notifyDataSetChanged();

        Log.d("stringpath", "HI");
        String path = Environment.getExternalStorageDirectory().toString() + "/Pictures";
        Log.d("path", path);
        File f = new File(path);

        File[] files = f.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().toLowerCase(Locale.US).endsWith(".jpg");
            }
        });
        for(File file : files){
            Log.d("uriabsolutepath", file.getAbsolutePath());
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +
                    File.separator + "Pictures" + File.separator + file.getName() + File.separator);
            if(!pictures.contains(uri)) pictures.add(uri);
        }

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
    private void OnCheckPermission() {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this, "앱 실행을 위해서는 권한을 설정하세요", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }
}