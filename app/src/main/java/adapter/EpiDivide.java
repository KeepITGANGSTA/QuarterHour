package adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 李英杰 on 2017/12/7.
 * Description：
 */

public class EpiDivide extends RecyclerView.ItemDecoration {
    private int lr;
    private int tb;


    private int mDivideHight=1;
    private static final int[] ATTRS=new int[]{android.R.attr.listDivider};
    private Context mContext;
    private int mOrientation;
    private Drawable mDrawable;
    private Paint mPaint;
    private int mDivideColor;
    public static final int HORIZONTAL_LIST= LinearLayoutManager.HORIZONTAL;
    public static final int VETTICAL_LIST=LinearLayoutManager.VERTICAL;

    public EpiDivide(Context context,int orientation,int lr, int tb) {
        this.lr=lr;
        this.tb=tb;
        final TypedArray typedArray = context.obtainStyledAttributes(ATTRS);
        mDrawable=typedArray.getDrawable(0);
        typedArray.recycle();
        setOrientation(orientation);
    }

    private void setOrientation(int orientation){
        if (orientation!=HORIZONTAL_LIST && orientation!=VETTICAL_LIST){
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation=orientation;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation==VETTICAL_LIST){
            drawVertical(c,parent);
        }else if (mOrientation==HORIZONTAL_LIST){
            drawHorizontal(c,parent);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        final int left=parent.getPaddingLeft();
        final int right=parent.getWidth() - parent.getPaddingRight();
        final int childSize=parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child=parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top=child.getBottom()+layoutParams.bottomMargin;
            final int bottom=top+mDrawable.getIntrinsicHeight();
            if (mDrawable!=null){
                mDrawable.setBounds(left,top,right,bottom);
                mDrawable.draw(c);
            }
        }
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top=parent.getPaddingTop();
        final int bottom=parent.getHeight()-parent.getPaddingBottom();
        final int childSize=parent.getChildCount();
        for (int i = 0; i < childSize; i++) {
            final View child=parent.getChildAt(i);
            RecyclerView.LayoutParams layoutParams= (RecyclerView.LayoutParams) child.getLayoutParams();
            final int left=child.getRight()+layoutParams.rightMargin;
            final int right=left+mDrawable.getIntrinsicHeight();
            if (mDrawable!=null){
                mDrawable.setBounds(left,top,right,bottom);
                mDrawable.draw(c);
            }
        }

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view)==0){
            outRect.top=tb;
        }
        outRect.top=tb;
        outRect.bottom=tb;
    }

}
