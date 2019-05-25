package com.ydn.viewpagerwithicons;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.Log;
import java.io.Serializable;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;
import static android.view.Gravity.TOP;
import static android.view.Gravity.BOTTOM;

class Icon implements Serializable {
    private static final String TAG = "Icon";
    private static final boolean DEBUG = false;

    private int mId;
    private int mLeft;
    private int mTop;
    private int mBorderWidth;
    private int mWidth;
    private int mHeight;

    private int mColor;
    private int mNumberColor;
    private int mBorderColor;
    private int mCheckmarkColor;

    private boolean mIsGradientColor;
    private boolean mIsShowNumber;

    private String mText = "";
    private int mTextGravity = CENTER;
    private int mTextSize;
    private int mTextColor;
    private int mTextStyle;
    private int mTextMargin;

    private boolean mShowCheckmark;
    private boolean mIsDottedStyle;
    private boolean mIsSquare;

    private ProxyBitmap mProxyBitmap;
    private ProxyBitmap mIndividualProxyBitmap;

    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    Icon(int left, int top, int width, int height) {
        mLeft = left;
        mTop = top;
        mWidth = width;
        mHeight = height;
        mTextPaint.setTextAlign(Paint.Align.LEFT);
    }

    String getText() {
        return mText;
    }

    void setText(String text) {
        mText = text;
    }

    boolean isGradient() {
        return mIsGradientColor;
    }

    void setGradient(boolean gradient) {
        mIsGradientColor = gradient;
    }

    void locate(int left, int top) {
        if (DEBUG) Log.i(TAG, "locate() " + left + " " + top + " " + mWidth + " " + mHeight);

        mLeft = left;
        mTop = top;
    }

    int getWidth() {
        return mWidth;
    }

    @SuppressLint("WrongConstant")
    int getTextWidth() {
        Rect r = new Rect();

        if (getTextGravity() == LEFT || getTextGravity() == RIGHT) {
            mTextPaint.setTextSize(getTextSize());
            mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, getTextStyle()));
            mTextPaint.getTextBounds(getText(), 0, getText().length(), r);
            return r.width() + mTextMargin;
        }
        return 0;
    }

    @SuppressLint("WrongConstant")
    int getTextHeight() {
        Rect r = new Rect();

        if (getTextGravity() == TOP || getTextGravity() == BOTTOM) {
            mTextPaint.setTextSize(getTextSize());
            mTextPaint.setTypeface(Typeface.create(Typeface.DEFAULT, getTextStyle()));
            mTextPaint.getTextBounds(getText(), 0, getText().length(), r);
            return r.height() + mTextMargin;
        }
        return 0;
    }

    void setWidth(int width) {
        mWidth = width;
    }

    void setHeight(int height) {
        mHeight = height;
    }

    int getHeight() {
        return mHeight;
    }

    int getLeft() {
        return mLeft;
    }

    void setTop(int top) {
        mTop = top;
    }

    int getTop() {
        return mTop;
    }

    void setColor(int color) {
        mColor = color;
    }

    int getColor() {
        return mColor;
    }

    void setBitmap(Bitmap b) {
        if (b != null) {
            mProxyBitmap = new ProxyBitmap(b);
        } else {
            mProxyBitmap = null;
        }
    }

    Bitmap getBitmap() {
        return mProxyBitmap != null ? mProxyBitmap.getBitmap() : null;
    }

    Bitmap getIndividualBitmap() {
        return mIndividualProxyBitmap != null ? mIndividualProxyBitmap.getBitmap() : null;
    }

    void setIndividualBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            mIndividualProxyBitmap = new ProxyBitmap(bitmap);
        } else {
            mIndividualProxyBitmap = null;
        }
    }

    int getTextGravity() {
        return mTextGravity;
    }

    void setTextGravity(int textGravity) {
        mTextGravity = textGravity;
    }

    int getTextSize() {
        return mTextSize;
    }

    void setTextSize(int textSize) {
        mTextSize = textSize;
    }

    int getTextColor() {
        return mTextColor;
    }

    void setTextColor(int textColor) {
        mTextColor = textColor;
    }

    int getTextStyle() {
        return mTextStyle;
    }

    void setTextStyle(int textStyle) {
        mTextStyle = textStyle;
    }

    void setBorderWidth(int borderWidth) {
        mBorderWidth = borderWidth;
    }

    int getBorderWidth() {
        return mBorderWidth;
    }

    int getBorderColor() {
        return mBorderColor;
    }

    void setBorderColor(int borderColor) {
        mBorderColor = borderColor;
    }

    int getTextMargin() {
        return mTextMargin;
    }

    void setTextMargin(int textMargin) {
        mTextMargin = textMargin;
    }

    boolean isShowCheckmark() {
        return mShowCheckmark;
    }

    void setShowCheckmark(boolean showCheckmark) {
        mShowCheckmark = showCheckmark;
    }

    int getCheckmarkColor() {
        return mCheckmarkColor;
    }

    void setCheckmarkColor(int CheckmarkColor) {
        mCheckmarkColor = CheckmarkColor;
    }

    boolean isDottedStyle() {
        return mIsDottedStyle;
    }

    void setDottedStyle(boolean dottedStyle) {
        mIsDottedStyle = dottedStyle;
    }

    boolean isSquare() {
        return mIsSquare;
    }

    void setSquare(boolean square) {
        mIsSquare = square;
    }

    boolean isShowNumber() {
        return mIsShowNumber;
    }

    void setShowNumber(boolean showNumber) {
        this.mIsShowNumber = showNumber;
    }

    int getId() {
        return mId;
    }

    void setId(int id) {
        this.mId = id;
    }

    int getNumberColor() {
        return mNumberColor;
    }

    void setNumberColor(int numberColor) {
        this.mNumberColor = numberColor;
    }

    private class ProxyBitmap implements Serializable {
        private final int [] pixels;
        private final int width , height;

        ProxyBitmap(Bitmap bitmap){
            width = bitmap.getWidth();
            height = bitmap.getHeight();
            pixels = new int [width*height];
            bitmap.getPixels(pixels,0,width,0,0,width,height);
        }

        Bitmap getBitmap(){
            return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888);
        }
    }
}

