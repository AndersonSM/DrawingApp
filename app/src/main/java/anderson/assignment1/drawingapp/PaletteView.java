package anderson.assignment1.drawingapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by anderson on 9/14/15.
 */
public class PaletteView extends ViewGroup{
    private Bitmap _backgroundBitmap;
    private BitmapShader _bitmapShader;

    public PaletteView(Context context){
        super(context);
        setBackgroundColor(Color.TRANSPARENT);
        _backgroundBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wood);
        _bitmapShader = new BitmapShader(_backgroundBitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        RectF layoutOvalRect = new RectF();
        layoutOvalRect.left = (float)getPaddingLeft();
        layoutOvalRect.top = (float)getPaddingTop();
        layoutOvalRect.right = (float)getWidth();
        layoutOvalRect.bottom = (float)getHeight();

        float dp = getResources().getDisplayMetrics().density;

        Paint paint = new Paint();
        int[] brownRGB = {156,120,22};
        paint.setColor(Color.rgb(brownRGB[0], brownRGB[1], brownRGB[2]));
        paint.setShadowLayer(10.0f * dp , -8.0f * dp, 8.0f * dp, Color.BLACK);
        canvas.drawOval(layoutOvalRect, paint);

        Paint paintWood = new Paint();
        paintWood.setColor(Color.rgb(brownRGB[0], brownRGB[1], brownRGB[2]));
        paintWood.setShader(_bitmapShader);
        canvas.drawOval(layoutOvalRect, paintWood);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        float childWidth = (70.0f * getResources().getDisplayMetrics().density);
        float childHeight = (70.0f * getResources().getDisplayMetrics().density);

        RectF layoutOvalRect = new RectF();
        layoutOvalRect.left = (float)getPaddingLeft() + childWidth * 0.5f;
        layoutOvalRect.top = (float)getPaddingTop() + childHeight * 0.5f;
        layoutOvalRect.right = (float)(getWidth() - getPaddingRight()) - childWidth * 0.5f;
        layoutOvalRect.bottom = (float)(getHeight() - getPaddingBottom()) - childHeight * 0.5f;

        for(int childIndex = 0; childIndex < getChildCount(); childIndex++){
            View childView = getChildAt(childIndex);

            float childTheta = (float)childIndex / (float)getChildCount() * 2.0f * (float)Math.PI;
            PointF childCenter = new PointF();
            childCenter.x = layoutOvalRect.centerX() + layoutOvalRect.width() * 0.5f * (float)Math.cos(childTheta);
            childCenter.y = layoutOvalRect.centerY() + layoutOvalRect.height() * 0.5f * (float)Math.sin(childTheta);

            double angle = Math.toDegrees(childTheta);
            if(getChildCount() % 2 != 0) {
                if (angle > 180 && angle < 220) {
                    childCenter.y -= 20.0f * getResources().getDisplayMetrics().density;
                    childCenter.x += 45.0f * getResources().getDisplayMetrics().density;
                } else if (angle > 20 && angle < 70) {
                    childCenter.x -= 45.0f * getResources().getDisplayMetrics().density;
                } else if (angle > 250 && angle < 280) {
                    childCenter.x += 30.0f * getResources().getDisplayMetrics().density;
                }
            }

            Rect childRect = new Rect();
            childRect.left = (int)(childCenter.x - childWidth * 0.5f);
            childRect.top = (int)(childCenter.y - childHeight * 0.5f);
            childRect.right = (int)(childCenter.x + childWidth * 0.5f);
            childRect.bottom = (int)(childCenter.y + childHeight * 0.5f);

            childView.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    public void removeColor(View view){
        for (int i = 0; i < getChildCount(); i++){
            if(getChildAt(i).equals(view)){
                removeViewAt(i);
            }
        }
    }

    public void addColor(View view){
        this.addView(view);
    }
}
