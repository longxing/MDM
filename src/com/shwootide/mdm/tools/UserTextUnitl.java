package com.shwootide.mdm.tools;

import java.util.Vector;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;

public class UserTextUnitl {

	private int mTextPosx = 0;// x坐标
	private int mTextPosy = 0;// y坐标
	private int mTextWidth = 0;// 绘制宽度
	private int mTextHeight = 0;// 绘制高度
	private int mFontHeight = 0;// 绘制字体高度
	private int mPageLineNum = 0;// 每一页显示的行数
	private int mCanvasBGColor = 0;// 背景颜色
	private int mFontColor = 0;// 字体颜色
	private int mAlpha = 0;// Alpha�?
	private int mRealLine = 0;// 字符串真实的行数
	private int mCurrentLine = 0;// 当前�?
	private int mTextSize = 0;// 字体大小
	private String mStrText = "";
	private Vector mString = null;
	private Paint mPaint = null;
	/**
	 * 
	 * @param StrText 文本
	 * @param x x坐标
	 * @param y y坐标
	 * @param w 绘制宽度
	 * @param h 绘制高度
	 * @param bgcolor 背景颜色
	 * @param textcolor 字体颜色
	 * @param alpha Alpha�?
	 * @param textsize 字体大小
	 */
	public UserTextUnitl(String StrText, int x, int y, int w, int h, int bgcolor,
			int textcolor, int alpha, int textsize) {
		mPaint = new Paint();
		mString = new Vector();
		this.mStrText = StrText;
		this.mTextPosx = x;
		this.mTextPosy = y;
		this.mTextWidth = w;
		this.mTextHeight = h;
		this.mCanvasBGColor = bgcolor;
		this.mFontColor = textcolor;
		this.mAlpha = alpha;
		this.mTextSize = textsize;
	}

	public void InitText() {
		mString.clear();// 清空
		// 对画笔属性的设置
		mPaint.setTextSize(this.mTextSize);
		mPaint.setColor(Color.BLACK);//画笔颜色
		//设置粗体
		mPaint.setStrokeWidth(3.5f);//�߿�
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);//�����
//		Typeface font = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
//		Typeface font = Typeface.create(Typeface.DEFAULT,Typeface.NORMAL);
//		mPaint.setTypeface( font );
//		mPaint.setFakeBoldText(true);
//		mPaint.setStrokeWidth(2.5f);
		this.GetTextIfon();
	}

	/**
	 * 得到字符串信息包括行数，页数等信�?
	 */
	public void GetTextIfon() {
		char ch;
		int w = 0;
		int istart = 0;
		FontMetrics fm = mPaint.getFontMetrics();// 系统默认字体属�?
		mFontHeight = (int) (Math.ceil(fm.descent - fm.top) + 2);// 字体高度
		mPageLineNum = mTextHeight / mFontHeight;// 行数
		int count = this.mStrText.length();
		for (int i = 0; i < count; i++) {
			ch = this.mStrText.charAt(i);
			float[] widths = new float[1];
			String str = String.valueOf(ch);
			mPaint.getTextWidths(str, widths);
			if (ch == '\n') {
				mRealLine++;// 行数加一
				mString.addElement(this.mStrText.substring(istart, i));
				istart = i + 1;
				w = 0;
			} else {
				w += (int) Math.ceil(widths[0]);
				if (w > this.mTextWidth) {
					mRealLine++;// 行数加一
					mString.addElement(this.mStrText.substring(istart, i));
					istart = i;
					i--;
					w = 0;
				} else {
					if (i == count - 1) {
						mRealLine++;// 行数加一
						mString.addElement(this.mStrText.substring(istart,
								count));
					}
				}
			}
		}
	}

	/**
	 * 字符�?
	 * 
	 * @param canvas
	 */
	public void DrawText(Canvas canvas) {
		for (int i = this.mCurrentLine, j = 0; i < this.mRealLine; i++, j++) {
			if (j > this.mPageLineNum) {
				break;
			}
			canvas.drawText((String) (mString.elementAt(i)), this.mTextPosx,
					this.mTextPosy + this.mFontHeight * j, mPaint);
		}
	}
}
