package me.drakeet.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.RelativeLayout;

/**
 * Created by drakeet(http://drakeet.me)
 * Date: 8/29/15 10:53
 */
public abstract class ShapeLayout extends RelativeLayout {

    private static final String TAG = "ShapeLayout";

    private final Matrix mMatrix = new Matrix();
    private final Path mPath = new Path();
    //private Bitmap mSrcBitmap;

    public ShapeLayout(Context context) {
        this(context, null);
    }

    public ShapeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShapeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected abstract void drawPath(Path path, int srcWidth, int srcHeight, float scale,
        float translateX, float translateY);

    private void calculateSrcSize() {
        int srcWidth = getWidth();
        int srcHeight = getHeight();

        if (srcWidth > 0 && srcHeight > 0) {
            float width = getWidth();
            float height = getHeight();

            float scale;
            float translateX = 0;
            float translateY = 0;

            if (srcWidth * height > width * srcHeight) {
                scale = height / srcHeight;
                translateX = Math.round((width / scale - srcWidth) / 2f);
            }
            else {
                scale = width / (float) srcWidth;
                translateY = Math.round((height / scale - srcHeight) / 2f);
            }

            mMatrix.setScale(scale, scale);
            mMatrix.preTranslate(translateX, translateY);
            mMatrix.postTranslate(0, 0);

            drawPath(mPath, srcWidth, srcHeight, scale, translateX, translateY);
        }
    }

    @Override protected void dispatchDraw(Canvas canvas) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        calculateSrcSize();
        try {
            canvas.clipPath(mPath);
        } catch (UnsupportedOperationException e) {
            Log.e(TAG, "clipPath() not supported");
        }
        super.dispatchDraw(canvas);
    }

    public final int dpToPx(DisplayMetrics displayMetrics, int dp) {
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
