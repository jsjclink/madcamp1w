package com.example.test2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

public class GalleryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        Intent intent = getIntent();
        Uri pictureUri = Uri.parse(intent.getStringExtra("pictureUri"));

        TextView explanationTV = findViewById(R.id.GalleryDetailExplanationTV);
        PhotoView photoView = findViewById(R.id.GalleryDetailPhotoView);
        photoView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        explanationTV.setText(pictureUri.toString());
        photoView.setImageURI(pictureUri);

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
}