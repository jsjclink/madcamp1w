package com.example.test2;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.util.ArrayList;

public class PhoneNumberDetailActivity extends AppCompatActivity {
    private ArrayList<Uri> pictures;
    private int personalNumber;
    private NameNumberModel nnModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_detail);

        personalNumber = getIntent().getIntExtra("position", 0);
        nnModel = (NameNumberModel) getIntent().getSerializableExtra("nnModel");

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
                intent.setData(Uri.parse(nnModel.getNumber()));
                startActivity(intent);
            }
        });

        pictures.setAdapter(new PhoneNumberDetailPicturesRVAdapter());
        pictures.setLayoutManager(new GridLayoutManager(this, 3));

        // Setting floating action button to fetch image from gallery
        ActivityResultLauncher<Intent> mGetContent =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == Activity.RESULT_OK) {
                                Intent data = result.getData();
                                Uri uri = data.getData();
                                nnModel.getPictures().add(uri.toString());
                                FirstTab.nnModels.get(personalNumber)
                                        .getPictures().add(uri.toString());
                                this.getContentResolver().
                                        takePersistableUriPermission(uri,
                                                Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                pictures.requestLayout();
                            }
                        });

        // Launch mGetContent if floating button clicked
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return nnModel.getPictures().size();
        }
    }
}