package anderson.assignment1.drawingapp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Anderson Sales de Menezes
 * u1034315
 * Assignment 1 - Drawing App
 */

public class MainActivity extends Activity {

    private int[] colors = {Color.BLACK, Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.WHITE};
    private DrawingView drawingView;
    private PaletteView paletteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(rootLayout);

        drawingView = new DrawingView(this);
        rootLayout.addView(drawingView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        paletteView = new PaletteView(this);
        paletteView.setBackgroundColor(Color.DKGRAY);
        paletteView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        rootLayout.addView(paletteView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        addDeleteButton();
        createInitialPaints();

        LinearLayout textViews = new LinearLayout(this);

        TextView resetCanvas = new TextView(this);
        resetCanvas.setBackgroundColor(Color.DKGRAY);
        resetCanvas.setTextColor(Color.WHITE);
        resetCanvas.setText("RESET CANVAS");
        resetCanvas.setGravity(Gravity.LEFT);
        textViews.addView(resetCanvas, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        TextView resetPalette = new TextView(this);
        resetPalette.setBackgroundColor(Color.DKGRAY);
        resetPalette.setTextColor(Color.WHITE);
        resetPalette.setText("RESET PALETTE");
        resetPalette.setGravity(Gravity.RIGHT);
        textViews.addView(resetPalette, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        rootLayout.addView(textViews);

        resetCanvas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.resetCanvas();
                drawingView.invalidate();
            }
        });

        resetPalette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAllPaints();
                addDeleteButton();
                createInitialPaints();
            }
        });
    }

    private void toggleColor(int id){
        PaintBtn clickedBtn = (PaintBtn) findViewById(id);
        PaintBtn otherBtn;
        PaintBtn activeMixBtn = null;

        for (int i=0; i < paletteView.getChildCount(); i++){
            otherBtn = (PaintBtn) paletteView.getChildAt(i);
            if(!otherBtn.equals(clickedBtn)) {
                if(otherBtn.isMixModeOn()){
                    activeMixBtn = otherBtn;
                }
                otherBtn.invalidate();
                otherBtn.setSelected(false);
                otherBtn.setMixMode(false);
            }
        }

        if(clickedBtn.isSelected() && !clickedBtn.isMixModeOn()) clickedBtn.setMixMode(true);
        else if(clickedBtn.isSelected() && clickedBtn.isMixModeOn()) clickedBtn.setMixMode(false);

        if(activeMixBtn != null && paletteView.getChildCount() > 7){
            Toast.makeText(this, "There are too many colors in the palette. Remove one before you create another.",
                    Toast.LENGTH_LONG).show();
            activeMixBtn.setSelected(true);
            activeMixBtn.invalidate();
        } else if(activeMixBtn != null){
            clickedBtn.setSelected(false);
            addColor(activeMixBtn, clickedBtn);
        } else {
            clickedBtn.setSelected(true);
            drawingView.setPaintColor(clickedBtn.getColor());
        }

        clickedBtn.invalidate();
    }

    private void removeColor(){
        PaintBtn colorBtn;
        for (int i=0; i < paletteView.getChildCount(); i++){
            colorBtn = (PaintBtn) paletteView.getChildAt(i);
            if(colorBtn.isSelected() && colorBtn.isMixModeOn()) {
                paletteView.removeColor(colorBtn);
                break;
            }
        }
    }

    private PaintBtn addColor(PaintBtn activeBtn, PaintBtn clickedBtn){
        int activeRed = Color.red(activeBtn.getColor());
        int activeGreen = Color.green(activeBtn.getColor());
        int activeBlue = Color.blue(activeBtn.getColor());
        int clickedRed = Color.red(clickedBtn.getColor());
        int clickedGreen = Color.green(clickedBtn.getColor());
        int clickedBlue = Color.blue(clickedBtn.getColor());
        int finalColor = Color.rgb(activeRed - (activeRed - clickedRed) / 2,
                activeGreen - (activeGreen - clickedGreen) / 2,
                activeBlue - (activeBlue - clickedBlue) / 2);

        final PaintBtn mixBtn = new PaintBtn(this, finalColor);
        mixBtn.setSelected(true);
        mixBtn.setId(View.generateViewId());
        paletteView.addColor(mixBtn);
        drawingView.setPaintColor(mixBtn.getColor());

        mixBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP) {
                    toggleColor(mixBtn.getId());
                }
                return true;
            }
        });

        return mixBtn;
    }

    private void removeAllPaints(){
        for (int i = 1; i < paletteView.getChildCount(); i++){
            paletteView.removeAllViews();
        }
    }

    private void createInitialPaints(){
        PaintBtn colorBtn;
        for(int i = 0; i < 6; i++){
            colorBtn = new PaintBtn(this, colors[i]);
            colorBtn.setPadding(0, 0, 0, 0);
            final int id = View.generateViewId();
            colorBtn.setId(id);
            paletteView.addView(colorBtn);
            colorBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(event.getAction() == MotionEvent.ACTION_UP) {
                        toggleColor(id);
                    }
                    return true;
                }
            });
        }
    }

    private void addDeleteButton(){
        final PaintBtn removeBtn = new PaintBtn(this, Color.WHITE, true);
        paletteView.addView(removeBtn);
        removeBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    removeColor();
                }
                return true;
            }
        });
    }
}
