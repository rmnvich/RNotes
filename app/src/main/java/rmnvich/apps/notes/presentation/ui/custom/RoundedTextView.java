package rmnvich.apps.notes.presentation.ui.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import rmnvich.apps.notes.R;

public class RoundedTextView extends View {

    private int circleColor;
    private int labelColor;

    private String labelText;

    private float labelSize;
    private float cornerRadius;
    private float circleSize;

    private Paint circlePaint;
    private Paint labelPaint;

    public RoundedTextView(Context context) {
        super(context);
        init(context, null);
    }

    public RoundedTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        circlePaint = new Paint();
        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.LINEAR_TEXT_FLAG);

        TypedArray array = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.RoundedTextView, 0, 0);
        try {
            circleColor = array.getInteger(R.styleable.RoundedTextView_circleColor, 0);
            circleSize = array.getDimensionPixelSize(R.styleable.RoundedTextView_circleSize, 15);
            labelColor = array.getInteger(R.styleable.RoundedTextView_labelColor, 0);
            labelText = array.getString(R.styleable.RoundedTextView_labelText);
            labelSize = array.getDimensionPixelSize(R.styleable.RoundedTextView_labelSize, 12);
            cornerRadius = array.getDimensionPixelSize(R.styleable.RoundedTextView_cornerRadius, 10);
        } catch (Exception ignored) {
        } finally {
            array.recycle();
        }
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int canvasWidth = getWidth();
        int canvasHeight = getHeight();

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int usableWidth = canvasWidth - (paddingLeft + paddingRight);
        int usableHeight = canvasHeight - (paddingTop + paddingBottom);

        int radius = Math.min(usableWidth, usableHeight) >> 1;
        int cx = paddingLeft + (usableWidth >> 1);
        int cy = paddingTop + (usableHeight >> 1);

        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.FILL);
        circlePaint.setAntiAlias(true);

        if (labelText == null || labelText.isEmpty()) {
            canvas.drawCircle(cx, cy, radius, circlePaint);
        } else {
            RectF roundedSquare = new RectF(0, 0, canvasWidth, canvasHeight);
            canvas.drawRoundRect(roundedSquare, cornerRadius, cornerRadius, circlePaint);

            labelPaint.setColor(labelColor);
            labelPaint.setStyle(Paint.Style.FILL);
            labelPaint.setTextAlign(Paint.Align.CENTER);
            labelPaint.setTextSize(labelSize);

            canvas.drawText(
                    labelText,
                    (int) roundedSquare.width() >> 1,
                    (int) (roundedSquare.height() + getTextHeight()) >> 1,
                    labelPaint
            );
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredWidth;
        int desiredHeight;
        if (labelText == null || labelText.isEmpty()) {
            desiredWidth = (int) circleSize;
            desiredHeight = (int) circleSize;
        } else {
            desiredWidth = getTextWidth();
            desiredHeight = (int) circleSize;
        }

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = Math.min(desiredWidth, widthSize);
        } else width = desiredWidth;

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            height = Math.min(desiredHeight, heightSize);
        } else height = desiredHeight;

        setMeasuredDimension(width, height);
    }

    private int getTextHeight() {
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        return bounds.height();
    }

    private int getTextWidth() {
        Rect bounds = new Rect();
        labelPaint.getTextBounds(labelText, 0, labelText.length(), bounds);
        return bounds.width();
    }

    public void setCircleColor(int color) {
        circleColor = color;
        invalidate();
        requestLayout();
    }

    public void setLabelColor(int color) {
        labelColor = color;
        invalidate();
        requestLayout();
    }

    public void setLabelText(String label) {
        labelText = label;
        invalidate();
        requestLayout();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public int getLabelColor() {
        return labelColor;
    }

    public String getLabelText() {
        return labelText;
    }
}
