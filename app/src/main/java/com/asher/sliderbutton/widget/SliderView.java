package com.asher.sliderbutton.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.apicloud.uisliderbutton.Utils.ViewUtil;
/**
 * Created by asher on 2021/1/19.
 */
public class SliderView extends View {
    
    private int mDefaultRoundColor = Color.parseColor("#FFEDDA");
    private int mCompileRoundColor = Color.parseColor("#F7B365");
    private int sliderColor = Color.parseColor("#696969");

    private float mRoundWidth = ViewUtil.dp2px(getContext(), 5);
    private float mCompileRoundWidth = ViewUtil.dp2px(getContext(), 10);
    
    private Paint mPaint;
    private Paint bitmapPaint;
    private int mWidth;
    private int mHeight;
    private int sliderR;//滑块半径
    private int mProgerss=0;
    private int radius = 50; //弧度
    
	private String title = "滑动解锁 >>";
	/*title颜色与大小*/
	private int mTextColor = Color.parseColor("#999999");
    private float mTextSize = ViewUtil.sp2px(getContext(), 15);

    private int animTime = 200;

	private ProgressListener listener;

	private int id = 0;
	private float titleWidth = 0;

	private boolean isDownSilder = false;


	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}


	public void setSliderColor(int touchColor) {
		this.sliderColor = touchColor;
	}

	public ProgressListener getListener() {
		return listener;
	}

	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}



	public SliderView(Context context) {
        this(context,null);
    }

    public SliderView(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }
    

    public void setCircleProgress(int progress){
    	this.mProgerss=progress;
    }
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		if (title!=null) {
			this.title = title;
			
		}
	}

	public int getDefaultRoundColor() {
		return mDefaultRoundColor;
	}

	public void setDefaultRoundColor(int mRoundBgColor) {
		this.mDefaultRoundColor = mRoundBgColor;
	}

	public int getCompileRoundColor() {
		return mCompileRoundColor;
	}

	public void setCompileRoundColor(int mRoundColor) {
		this.mCompileRoundColor = mRoundColor;
	}

	public float getRoundWidth() {
		return mRoundWidth;
	}

	public void setRoundWidth(float mRoundWidth) {
		this.mRoundWidth = mRoundWidth;
	}

	public float getCompileRoundWidth() {
		return mCompileRoundWidth;
	}

	public void setCompileRoundWidth(float mPassRoundWidth) {
		this.mCompileRoundWidth = mPassRoundWidth;
	}

	public int getProgerss() {
		return mProgerss;
	}

	public int getTitleColor() {
		return mTextColor;
	}

	public void setTitleColor(int mTextColor) {
		this.mTextColor = mTextColor;
	}

	public float getTextSize() {
		return mTextSize;
	}

	public void setTitleSize(float mTextSize) {
		this.mTextSize = mTextSize;
	}

	public int getAnimTime() {
		return animTime;
	}

	public void setAnimTime(int animTime) {
		this.animTime = animTime;
	}

	public SliderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();//初始化画笔
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapPaint = new Paint();
        //线头设置未圆头
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        
    }
    
    boolean isFirst=true;
    @SuppressLint("DrawAllocation")
	@Override
    protected void
	onDraw(Canvas canvas) {
    	mWidth = getWidth() ;
    	mHeight = getHeight();
    	sliderR = Math.min(mWidth,mHeight)/2;

		Path mPath = new Path();
		mPath.addRoundRect(new RectF(0, 0, mWidth, mHeight), radius, radius, Path.Direction.CW);
		canvas.clipPath(mPath);

		changeProgress(mProgerss);

		//1、先画背景
		RectF rectF = new RectF(0,0,mWidth,mHeight);
		mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mDefaultRoundColor);
        mPaint.setStrokeWidth(0);
//        mPaint.setStrokeWidth(mHeight);
//		canvas.drawLine(sliderR,mHeight/2,mWidth-sliderR,mHeight/2,mPaint);
//		canvas.drawCircle(mWidth-sliderR,mHeight/2,sliderR,mPaint);
//		canvas.drawRoundRect(rectF, radian, radian,mPaint);
		canvas.drawRect(rectF,mPaint);
        //2、画已滑动背景
		int right = mProgerss;
		if (mProgerss != 0){
			right += sliderR;
		}
		RectF rectFSelect = new RectF(0,0,right,mHeight);
        mPaint.setColor(mCompileRoundColor);
//		canvas.drawCircle(sliderR,mHeight/2,sliderR,mPaint);
//		canvas.drawLine(sliderR, mHeight/2,mProgerss,mHeight/2, mPaint);

//		canvas.drawRoundRect(rectFSelect, radian, radian,mPaint);
		canvas.drawRect(rectFSelect,mPaint);
        //title
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(0);//如果不设置回0，很难看
        mPaint.setTextSize(mTextSize);
        //测量字体的宽度
        titleWidth = mPaint.measureText(title);
		Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        canvas.drawText(title,mWidth/2-titleWidth/2, ((mHeight - fontMetrics.bottom - fontMetrics.top) / 2),mPaint);
        //画滑块
		RectF rectFSlider = new RectF(mProgerss,0,mProgerss+mHeight,mHeight);
		mPaint.setColor(sliderColor);
//		canvas.drawCircle(mProgerss,mHeight/2,sliderR,mPaint);
		canvas.drawRoundRect(rectFSlider, radius, radius,mPaint);


    }

	private void changeProgress(int progress) {
		if (progress<0){
			mProgerss = 0;
		}else if (progress > mWidth-mHeight){
			mProgerss = mWidth-mHeight;
		}else{
			mProgerss = progress;
		}
	}

	@SuppressLint("NewApi")
	private void startAnim(int progress){

		ValueAnimator aimAnimator=ValueAnimator.ofInt(mProgerss,progress);
    	aimAnimator.addUpdateListener(new AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				// TODO Auto-generated method stub
				mProgerss=(int) animation.getAnimatedValue();
				
				invalidate();
			}
		});
    	aimAnimator.setDuration(animTime);
    	aimAnimator.start();

    }

	private float mPreX;
	@Override
	public boolean onTouchEvent(MotionEvent event){
		switch (event.getAction() & event.getActionMasked())
		{
			case MotionEvent.ACTION_DOWN:
				if (isInFinalPoint(event.getX(), event.getY()))
				{
					mPreX = event.getX();
					isDownSilder = true;
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (isDownSilder){
					float moveX = event.getX();
					float change = moveX-mPreX;
//					mPreX = moveX;
					changeProgress((int) change);
					if (listener != null){
//						listener.onProgressChanged((mProgerss-sliderR)*100/(mWidth-sliderR*2));
						listener.onProgressChanged(mProgerss*100/(mWidth-mHeight));
					}
					invalidate();
				}
				break;

			case MotionEvent.ACTION_UP:
				if (isDownSilder){
					isDownSilder = false;
//					if (mProgerss > mWidth/2){
//						startAnim(mWidth-sliderR);
//					}else{
						startAnim(0);
//					}
				}
				break;
		}
		return true;
	}

	//是否在进度最后触摸点
	private boolean isInFinalPoint(float x, float y){
		float w = Math.abs(mProgerss+sliderR-x);
		float h = Math.abs(mHeight/2-y);
		if (Math.sqrt((w*w) + (h*h))<sliderR){
			return true;
		}
		return false;
	}


	@Override
    protected void onDetachedFromWindow() {
    	// TODO Auto-generated method stub
    	super.onDetachedFromWindow();
    }

}
