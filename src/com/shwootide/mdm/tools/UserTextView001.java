package com.shwootide.mdm.tools;

import com.shwootide.mdm.common.Configuration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;


public class UserTextView001 extends View{
	public  String TAG= "UserTextView";
	public static String str001 = "　　电梯维保管家是为电梯行业维保工程师量身打造的移动办公应用。依靠强大的云管理系统，它可以实现维保任务实时下载，维保现场二维码签到，远程提交急修，保养工单，合同管理，工作提醒，库存管理，电梯档案，统计分析，现场拍照上传等功能。"
			+ "\n　　可以使用体验账号了解我们的产品：\n　　公司名：e0010\n　　用户名：test001\n　　密　码：test123\n";//产品介绍
	
	
	private Paint mPaint;
	private int mICount = 0;
	private UserTextUnitl mTextUtil;
	public UserTextView001(Context context) {
		super(context);
//		this(context,null);
	}
	
	public UserTextView001(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public UserTextView001(Context context, AttributeSet attrs) {
//		this(context,attrs,0);
		super(context);
	}
	
	private void init() {
//		mPaint.setAntiAlias(true);
//		mPaint.setColor(Color.WHITE); 
//		mPaint.setStyle(Style.STROKE);
//		mPaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_DIP, 15));
		
		
		
	}
	/**
	 * 绘制布局
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		mPaint = new Paint();
		
		String string = str001;
		mTextUtil = new UserTextUnitl(string, 15, 20, Configuration.width-20, Configuration.height, 0xffffff, 0x000000,
				255, 18);
		mTextUtil.InitText();
//		// 设置背景颜色
		canvas.drawColor(Color.parseColor("#ffffff"));//
//		mPaint.setAntiAlias(true);//设置抗锯�?
//		TextView tv = new TextView(getContext());
		
		Typeface font = Typeface.create(Typeface.DEFAULT,Typeface.BOLD);//加粗看看
//		Typeface font = Help.font;
//		font = tv.getTypeface();//这种方式得到的默认字体看行不�?
//		Log.e(TAG,String.valueOf(font.getStyle()));
//		mPaint.setTypeface( font );
		//
		mTextUtil.DrawText(canvas);
	}
	
	//设置自定义的View的宽和高,这两个值要想办法计算出来
//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		// TODO Auto-generated method stub
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);//这里没有重写，被覆盖了
//		
//	}
	
	
}