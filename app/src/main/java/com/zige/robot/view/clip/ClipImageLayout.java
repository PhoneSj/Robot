package com.zige.robot.view.clip;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.RelativeLayout;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 * @author zhy
 *
 */
public class ClipImageLayout extends RelativeLayout
{

	private ClipZoomImageView mZoomImageView;
	private ClipImageBorderView mClipImageView;
	private int mHorizontalPadding = 50;
	private int resID;

	public ClipImageLayout(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mZoomImageView = new ClipZoomImageView(context);
		mClipImageView = new ClipImageBorderView(context);

		android.view.ViewGroup.LayoutParams lp = new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT);
		if(resID >0){
			mZoomImageView.setImageResource(resID);

		}
		this.addView(mZoomImageView, lp);
		this.addView(mClipImageView, lp);

		
		// 计算padding的px
		mHorizontalPadding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
						.getDisplayMetrics());
		mZoomImageView.setHorizontalPadding(mHorizontalPadding);
		mClipImageView.setHorizontalPadding(mHorizontalPadding);
	}




	public void setImageResource(int resID){
		this.resID =resID;
		if(resID >0){
			mZoomImageView.setImageResource(resID);

		}
	}
	public void setImage(Bitmap bitmap){
		mZoomImageView.setImageBitmap(bitmap);
	}

	/**
	 * 裁切图片
	 * 
	 * @return
	 */
	public Bitmap clip()
	{
		return mZoomImageView.clip();
	}

}
