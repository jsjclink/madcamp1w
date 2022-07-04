package com.example.test2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CustomView extends View {
    Paint paint;
    ArrayList<PathInfo> data;
    PathInfo pathInfo;
    Bitmap backgroundImange;
    boolean enableBG = true;
    ArrayList<PathInfo> poppedData;
    boolean beforeActionUp = true;
    int curColor;
    float curR;

    public CustomView(Context context){
        super(context);

        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(10f);

        data = new ArrayList<>();
        poppedData = new ArrayList<>();
    }

    public void setPaintInfo(int color, float r){
        curColor = color;
        curR = r;

        paint = new Paint();
        paint.setColor(color);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(r);

        pathInfo = new PathInfo();
        pathInfo.setPaint(paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(enableBG) canvas.drawBitmap(backgroundImange, 0, 0, null);
        for (PathInfo p : data){
            canvas.drawPath(p, p.getPaint());
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pathInfo.moveTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                pathInfo.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                data.add(pathInfo);
                pathInfo = new PathInfo();
                setPaintInfo(curColor, curR);
                poppedData.clear();
                break;
        }

        invalidate();

        return true;
    }

    public void clearAll(int color, float r) {
        data.clear();
        setPaintInfo(color, r);
        invalidate();
    }

    public void setBackgroundImage(Bitmap bitmap) {
        this.backgroundImange = Bitmap.createScaledBitmap(bitmap, 1080, 1000, true);
    }
    public void flipBackground(){
        enableBG = enableBG ? false : true;
        invalidate();
    }

    public void undoPathInfo() {
        if(data.size() > 0) {
            poppedData.add(data.remove(data.size()-1));
        }
        Log.d("afterundo", "data.size()" + Integer.toString(data.size()));
        invalidate();
    }

    public void redoPathInfo() {
        if(poppedData.size() > 0){
            data.add(poppedData.remove(poppedData.size()-1));
        }
        invalidate();
    }
}

class PathInfo extends Path {
    private Paint paint;

    PathInfo(){
        paint = new Paint();
    }

    public Paint getPaint(){
        return paint;
    }

    public void setPaint(Paint paint){
        this.paint = paint;
    }

}
