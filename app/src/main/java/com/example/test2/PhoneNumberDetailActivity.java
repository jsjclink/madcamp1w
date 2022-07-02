package com.example.test2;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PhoneNumberDetailActivity extends AppCompatActivity {
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
    private int personalNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_detail);

        String name = getIntent().getStringExtra("name");
        String number = getIntent().getStringExtra("number");
        personalNumber = getIntent().getIntExtra("position", 0);

        TextView nameText = findViewById(R.id.DetailName);
        TextView numberText = findViewById(R.id.DetailNumber);
        RecyclerView pictures = findViewById(R.id.PhoneNumberDetailRV);

        nameText.setText(name);
        numberText.setText(number);

        pictures.setAdapter(new PhoneNumberDetailPicturesRVAdapter());
        pictures.setLayoutManager(new GridLayoutManager(this, 3));

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
        public class ViewHolder extends RecyclerView.ViewHolder{
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
            holder.picture.setImageResource(mThumbIds[personalNumber]);
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PhoneNumberDetailActivity.this,
                            GalleryDetailActivity.class);
                    intent.putExtra("name", "drawable://" + mThumbIds[personalNumber]);
                    intent.putExtra("picture", mThumbIds[personalNumber]);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount()  {
            return 1;
        }
    }
}