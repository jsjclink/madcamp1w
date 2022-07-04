package com.example.test2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ThirdTab extends Fragment {
    CustomView customView;
    FrameLayout stage;
    RadioGroup radioGroup;
    RadioButton radioBtnBlack, radioBtnRed, radioBtnGreen, radioBtnBlue;
    SeekBar seekBar;
    Button btnClear, btnSave, btnBackground, btnUndo, btnRedo, btnAddText;

    int color = Color.BLACK;
    float r = 5f;
    String firstTabNumberString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.third_tab, container, false);

        initView(v);
        initListener();

        customView = new CustomView(getActivity());
        customView.setPaintInfo(color, r * 2);
        stage.addView(customView);

        Intent intent = getActivity().getIntent();
        String from = intent.getStringExtra("from");
        firstTabNumberString = intent.getStringExtra("number");
        // Pictures from gallery detail activity is set to background
        if ("GalleryDetailActivity".equals(from)) {
            String uriStr = getActivity().getIntent().getStringExtra("uri");
            Uri uri;
            if (uriStr.startsWith("/")) {
                uri = Uri.parse("file://" + uriStr);
            } else {
                uri = Uri.parse(uriStr);
            }
            try {
                customView.setBackgroundImage(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return v;
    }

    public static ThirdTab newInstance() {
        ThirdTab f = new ThirdTab();
        return f;
    }

    private void initView(View v) {
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

    private void initListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
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
                customView.setPaintInfo(color, r * 2);
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
                r = (float) seekBar.getProgress() / 10;
                customView.setPaintInfo(color, r * 2);
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
                final EditText editText = new EditText(getActivity());

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("텍스트 추가");
                dialog.setView(editText);
                dialog.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        customView.wantToAddText(editText.getText().toString());
                    }
                });
                dialog.show();
            }
        });
    }

    public static Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        else
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }

    public void saveBitmapAsJPG(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Pictures");

        String fname = "Image-" + System.currentTimeMillis() + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

            // If called from phone number detail activity, then the pictures is saved
            if (firstTabNumberString != null) {
                Log.d("hihi", firstTabNumberString);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = prefs.edit();
                String json = prefs.getString(firstTabNumberString, null);
                ArrayList<String> urls = new ArrayList<String>();
                try {
                    if (json != null) {
                        JSONArray a = new JSONArray(json);
                        for (int i = 0; i < a.length(); i++) {
                            String url = a.optString(i);
                            urls.add(url);
                        }
                    }
                    String uriStr = file.toString();
                    urls.add(uriStr);
                    JSONArray a = new JSONArray();
                    for (int i = 0; i < urls.size(); i++) {
                        a.put(urls.get(i));
                    }
                    Log.d("nono", a.toString());
                    editor.putString(firstTabNumberString, a.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                editor.apply();
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}