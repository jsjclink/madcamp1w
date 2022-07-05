package com.example.test2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    private static final int NUM_PAGES = 3;
    public static ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private String[] titles = new String[]{"전화번호부", "사진첩", "그림판"};
    private int[] tabIcons = new int[]{R.drawable.tabicon1, R.drawable.tabicon2, R.drawable.tabicon3};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.mypager);
        pagerAdapter = new MyPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        TabLayout tabLayout =(TabLayout) findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, (tab, postiion) -> { return; }).attach();
        setTabIcons(tabLayout);
        getSupportActionBar().hide();
        viewPager.setUserInputEnabled(false);

        OnCheckPermission();

        //android-data-com.example.teset2/pictures 폴더 추가
        String path = getExternalFilesDir(null) + "/Pictures/";
        Log.d("path", path);
        File dir = new File(path);
        try{
            if(!dir.exists()){
                Log.d("생성전", "폴더아직없음 생성함이제");
                if(dir.mkdirs() == false) Log.d("생성후", "생성실패");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }


        String from = getIntent().getStringExtra("from");
        if("GalleryDetailActivity".equals(from)){
            viewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }
                else{
                    finish();
                }
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int pos) {
            switch (pos) {
                case 1:
                    return SecondTab.newInstance();
                case 2:
                    return ThirdTab.newInstance();
                default:
                    return FirstTab.newInstance(getJsonString());
            }
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

    public String getJsonString(){
        String json = "";
        try{
            InputStream is = getAssets().open("phone.json");
            int fileSize = is.available();
            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return json;
    }

    private void setTabIcons(TabLayout tabLayout) {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    private void OnCheckPermission() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this, "앱 실행을 위해서는 권한을 설정하세요", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}