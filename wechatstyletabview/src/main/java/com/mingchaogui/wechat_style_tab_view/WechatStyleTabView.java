package com.mingchaogui.wechat_style_tab_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class WechatStyleTabView extends View {

    // 默认的文本颜色
    private static final int DEFAULT_TEXT_COLOR = Color.BLACK;
    // 填色模式
    public enum FillColorMode {
        STATE,// 根据Tab的状态值填色
        ALPHA// 根据FillAlpha值填色
    }

	private Paint mPaint = new Paint();
    private Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);
	private Paint mTextPaint = new TextPaint();
    private Paint.FontMetrics mFontMetrics = new Paint.FontMetrics();
    // 填色模式
    private FillColorMode mFillColorMode = FillColorMode.STATE;
    // 填色透明度
	private int mFillAlpha = 0;
    // 图标
	private Bitmap mIconBitmap;
    // 图标绘制范围
	private Rect mIconRect = new Rect();
    // 文本
	private String mText = "";
    // 文本绘制范围
	private Rect mTextRect = new Rect();

	public WechatStyleTabView(Context context) {
		super(context);
	}

	public WechatStyleTabView(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WechatStyleTabView);
		for (int i = 0; i < ta.getIndexCount(); i++) {
			int attr = ta.getIndex(i);

			if (attr == R.styleable.WechatStyleTabView_fill_color) {
				int color = ta.getColor(attr, DEFAULT_TEXT_COLOR);
                setFillColor(color);
			} else if (attr == R.styleable.WechatStyleTabView_tab_icon) {
				BitmapDrawable drawable = (BitmapDrawable) ta.getDrawable(attr);
				if (drawable != null) {
                    setTabIcon(drawable.getBitmap());
				}
			} else if (attr == R.styleable.WechatStyleTabView_tab_text) {
				String text = ta.getString(attr);
                setTabText(text);
			} else if (attr == R.styleable.WechatStyleTabView_tab_text_size) {
				float textSize = ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics()));
                setTabTextSize(textSize);
			}
		}
		ta.recycle();

        // 开启图像抖动处理
        mPaint.setDither(true);
        // 开启反锯齿
        mTextPaint.setAntiAlias(true);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;
        switch (widthSpecMode) {
            case MeasureSpec.AT_MOST:
                width = Math.min(widthSpecSize, getWrapWidth());
                break;

            case MeasureSpec.EXACTLY:
                width = widthSpecSize;
                break;

            case MeasureSpec.UNSPECIFIED:
                width = getWrapWidth();
                break;

            default:
                width = getWrapWidth();
                break;
        }
        switch (heightSpecMode) {
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSpecSize, getWrapHeight());
                break;

            case MeasureSpec.EXACTLY:
                height = heightSpecSize;
                break;

            case MeasureSpec.UNSPECIFIED:
                height = getWrapHeight();
                break;

            default:
                height = getWrapHeight();
                break;
        }
        setMeasuredDimension(width, height);
	}

    private int getWrapWidth() {
        int iconWidth = mIconBitmap == null ? 0 : mIconBitmap.getWidth();
        int textWidth = mTextRect.width();

		return Math.max(iconWidth, textWidth) + getPaddingLeft() + getPaddingRight();
    }

    private int getWrapHeight() {
        int iconHeight = mIconBitmap == null ? 0 : mIconBitmap.getHeight();
        int textHeight = mTextRect.height();

		return iconHeight + textHeight + getPaddingTop() + getPaddingBottom();
    }

	@Override
	protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 计算图标的Rect
        int iconWidth = mIconBitmap.getWidth();
        int iconHeight = mIconBitmap.getHeight();
        mIconRect.left = (getWidth() - iconWidth) / 2;
        mIconRect.right = mIconRect.left + iconWidth;
        mIconRect.top = (getHeight() - iconHeight - mTextRect.height()) / 2;
        mIconRect.bottom = mIconRect.top + iconHeight;
        // 获取文字的Rect
        mTextPaint.getTextBounds(mText, 0, mText.length(), mTextRect);
        // 计算文字的坐标
        mTextPaint.getFontMetrics(mFontMetrics);
        float textHeight = mFontMetrics.bottom - mFontMetrics.top;
        float textBaseLine = textHeight - (mFontMetrics.bottom - mFontMetrics.descent);
        float textX = (getWidth() - mTextRect.width()) / 2.0f;
        float textY = mIconRect.bottom + textBaseLine;

        // 绘制图标
        canvas.drawBitmap(mIconBitmap, null, mIconRect, null);
        // 绘制文字
        mTextPaint.setColor(DEFAULT_TEXT_COLOR);
        mTextPaint.setAlpha(255 - mFillAlpha);
        canvas.drawText(mText, textX, textY, mTextPaint);

        // 判断是否需要填色，并设置填色透明度
        boolean drawColorLayer = false;
        switch (mFillColorMode) {
            case STATE:
                if (isSelected()) {
                    drawColorLayer = true;
                    mPaint.setAlpha(255);
                    mTextPaint.setAlpha(255);
                }

                break;

            case ALPHA:
                if (mFillAlpha != 0) {
                    drawColorLayer = true;
                    mPaint.setAlpha(mFillAlpha);
                    mTextPaint.setAlpha(mFillAlpha);
                }

                break;
        }
        if (drawColorLayer) {
            int save = canvas.saveLayer(0, 0, canvas.getWidth(), canvas.getHeight(), null, Canvas.ALL_SAVE_FLAG);

            // 绘制图标颜色
            canvas.drawRect(mIconRect, mPaint);
            mPaint.setXfermode(mXfermode);
            canvas.drawBitmap(mIconBitmap, null, mIconRect, mPaint);
            mPaint.setXfermode(null);
            // 绘制文字颜色
            mTextPaint.setColor(mPaint.getColor());
            canvas.drawText(mText, textX, textY, mTextPaint);

            canvas.restoreToCount(save);
        }
	}

    public void setFillColor(int color) {
        if (mPaint.getColor() != color) {
            mPaint.setColor(color);
            invalidate();
        }
    }

    public void setTabIcon(int resId) {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resId);
        setTabIcon(bitmap);
    }

    public void setTabIcon(Bitmap iconBitmap) {
        if (mIconBitmap != iconBitmap) {
            mIconBitmap = iconBitmap;
            invalidate();
        }
    }

    public void setTabText(String text) {
        if (!mText.equals(text)) {
            mText = text == null ? "" : text;
            invalidate();
        }
    }

    public void setTabTextSize(float textSize) {
        if (textSize != textSize) {
            mTextPaint.setTextSize(textSize);
            invalidate();
        }
    }

    public void setFillColorMode(FillColorMode mode) {
        if (mFillColorMode != mode) {
            mFillColorMode = mode;
            invalidate();
        }
    }

    public int getColorAlpha() {
        return mFillAlpha;
    }

	public void setColorAlpha(int alpha) {
        if (mFillAlpha != alpha) {
            mFillAlpha = alpha;
            invalidate();
        }
	}
}
