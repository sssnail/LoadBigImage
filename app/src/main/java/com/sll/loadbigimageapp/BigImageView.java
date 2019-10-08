package com.sll.loadbigimageapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import java.io.IOException;
import java.io.InputStream;

public class BigImageView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {
    private final Rect mRect;
    private final BitmapFactory.Options mOptions;
    private final GestureDetector mGestureDetector;
    private final Scroller mScroller;
    private int mImageWidth;
    private int mImageHeight;
    private BitmapRegionDecoder mDecoder;
    private int mViewWidth, mViewHeight;
    private float mScale;
    private Bitmap mBitmap;

    public BigImageView(Context context) {
        this(context, null);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //step 1: set member variable.
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();//内存复用
        mGestureDetector = new GestureDetector(context, this);//手势识别
        mScroller = new Scroller(context);
        setOnTouchListener(this);

    }

    //step 2： get picture's imformation.
    public void setImage(InputStream is){
        //get picture's width and height.
        mOptions.inJustDecodeBounds = true;//只读取图片，不加载到内存中
        BitmapFactory.decodeStream(is, null, mOptions);
        mImageWidth = mOptions.outWidth;
        mImageHeight = mOptions.outHeight;

        //开启复用 inMutable
        mOptions.inMutable = true;
        //set fomat as GRB565
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;//加载到内存中

        //区域解码器
        try {
            mDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        requestLayout();

    }

    //step 3: measure. get View's width and height.
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidth = getMeasuredWidth();
        mViewHeight = getMeasuredHeight();

        //define the area we need to loding
        mRect.left = 0;
        mRect.top = 0;
        mRect.right = mImageWidth;
        //计算缩放因子
        mScale = mViewWidth/(float)mImageWidth;
        mRect.bottom = (int)(mViewHeight/mScale);

    }

    //step 4 : draw content.
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //判断解码器是否为null，如果解码器为null，表示未设置过图片
        if(mDecoder == null){
            return;
        }
        //真正内存复用 复用的bitmap必须跟即将解码的bitmap尺寸一样
        mOptions.inBitmap = mBitmap;
        mBitmap = mDecoder.decodeRegion(mRect, mOptions);//指定解码区域
        //得到矩阵进行缩放，相当于得到view的大小
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        canvas.drawBitmap(mBitmap, matrix, null);

    }

    //step 5: 处理点击事件
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //事件交给手势事件处理
        return mGestureDetector.onTouchEvent(event);
    }

    //step 6:
    @Override
    public boolean onDown(MotionEvent e) {
        //如果移动没有停止，强行停止
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        //继续接收后续事件
        return true;
    }

    //step 7: 处理滑动事件
    //e1:开始事件，手指按下去，开始获取坐标
    //e2:获取当前事件坐标
    //xy：xy轴坐标
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //上下移动时， mRect需要改变现实的区域
        mRect.offset(0, (int)distanceY);
        //移动时，处理到达顶部和底部的情况
        if(mRect.bottom > mImageHeight){
            mRect.bottom = mImageHeight;
            mRect.top = mImageHeight - (int)(mViewHeight/mScale);
        }
        if (mRect.top < 0){
            mRect.top = 0;
            mRect.bottom = (int)(mViewHeight/mScale);
        }
        invalidate();
        return false;
    }

    //step 8:处理惯性
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mScroller.fling(0, mRect.top, 0, (int)-velocityY, 0, 0, 0, mImageHeight - (int)(mViewHeight/mScale));
        return false;
    }

    //step 9: 处理计算结果
    @Override
    public void computeScroll() {
        if(mScroller.isFinished()){
            return;
        }
        if (mScroller.computeScrollOffset()){
            mRect.top = mScroller.getCurrY();
            mRect.bottom = mRect.top + (int)(mViewHeight/mScale);
            invalidate();
        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }



}
