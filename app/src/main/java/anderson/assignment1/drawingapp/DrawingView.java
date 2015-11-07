package anderson.assignment1.drawingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.ViewGroup;

/**
 * Created by anderson on 9/20/15.
 */
public class DrawingView extends ViewGroup {
    //TODO: Create a class that stores X, Y, Color and if the point is the beginning of a polyline
    private Path _path = new Path();
    private Canvas _canvas;
    private Paint _paint, _paintCanvas;
    private Bitmap _canvasBitmap;

    public DrawingView(Context context){
        super(context);
        setBackgroundColor(Color.WHITE);
        _paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        _paint.setColor(Color.BLACK);
        _paint.setAntiAlias(true);
        _paint.setStyle(Paint.Style.STROKE);
        _paint.setStrokeWidth(5.0f * getResources().getDisplayMetrics().density);
        _paint.setStrokeJoin(Paint.Join.ROUND);
        _paint.setStrokeCap(Paint.Cap.ROUND);
        _paintCanvas = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == event.ACTION_DOWN){
            _path.moveTo(event.getX(), event.getY());
        } else if(event.getAction() == event.ACTION_MOVE) {
            _path.lineTo(event.getX(), event.getY());
        } else if(event.getAction() == event.ACTION_UP){
            _canvas.drawPath(_path, _paint);
            _path.reset();
        }

        invalidate();

        return true;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(changed) {
            _canvasBitmap = _canvasBitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_4444);
            _canvas = new Canvas(_canvasBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(_canvasBitmap, 0, 0, _paintCanvas);
        canvas.drawPath(_path, _paint);
    }

    public void setPaintColor(int color){
        _paint.setColor(color);
    }

    public void resetCanvas(){
        _canvasBitmap = _canvasBitmap.createBitmap(_canvasBitmap.getWidth(), _canvasBitmap.getHeight(), Bitmap.Config.ARGB_4444);
        _canvas = new Canvas(_canvasBitmap);
    }
}
