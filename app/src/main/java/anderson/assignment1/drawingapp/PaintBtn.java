package anderson.assignment1.drawingapp;

/**
 * Created by anderson on 9/20/15.
 */

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

/**
 * Created by anderson on 8/31/15.
 */
public class PaintBtn extends View {
    private int _color = Color.BLACK;
    private boolean _selected = false;
    private RectF btnRect;
    private boolean _removeBtnFlag = false;
    private boolean _mixMode = false;

    public PaintBtn(Context context, int color){
        super(context);
        _color = color;
    }

    public PaintBtn(Context context, int color, boolean removeBtnFlag){
        this(context, color);
        _removeBtnFlag = removeBtnFlag;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        btnRect = new RectF();
        btnRect.left = 0.0f + getPaddingLeft();
        btnRect.top = 0.0f + getPaddingTop();
        btnRect.right = getWidth() - getPaddingRight();
        btnRect.bottom = getHeight() - getPaddingBottom();

        if (btnRect.width() < btnRect.height()) {
            btnRect.bottom -= getHeight() - getWidth();
        } else {
            btnRect.right -= getWidth() - getHeight();
        }

        float cx = btnRect.centerX();
        float cy = btnRect.centerY();
        float radius = btnRect.width() / 2.0f;
        //double pi = Math.PI;
        Random rand = new Random();
        int n;
        Path path = new Path();
        path.moveTo((float) (cx + radius * Math.cos(0)), (float) (cy + radius * Math.sin(0)));

        for (int i = 0; i < 376; i += 15) {
            n = rand.nextInt(10);
            n += 18;
            path.lineTo((float) (cx + (radius) * Math.cos(Math.toRadians(i))), (float) (cy + (radius) * Math.sin(Math.toRadians(i))));
            i += 15;
            path.lineTo((float) (cx + (radius / (n/10.0)) * Math.cos(Math.toRadians(i))), (float) (cy + (radius / 1.4f) * Math.sin(Math.toRadians(i))));
        }

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        if(_removeBtnFlag) _color = Color.LTGRAY;
        paint.setColor(_color);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(5.0f * getResources().getDisplayMetrics().density);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setFlags(Paint.DITHER_FLAG);
        paint.setPathEffect(new CornerPathEffect(60));
        paint.setMaskFilter(new EmbossMaskFilter(new float[]{0.2f, 1.2f, 0.7f}, 0.6f, 0.45f, 2.5f));

        canvas.drawPath(path, paint);

        if(_removeBtnFlag){
            paint.setMaskFilter(null);
            paint.setColor(Color.BLACK);
            paint.setTextSize(12.0f * getResources().getDisplayMetrics().density);
            canvas.drawText("Delete", btnRect.left + radius / 2, btnRect.centerY(), paint);
        }

        if (_selected && !_mixMode) {
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(3.0f * getResources().getDisplayMetrics().density);
            paint.setStyle(Paint.Style.STROKE);
            paint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
            canvas.drawPath(path, paint);
        } else if (_selected && _mixMode){
            paint.setColor(Color.CYAN);
            paint.setStrokeWidth(3.0f * getResources().getDisplayMetrics().density);
            paint.setStyle(Paint.Style.STROKE);
            paint.setMaskFilter(new BlurMaskFilter(15, BlurMaskFilter.Blur.NORMAL));
            canvas.drawPath(path, paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        int desiredSize = (int) (70.0f * getResources().getDisplayMetrics().density);

        int measuredWidth = width;
        int measuredHeight = height;

        if(widthMode == MeasureSpec.EXACTLY){
            measuredHeight = width;
        }
        else if(heightMode == MeasureSpec.EXACTLY){
            measuredWidth = height;
        }
        else if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            measuredWidth = Math.min(measuredWidth, desiredSize);
            measuredHeight = measuredWidth;
        }
        else if(widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED) {
            measuredHeight = Math.max((int) (160.0f * getResources().getDisplayMetrics().density), Math.max(getSuggestedMinimumHeight(), getSuggestedMinimumWidth()));
            measuredWidth = height;
        }
        else if(widthMode != MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED){
            measuredHeight = width;
            measuredWidth = height;
        }
        else if(widthMode == MeasureSpec.UNSPECIFIED && heightMode != MeasureSpec.UNSPECIFIED){
            measuredHeight = width;
            measuredWidth = height;
        }

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    public boolean isSelected() {
        return _selected;
    }

    public void setSelected(boolean selected) {
        this._selected = selected;
    }

    public int getColor() {
        return _color;
    }

    public void setColor(int color) {
        this._color = color;
    }

    public boolean isRemove(){
        return _removeBtnFlag;
    }

    public boolean isMixModeOn(){
        return _mixMode;
    }

    public void setMixMode(boolean mode){
        _mixMode = mode;
    }
}
