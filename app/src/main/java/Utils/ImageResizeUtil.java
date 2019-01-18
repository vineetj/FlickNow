package Utils;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.widget.ImageView;

public class ImageResizeUtil extends ImageView {


    public ImageResizeUtil(Context context) {
        super(context);
    }

    public ImageResizeUtil(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageResizeUtil(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();
            setMeasuredDimension(w, h);
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}