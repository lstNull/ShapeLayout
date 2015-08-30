package me.drakeet.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Path;
import android.util.AttributeSet;

/**
 * xml 配置属性说明:
 * <attr name="arrowSize" format="dimension"/> 下箭头的大小
 * <attr name="arrowPosition" format="float"/> 下剪头的位置, 范围为0.0~2.0, 1.0 代表居中
 *
 * Created by drakeet(http://drakeet.me)
 * Date: 8/30/15 21:00
 */
public class TopInnerArrowLayout extends ShapeLayout {

    private static final int DEFAULT_SIZE_DP = 24;
    private static final float DEFAULT_ARROW_POSITION = 1.0f;

    private float mArrowPosition;
    private int mArrowSizePx;

    public TopInnerArrowLayout(Context context) {
        this(context, null);
    }

    public TopInnerArrowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TopInnerArrowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.TopInnerArrowLayout, defStyleAttr, 0);
            mArrowSizePx =
                typedArray.getDimensionPixelSize(R.styleable.TopInnerArrowLayout_arrowSize, 0);
            mArrowPosition = typedArray.getFloat(R.styleable.TopInnerArrowLayout_arrowPosition,
                DEFAULT_ARROW_POSITION);

            typedArray.recycle();
        }

        if (mArrowSizePx == 0) {
            mArrowSizePx = dpToPx(context.getResources().getDisplayMetrics(), DEFAULT_SIZE_DP);
        }
    }

    @Override
    protected void drawPath(Path path, int srcWidth, int srcHeight, float scale, float translateX,
        float translateY) {
        path.reset();
        float x = -translateX;
        float y = -translateY;
        float scaledTriangleHeight = mArrowSizePx / scale;
        float resultWidth = srcWidth + 2 * translateX;
        float resultHeight = srcHeight + 2 * translateY;
        float centerX = resultWidth / 2f + x;
        centerX = centerX * mArrowPosition;// 偏移中心点

        path.setFillType(Path.FillType.EVEN_ODD);

        float rectLeft, rectRight, rectTop, rectBottom;

        rectLeft = x;
        rectRight = resultWidth + x;
        rectTop = y;
        float srcBottom = resultHeight + rectTop;
        rectBottom = srcBottom - scaledTriangleHeight;
        path.addRect(rectLeft, rectTop, rectRight, rectBottom, Path.Direction.CW);
        path.moveTo(centerX, mArrowSizePx + rectTop);
        path.lineTo(centerX - scaledTriangleHeight * 0.618f, rectTop);
        path.lineTo(centerX + scaledTriangleHeight * 0.618f, rectTop);
    }
}
