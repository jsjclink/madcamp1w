package com.example.test2;

import android.content.Context;
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

    public CustomView(Context context){
        super(context);

        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(10f);

        data = new ArrayList<>();
    }

    public void setPaintInfo(int color, float r){
        paint = new Paint();
        paint.setColor(color);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(r);

        pathInfo = new PathInfo();
        pathInfo.setPaint(paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
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
                break;
        }

        data.add(pathInfo);

        invalidate();

        return true;
    }

    public void clearAll(int color, float r) {
        data.clear();
        setPaintInfo(color, r);
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
