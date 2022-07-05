package com.example.test2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        Intent intent = getIntent();
        Uri pictureUri = Uri.parse(intent.getStringExtra("pictureUri"));
        String numberString = intent.getStringExtra("number");

        TextView explanationTV = findViewById(R.id.GalleryDetailExplanationTV);
        PhotoView photoView = findViewById(R.id.GalleryDetailPhotoView);
        photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        explanationTV.setText(pictureUri.toString());
        photoView.setImageURI(pictureUri);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton editFB = findViewById(R.id.editFB);
        editFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GalleryDetailActivity.this, MainActivity.class);
                intent.putExtra("from", "GalleryDetailActivity");
                intent.putExtra("uri", pictureUri.toString());
                intent.putExtra("number", numberString);
                startActivity(intent);
            }
        });
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