package com.saysay.ljh.chattingui.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ljh on 2015/9/10 0010.
 */
public class PointIndicator extends View {
    private Paint bPaint, fPaint;
    private Path bPath,fPath;
    private Context mContext;
    private int radius;
    private int offset;
    private int count;
    private int position;

    public PointIndicator(Context context) {
        super(context);
        init(context);

    }

    public void setIndicator(int radius,int offset,int fColor,int bColor ){
        this.radius=radius;
        this.offset=offset;
        bPaint.setColor(bColor);
        fPaint.setColor(fColor);

    }

    public void setIndicatorCount(int count){
        this.count=count;
        for (int i=0;i<count;i++){
            bPath.addCircle(radius + offset * i, radius, radius*0.9f, Path.Direction.CCW);
        }
        invalidate();

    }
    public PointIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context ctx){
        mContext=ctx;
        bPaint =new Paint();
        bPaint.setAntiAlias(true);
        bPaint.setColor(Color.GRAY);
        bPaint.setAlpha(125);
        bPaint.setStyle(Paint.Style.FILL);
        bPaint.setStrokeWidth(1);
        bPath =new Path();
        fPaint =new Paint(bPaint);
        fPaint.setAlpha(0);
        fPaint.setColor(Color.RED);
        fPath=new Path();
        radius=5;
        setWillNotDraw(false);
    }

    public void translationPoint(int position){
        this.position=position;
        invalidate();
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension((count - 1) * offset + 2 * radius, 2 * radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(bPath, bPaint);
        fPath.reset();
        fPath.addCircle(radius + offset * position, radius, radius, Path.Direction.CCW);
        canvas.drawPath(fPath, fPaint);
    }
}
