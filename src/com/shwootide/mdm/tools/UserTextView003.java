package com.shwootide.mdm.tools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.shwootide.mdm.common.Configuration;

public class UserTextView003 
extends View{
	public  String TAG= "UserTextView";
//	private String str001 = "　　电梯维保管家是为电梯行业维保工程师量身打造的移动办公应用。依靠强大的云管理系统，它可以实现维保任务实时下载，维保现场二维码签到，远程提交急修，保养工单，合同管理，工作提提醒，库存管理，电梯档案，统计分析，现场拍照上传等功能。"
//			+ "\n　　可以使用体验账号了解我们的产品：\n　　公司名：e0010\n　　用户名：test001\n　　密　   码：test123\n";//产品介绍
	
	public static String str001 = "　　上海物泰信息科技有限公司是一家提供移动互联网和无线传感网应用解决方案的科技公司。我们致力于手机应用定制开发，为客户量身打造苹果和安卓平台下的应用。\n\n　　固定电话：021-60753335\n　　邮        箱：sales@shwootide.com";//关于我们
	
	private Paint mPaint;
	private int mICount = 0;
	private UserTextUnitl mTextUtil;
	public UserTextView003(Context context) {
//		super(context);
		this(context,null);
	}
	
	public UserTextView003(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public UserTextView003(Context context, AttributeSet attrs) {
		this(context,attrs,0);
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
		mPaint.setTypeface( font );
		mTextUtil.DrawText(canvas);
	}
	
}