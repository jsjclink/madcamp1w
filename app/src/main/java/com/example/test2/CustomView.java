package com.example.test2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CustomView extends View {
    Paint paint, textPaint; //paint: 그린 paint, textPaint: 글
    ArrayList<PathInfo> data, poppedData, tmpData; //poppedData: pop된 data stack에 쌓은거 tmpData: 부드럽게 그려지게(마우스 떼면 clear)
    PathInfo pathInfo;
    ArrayList<TextInfo> textData;
    Bitmap backgroundImange = null;
    boolean enableBG = false;
    int curColor; float curR;
    String text = "";
    Context context;


    public CustomView(Context context){
        super(context);
        this.context = context;

        data = new ArrayList<>();
        poppedData = new ArrayList<>();
        tmpData = new ArrayList<>();
        textData = new ArrayList<>();

        paint = new Paint();
        paint.setColor(Color.CYAN);
        paint.setStrokeWidth(10f);

        //text 설정
        textPaint = new Paint();
        textPaint.setTextSize(50);
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
        if(enableBG && backgroundImange != null) canvas.drawBitmap(backgroundImange, 0, 0, null);
        for (PathInfo p : tmpData){
            canvas.drawPath(p, p.getPaint());
        }
        for (PathInfo p : data){
            canvas.drawPath(p, p.getPaint());
        }
        for (TextInfo t : textData){
            canvas.drawText(t.getText(), t.getX(), t.getY(), textPaint);
        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                pathInfo.moveTo(event.getX(), event.getY());
                //자연스럽게 그려지기 위한 코드
                tmpData.add(pathInfo);

                if(text.length() > 0){
                    float x = event.getX();
                    float y = event.getY();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setTitle("Alert");
                    dialog.setMessage(text + " 를 이 위치에 추가하시겠습니까?");
                    dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            textData.add(new TextInfo(text, x, y));
                            text = "";
                            invalidate();
                        }
                    });
                    dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            text = "";
                        }
                    });
                    dialog.show();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                pathInfo.lineTo(event.getX(), event.getY());
                //자연스럽게 그려지기 위한 코드
                tmpData.add(pathInfo);
                break;
            case MotionEvent.ACTION_UP:
                data.add(pathInfo);
                pathInfo = new PathInfo();
                setPaintInfo(curColor, curR);
                poppedData.clear();
                //자연스럽게 그려지기 위한 코드
                tmpData.clear();
                break;
        }

        invalidate();
        return true;
    }

    public void clearAll(int color, float r) {
        data.clear();
        setPaintInfo(color, r);
        textData.clear();
        invalidate();
    }

    public void setBackgroundImage(Bitmap bitmap) {
        this.backgroundImange = Bitmap.createScaledBitmap(bitmap, 1080, 1080, true);
        this.enableBG = true;
    }
    public void flipBackground(){
        enableBG = enableBG ? false : true;
        invalidate();
    }

    public void undoPathInfo() {
        if(data.size() > 0) {
            poppedData.add(data.remove(data.size()-1));
        }
        invalidate();
    }

    public void redoPathInfo() {
        if(poppedData.size() > 0){
            data.add(poppedData.remove(poppedData.size()-1));
        }
        invalidate();
    }


    public void wantToAddText(String text) {
        this.text = text;
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
class TextInfo{
    String text;
    float x, y;

    TextInfo(String text, float x, float y){
        this.text = text;
        this.x = x;
        this.y = y;
    }

    public String getText() {
        return text;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
