package com.example.test2;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

public class PhoneNumberDetailActivity extends AppCompatActivity {
    private NameNumberModel nnModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_detail);

        nnModel = (NameNumberModel) getIntent().getSerializableExtra("nnModel");
        ArrayList<String> prefPictures = getStringArrayPref(this, nnModel.getNumber());
        nnModel.setPictures(prefPictures);

        TextView nameText = findViewById(R.id.DetailName);
        TextView numberText = findViewById(R.id.DetailNumber);
        RecyclerView pictures = findViewById(R.id.PhoneNumberDetailRV);
        FloatingActionButton addPicture = findViewById(R.id.PhoneNumberDetailAddFB);

        nameText.setText(nnModel.getName());
        numberText.setText(nnModel.getNumber());
        numberText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + nnModel.getNumber()));
                startActivity(intent);
            }
        });

        // Recycler View Configuration.
        pictures.setAdapter(new PhoneNumberDetailPicturesRVAdapter());
        pictures.setLayoutManager(new GridLayoutManager(this, 3));

        // Setting floating action button to fetch image from gallery
        ActivityResultLauncher<Intent> mGetContent =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                ClipData clipData = result.getData().getClipData();
                                ContentResolver contentResolver = getContentResolver();
                                Uri uri;
                                if (clipData != null) {
                                    for (int i = 0; i < clipData.getItemCount(); i++) {
                                        uri = clipData.getItemAt(i).getUri();
                                        nnModel.getPictures().add(uri.toString());
                                        contentResolver.takePersistableUriPermission(uri,
                                                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    }
                                } else {
                                    uri = result.getData().getData();
                                    nnModel.getPictures().add(uri.toString());
                                    contentResolver.takePersistableUriPermission(uri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                }
                                setStringArrayPref(this, nnModel.getNumber(),
                                        nnModel.getPictures());
                                pictures.requestLayout();
                            }
                        });

        // Launch mGetContent if floating button clicked
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +
                        File.separator + "Pictures" + File.separator);
                intent.setDataAndType(uri,"*/*");
                mGetContent.launch(intent);
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class PhoneNumberDetailPicturesRVAdapter
            extends RecyclerView.Adapter<PhoneNumberDetailPicturesRVAdapter.ViewHolder> {
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView picture;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                picture = itemView.findViewById(R.id.phoneNumberDetailPictureIV);
            }
        }

        @NonNull
        @Override
        public PhoneNumberDetailActivity.PhoneNumberDetailPicturesRVAdapter.ViewHolder
        onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(PhoneNumberDetailActivity.this);
            View view = inflater.inflate(R.layout.phone_number_detail_picture_cell,
                    parent, false);
            return new PhoneNumberDetailActivity.PhoneNumberDetailPicturesRVAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(
                @NonNull PhoneNumberDetailActivity.PhoneNumberDetailPicturesRVAdapter.ViewHolder holder,
                int position) {
            holder.picture.setImageURI(Uri.parse(nnModel.getPictures().get(position)));
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getBindingAdapterPosition();
                    Intent intent = new Intent(PhoneNumberDetailActivity.this,
                            GalleryDetailActivity.class);
                    intent.putExtra("pictureUri", nnModel.getPictures().get(position));
                    intent.putExtra("number", nnModel.getNumber());
                    startActivity(intent);
                }
            });
            holder.picture.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    AlertDialog.Builder dialog =
                            new AlertDialog.Builder(PhoneNumberDetailActivity.this);
                    dialog.setTitle("삭제");
                    dialog.setMessage("선택하신 이미지를 삭제하시겠습니까?");
                    dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ArrayList<String> pictureList = nnModel.getPictures();
                            int position = holder.getLayoutPosition();
                            pictureList.remove(holder.getLayoutPosition());
                            setStringArrayPref(PhoneNumberDetailActivity.this,
                                    nnModel.getNumber(), pictureList);
                            holder.getBindingAdapter().notifyDataSetChanged();
                        }
                    });
                    dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    dialog.show();
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return nnModel.getPictures().size();
        }
    }

    private void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    private ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> urls = new ArrayList<String>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }
}