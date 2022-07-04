package com.example.test2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;

public class ThirdTab extends Fragment {
    CustomView customView;
    FrameLayout stage;
    RadioGroup radioGroup;
    RadioButton radioBtnBlack, radioBtnRed, radioBtnGreen, radioBtnBlue;
    SeekBar seekBar;
    Button btnClear, btnSave, btnBackground, btnUndo, btnRedo, btnAddText;

    int color = Color.BLACK;
    float r = 5f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_tab, container, false);

        initView(v);
        initListener();

        customView = new CustomView(getActivity());
        customView.setPaintInfo(color, r);
        customView.setBackgroundImage(BitmapFactory.decodeResource(getResources(), R.drawable.sample_1));
        stage.addView(customView);

        return v;
    }

    public static ThirdTab newInstance(String text) {

        ThirdTab  f = new ThirdTab();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    private void initView(View v){
        stage = v.findViewById(R.id.stage);
        radioGroup = v.findViewById(R.id.radioGroup);
        radioBtnBlack = v.findViewById(R.id.radioBtnBlack);
        radioBtnRed = v.findViewById(R.id.radioBtnRed);
        radioBtnGreen = v.findViewById(R.id.radioBtnGreen);
        radioBtnBlue = v.findViewById(R.id.radioBtnBlue);
        seekBar = v.findViewById(R.id.seekBar);
        btnClear = v.findViewById(R.id.clear);
        btnSave = v.findViewById(R.id.save);
        btnBackground = v.findViewById(R.id.background);
        btnUndo = v.findViewById(R.id.undo);
        btnRedo = v.findViewById(R.id.redo);
        btnAddText = v.findViewById(R.id.addtext);
    }

    private void initListener(){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch(i){
                    case R.id.radioBtnBlack:
                        color = Color.BLACK;
                        break;
                    case R.id.radioBtnRed:
                        color = Color.RED;
                        break;
                    case R.id.radioBtnGreen:
                        color = Color.GREEN;
                        break;
                    case R.id.radioBtnBlue:
                        color = Color.BLUE;
                        break;
                }
                customView.setPaintInfo(color, r);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                r = (float)seekBar.getProgress() / 10;
                customView.setPaintInfo(color, r);
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.clearAll(color, r);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = getBitmapFromView(customView);
                OnCheckPermission();
                saveBitmapAsJPG(bitmap);
                Toast.makeText(getActivity(), "저장되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
        btnBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.flipBackground();
            }
        });
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.undoPathInfo();
            }
        });
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customView.redoPathInfo();
            }
        });
        btnAddText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    public static Bitmap getBitmapFromView(View view){
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }

    public void saveBitmapAsJPG(Bitmap bitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        Log.d("root", root);
        File myDir = new File(root + "/Pictures");

        String fname = "Image-" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fname);
        if(file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void OnCheckPermission() {
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this, "앱 실행을 위해서는 권한을 설정하세요", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}