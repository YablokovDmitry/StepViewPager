package com.ydn.viewpagerwithicons;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Scroller;
import java.lang.reflect.Field;

import static android.graphics.Paint.ANTI_ALIAS_FLAG;
import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;
import static android.view.Gravity.TOP;
import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.LEFT;
import static android.view.Gravity.RIGHT;
import static android.view.Gravity.CENTER;
import static com.google.common.base.Preconditions.checkNotNull;

public class StateViewPager extends ViewPager implements PagerContract.View {
    private static final String TAG = "CustomViewPager";
    private static final boolean DEBUG = false;

    private static final int SCROLL_DISTANCE = 15;

    private PagerContract.Presenter mPagerPresenter;
    private Canvas mCanvas;

    private boolean mIsTouched;
    private boolean mIsScrolling;

    private Point mStartPoint;
    private Point mCurrPoint;

    private int mCurrPage;
    private float mTouchedX;
    private float mTouchedY;

    private int mOrientation;
    private int mGravity;

    private final Paint mPaintText = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintNumber = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintIcon = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintCheckmark = new Paint(ANTI_ALIAS_FLAG);
    private final Paint mPaintBitmap = new Paint();

    public StateViewPager(@NonNull Context context) {
        super(context);
        setSaveEnabled(true);
        init();
    }

    public StateViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setSaveEnabled(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateViewPager, 0, 0);

        mOrientation = a.getInt(R.styleable.StateViewPager_android_orientation, HORIZONTAL);
        mGravity = getGravity(a.getString(R.styleable.StateViewPager_iconsGravity), mOrientation);

        int numOfIcons = a.getInt(R.styleable.StateViewPager_numOfIcons, 0);

        int iconWidth = a.getDimensionPixelSize(R.styleable.StateViewPager_iconWidth, 150);
        int iconHeight = a.getDimensionPixelSize(R.styleable.StateViewPager_iconHeight, 150);
        int marginBetweenIcons = a.getDimensionPixelSize(R.styleable.StateViewPager_marginBetweenIcons, 50);

        int visitedBorderWidth = a.getDimensionPixelSize(R.styleable.StateViewPager_visitedBorderWidth, 0);
        int selectedBorderWidth = a.getDimensionPixelSize(R.styleable.StateViewPager_selectedBorderWidth, 0);
        int unvisitedBorderWidth = a.getDimensionPixelSize(R.styleable.StateViewPager_unvisitedBorderWidth, 0);

        int selectedWidth = a.getDimensionPixelSize(R.styleable.StateViewPager_selectedWidth, 150);
        int selectedHeight = a.getDimensionPixelSize(R.styleable.StateViewPager_selectedHeight, 150);
        int intermediateWidth = a.getDimensionPixelSize(R.styleable.StateViewPager_intermediateWidth, 0);
        int intermediateHeight = a.getDimensionPixelSize(R.styleable.StateViewPager_intermediateHeight, 0);

        int leftMargin = a.getDimensionPixelSize(R.styleable.StateViewPager_leftMargin, 25);
        int topMargin = a.getDimensionPixelSize(R.styleable.StateViewPager_topMargin, 25);
        int rightMargin = a.getDimensionPixelSize(R.styleable.StateViewPager_rightMargin, 25);
        int bottomMargin = a.getDimensionPixelSize(R.styleable.StateViewPager_bottomMargin, 25);

        boolean visitedGradient = a.getBoolean(R.styleable.StateViewPager_visitedGradientFill, false);
        boolean unvisitedGradient = a.getBoolean(R.styleable.StateViewPager_unvisitedGradientFill, false);
        boolean selectedGradient = a.getBoolean(R.styleable.StateViewPager_selectedGradientFill, false);

        boolean visitedIntermediateGradient = a.getBoolean(R.styleable.StateViewPager_visitedIntermediateGradientFill, false);
        boolean unvisitedIntermediateGradient = a.getBoolean(R.styleable.StateViewPager_unvisitedIntermediateGradientFill, false);

        int visitedTextGravity = getGravity(a.getString(R.styleable.StateViewPager_visitedTextGravity), -1);
        int visitedTextSize = a.getDimensionPixelSize(R.styleable.StateViewPager_visitedTextSize, 15);
        int visitedTextStyle = a.getInt(R.styleable.StateViewPager_visitedTextStyle, 0);
        int visitedTextColor = a.getColor(R.styleable.StateViewPager_visitedTextColor, Color.WHITE);
        int visitedTextMargin = a.getDimensionPixelSize(R.styleable.StateViewPager_visitedTextMargin, 10);

        int selectedTextGravity = getGravity(a.getString(R.styleable.StateViewPager_selectedTextGravity), -1);
        int selectedTextSize = a.getDimensionPixelSize(R.styleable.StateViewPager_selectedTextSize, 15);
        int selectedTextStyle = a.getInt(R.styleable.StateViewPager_selectedTextStyle, 0);
        int selectedTextColor = a.getColor(R.styleable.StateViewPager_selectedTextColor, Color.WHITE);
        int selectedTextMargin = a.getDimensionPixelSize(R.styleable.StateViewPager_selectedTextMargin, 10);

        int unvisitedTextGravity = getGravity(a.getString(R.styleable.StateViewPager_unvisitedTextGravity), -1);
        int unvisitedTextSize = a.getDimensionPixelSize(R.styleable.StateViewPager_unvisitedTextSize, 15);
        int unvisitedTextStyle = a.getInt(R.styleable.StateViewPager_unvisitedTextStyle, 0);
        int unvisitedTextColor = a.getColor(R.styleable.StateViewPager_unvisitedTextColor, Color.WHITE);
        int unvisitedTextMargin = a.getDimensionPixelSize(R.styleable.StateViewPager_unvisitedTextMargin, 10);

        int selectedColor = a.getColor(R.styleable.StateViewPager_selectedColor, Color.WHITE);
        int visitedColor = a.getColor(R.styleable.StateViewPager_visitedColor, Color.LTGRAY);
        int unvisitedColor = a.getColor(R.styleable.StateViewPager_unvisitedColor, Color.LTGRAY);

        int selectedCheckmarkColor = a.getColor(R.styleable.StateViewPager_selectedCheckmarkColor, Color.parseColor("#0CCCDF"));
        int visitedCheckmarkColor = a.getColor(R.styleable.StateViewPager_visitedCheckmarkColor, Color.parseColor("#0CCCDF"));
        int unvisitedCheckmarkColor = a.getColor(R.styleable.StateViewPager_unvisitedCheckmarkColor, Color.WHITE);

        int selectedNumberColor = a.getColor(R.styleable.StateViewPager_selectedNumberColor, Color.WHITE);
        int visitedNumberColor = a.getColor(R.styleable.StateViewPager_visitedNumberColor, Color.WHITE);
        int unvisitedNumberColor = a.getColor(R.styleable.StateViewPager_unvisitedNumberColor, Color.WHITE);

        boolean showSelectedCheckmark = a.getBoolean(R.styleable.StateViewPager_showSelectedCheckmark, false);
        boolean showVisitedCheckmark = a.getBoolean(R.styleable.StateViewPager_showVisitedCheckmark, false);
        boolean showUnvisitedCheckmark = a.getBoolean(R.styleable.StateViewPager_showUnvisitedCheckmark, false);

        boolean showSelectedNumber = a.getBoolean(R.styleable.StateViewPager_showSelectedNumber, false);
        boolean showVisitedNumber = a.getBoolean(R.styleable.StateViewPager_showVisitedNumber, false);
        boolean showUnvisitedNumber = a.getBoolean(R.styleable.StateViewPager_showUnvisitedNumber, false);

        int visitedIntermediateColor = a.getColor(R.styleable.StateViewPager_visitedIntermediateColor, Color.WHITE);
        int unvisitedIntermediateColor = a.getColor(R.styleable.StateViewPager_unvisitedIntermediateColor, Color.WHITE);

        int selectedBorderColor = a.getColor(R.styleable.StateViewPager_selectedBorderColor, Color.WHITE);
        int visitedBorderColor = a.getColor(R.styleable.StateViewPager_visitedBorderColor, Color.WHITE);
        int unvisitedBorderColor = a.getColor(R.styleable.StateViewPager_unvisitedBorderColor, Color.WHITE);

        String visitedIntermediatdStyle = a.getString(R.styleable.StateViewPager_visitedIntermediateStyle);
        String unvisitedIntermediatdStyle = a.getString(R.styleable.StateViewPager_unvisitedIntermediateStyle);

        boolean rectangularVisitedIcon = a.getBoolean(R.styleable.StateViewPager_rectangularVisitedIcon, false);
        boolean rectangularUnvisitedIcon = a.getBoolean(R.styleable.StateViewPager_rectangularUnvisitedIcon, false);
        boolean rectangularSelectedIcon = a.getBoolean(R.styleable.StateViewPager_rectangularSelectedIcon, false);

        Drawable visitedDrawable = a.getDrawable(R.styleable.StateViewPager_visitedDrawable);
        Drawable unvisitedDrawable = a.getDrawable(R.styleable.StateViewPager_unvisitedDrawable);
        Drawable selectedDrawable = a.getDrawable(R.styleable.StateViewPager_selectedDrawable);
        Drawable visitedIntermediateDrawable = a.getDrawable(R.styleable.StateViewPager_visitedIntermediateDrawable);
        Drawable unvisitedIntermediateDrawable = a.getDrawable(R.styleable.StateViewPager_unvisitedIntermediateDrawable);

        Bitmap visitedBitmap = visitedDrawable != null ? getBitmapFromDrawable(visitedDrawable) : null;
        Bitmap unvisitedBitmap = unvisitedDrawable != null ? getBitmapFromDrawable(unvisitedDrawable) : null;
        Bitmap selectedBitmap = selectedDrawable != null ? getBitmapFromDrawable(selectedDrawable) : null;
        Bitmap visitedIntermediateBitmap = visitedIntermediateDrawable != null ? getBitmapFromDrawable(visitedIntermediateDrawable) : null;
        Bitmap unvisitedIntermediateBitmap = unvisitedIntermediateDrawable != null ? getBitmapFromDrawable(unvisitedIntermediateDrawable) : null;

        CharSequence[] entries = a.getTextArray(R.styleable.StateViewPager_android_entries);

        a.recycle();

        mPagerPresenter = new PagerPresenter(this);
        mPagerPresenter.setOrientation(mOrientation);
        mPagerPresenter.setBitmaps(visitedBitmap, selectedBitmap, unvisitedBitmap);
        mPagerPresenter.setIntermediateBitmaps(visitedIntermediateBitmap, unvisitedIntermediateBitmap);
        mPagerPresenter.setIconSize(iconWidth, iconHeight);
        mPagerPresenter.setMarginBetweenIcons(marginBetweenIcons);
        mPagerPresenter.setSelectedIconSize(selectedWidth, selectedHeight);
        mPagerPresenter.setIntermediateIconSize(intermediateWidth, intermediateHeight);
        mPagerPresenter.setGravity(mGravity);
        mPagerPresenter.setTextGravities(visitedTextGravity, selectedTextGravity, unvisitedTextGravity);
        mPagerPresenter.setIconColors(visitedColor, selectedColor, unvisitedColor);
        mPagerPresenter.setGradientColors(visitedGradient, selectedGradient, unvisitedGradient);
        mPagerPresenter.setIntermediateIconColors(visitedIntermediateColor, unvisitedIntermediateColor);
        mPagerPresenter.setIntermediateGradients(visitedIntermediateGradient, unvisitedIntermediateGradient);
        mPagerPresenter.setTextColors(visitedTextColor, selectedTextColor, unvisitedTextColor);
        mPagerPresenter.setTextMargins(visitedTextMargin, selectedTextMargin, unvisitedTextMargin);
        mPagerPresenter.setMargins(leftMargin, rightMargin, topMargin, bottomMargin);
        mPagerPresenter.setTextSizes(visitedTextSize, selectedTextSize, unvisitedTextSize);
        mPagerPresenter.setTextStyles(visitedTextStyle, selectedTextStyle, unvisitedTextStyle);
        mPagerPresenter.setBorderSizes(visitedBorderWidth, selectedBorderWidth, unvisitedBorderWidth);
        mPagerPresenter.setBorderColors(visitedBorderColor, selectedBorderColor, unvisitedBorderColor);
        mPagerPresenter.setCheckmarkColors(visitedCheckmarkColor, selectedCheckmarkColor, unvisitedCheckmarkColor);
        mPagerPresenter.setShowCheckmarks(showVisitedCheckmark, showSelectedCheckmark, showUnvisitedCheckmark);
        mPagerPresenter.setIntermediateIconStyles(visitedIntermediatdStyle, unvisitedIntermediatdStyle);
        mPagerPresenter.setIconShapes(rectangularVisitedIcon, rectangularSelectedIcon, rectangularUnvisitedIcon);
        mPagerPresenter.setShowIconNumbers(showVisitedNumber, showSelectedNumber, showUnvisitedNumber);
        mPagerPresenter.setNumberColors(visitedNumberColor, selectedNumberColor, unvisitedNumberColor);

        if (entries != null) {
            String[] titles = new String[entries.length];
            int i = 0;
            for (CharSequence ch : entries) {
                titles[i++] = ch.toString();
            }

            mPagerPresenter.setTitles(titles);
        }

        mPagerPresenter.setNumberOfIcons(numOfIcons);

        init();

        mPaintIcon.setStyle(Paint.Style.FILL);
        mPaintText.setTextAlign(Paint.Align.LEFT);
        mPaintNumber.setTextAlign(Paint.Align.LEFT);
    }

    public StateViewPager setOnIconClickListener(OnIconClickListener l) {
        mPagerPresenter.setOnIconClickListener(l);
        return this;
    }

    /**
     * Sets the number of visible indicators (icons)
     *
     * @param num The number of visible icons.
     */
    public StateViewPager setNumberOfIcons(int num) {
        mPagerPresenter.setNumberOfIcons(num);
        requestLayout();
        return this;
    }

    /**
     * Sets array of icon titles
     *
     * @param titles
     */
    public StateViewPager setTitles(String[] titles) {
        mPagerPresenter.setTitles(titles);
        return this;
    }

    public StateViewPager setOrientation(int orientation) {
        switch (orientation) {
            case HORIZONTAL:
                if (mGravity == LEFT || mGravity == RIGHT) {
                    mGravity = TOP;
                }
                break;
            case VERTICAL:
                if (mGravity == TOP || mGravity == BOTTOM) {
                    mGravity = LEFT;
                }
                break;
            default:
                throw new IllegalArgumentException("Orientation must be either HORIZONTAL or VERTICAL.");
        }
        mPagerPresenter.setOrientation(orientation);
        mPagerPresenter.setGravity(mGravity);
        mOrientation = orientation;
        init();
        requestLayout();
        return this;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public StateViewPager setGravity(int gravity) {
        if (gravity != LEFT && gravity != RIGHT && gravity != TOP && gravity != BOTTOM) {
            throw new IllegalArgumentException("Gravity must be LEFT | RIGHT | TOP | BOTTOM.");
        }

        if (mOrientation == HORIZONTAL && (gravity == LEFT || gravity == RIGHT)) {
            gravity = TOP;
        }
        if (mOrientation == VERTICAL && (gravity == TOP || gravity == BOTTOM)) {
            gravity = LEFT;
        }

        mPagerPresenter.setGravity(gravity);
        mGravity = gravity;
        requestLayout();
        return this;
    }

    public StateViewPager setBorderSizes(int visitedDp, int selectedDp, int unvisitedDp) {
        mPagerPresenter.setBorderSizes(convertDpToPixel(visitedDp), convertDpToPixel(selectedDp), convertDpToPixel(unvisitedDp));
        requestLayout();
        return this;
    }

    public StateViewPager setIconSize(int widthDp, int heightDp) {
        mPagerPresenter.setIconSize(convertDpToPixel(widthDp), convertDpToPixel(heightDp));
        requestLayout();
        return this;
    }

    public StateViewPager setSelectedIconSize(int widthDp, int heightDp) {
        mPagerPresenter.setSelectedIconSize(convertDpToPixel(widthDp), convertDpToPixel(heightDp));
        requestLayout();
        return this;
    }

    public StateViewPager setIntermediateIconSize(int widthDp, int heightDp) {
        mPagerPresenter.setIntermediateIconSize(convertDpToPixel(widthDp), convertDpToPixel(heightDp));
        requestLayout();
        return this;
    }

    public StateViewPager setIntermediateIconStyles(String visitedStyle, String unvisitedStyle) {
        mPagerPresenter.setIntermediateIconStyles(visitedStyle, unvisitedStyle);
        requestLayout();
        return this;
    }

    public StateViewPager setTextGravities(int visitedTextGravity, int selectedTextGravity, int unvisitedTextGravity) {
        mPagerPresenter.setTextGravities(visitedTextGravity, selectedTextGravity, unvisitedTextGravity);
        requestLayout();
        return this;
    }

    public StateViewPager setShowCheckmarks(boolean showVisitedCheckmark, boolean showSelectedCheckmark, boolean showUnvisitedCheckmark) {
        mPagerPresenter.setShowCheckmarks(showVisitedCheckmark, showSelectedCheckmark, showUnvisitedCheckmark);
        requestLayout();
        return this;
    }

    public StateViewPager setShowNumbers(boolean showVisitedNumber, boolean showSelectedNumber, boolean showUnvisitedNumber) {
        mPagerPresenter.setShowIconNumbers(showVisitedNumber, showSelectedNumber, showUnvisitedNumber);
        requestLayout();
        return this;
    }

    public StateViewPager setIconColors(int visitedColor, int selectedColor, int unvisitedColor) {
        mPagerPresenter.setIconColors(visitedColor, selectedColor, unvisitedColor);
        requestLayout();
        return this;
    }

    public StateViewPager setGradientColors(boolean visitedColorGradient, boolean selectedColorGradient, boolean unvisitedColorGradient) {
        mPagerPresenter.setGradientColors(visitedColorGradient, selectedColorGradient, unvisitedColorGradient);
        requestLayout();
        return this;
    }

    public StateViewPager setIntermediateGradients(boolean visitedGradientColor, boolean unvisitedGradientColor) {
        mPagerPresenter.setIntermediateGradients(visitedGradientColor, unvisitedGradientColor);
        requestLayout();
        return this;
    }

    public StateViewPager setIntermediateIconColors(int visitedColorGradient, int unvisitedColorGradient) {
        mPagerPresenter.setIntermediateIconColors(visitedColorGradient, unvisitedColorGradient);
        requestLayout();
        return this;
    }

    public StateViewPager setBorderColors(int visitedColor, int selectedColor, int unvisitedColor) {
        mPagerPresenter.setBorderColors(visitedColor, selectedColor, unvisitedColor);
        requestLayout();
        return this;
    }

    public StateViewPager setCheckmarkColors(int visitedColor, int selectedColor, int unvisitedColor) {
        mPagerPresenter.setCheckmarkColors(visitedColor, selectedColor, unvisitedColor);
        requestLayout();
        return this;
    }

    public StateViewPager setNumberColors(int visitedColor, int selectedColor, int unvisitedColor) {
        mPagerPresenter.setNumberColors(visitedColor, selectedColor, unvisitedColor);
        requestLayout();
        return this;
    }

    public StateViewPager setTextColors(int visitedColor, int selectedColor, int unvisitedColor) {
        mPagerPresenter.setTextColors(visitedColor, selectedColor, unvisitedColor);
        requestLayout();
        return this;
    }

    public StateViewPager setTextStyles(int visitedTextStyle, int selectedTextStyle, int unvisitedTextStyle) {
        mPagerPresenter.setTextStyles(visitedTextStyle, selectedTextStyle, unvisitedTextStyle);
        requestLayout();
        return this;
    }

    public StateViewPager setMargins(int leftDp, int rightDp, int topDp, int bottomDp) {
        mPagerPresenter.setMargins(convertDpToPixel(leftDp), convertDpToPixel(rightDp), convertDpToPixel(topDp), convertDpToPixel(bottomDp));
        requestLayout();
        return this;
    }

    public StateViewPager setMarginBetweenIcons(int marginDp) {
        mPagerPresenter.setMarginBetweenIcons(convertDpToPixel(marginDp));
        requestLayout();
        return this;
    }

    public StateViewPager setTextMargins(int visitedTextMarginDp, int selectedTextMarginDp, int unvisitedTextMarginDp) {
        mPagerPresenter.setTextMargins(convertDpToPixel(visitedTextMarginDp), convertDpToPixel(selectedTextMarginDp), convertDpToPixel(unvisitedTextMarginDp));
        requestLayout();
        return this;
    }

    public StateViewPager setTextSizes(int visitedTextSizeDp, int selectedTextSizeDp, int unvisitedTextSizeDp) {
        mPagerPresenter.setTextSizes(convertDpToPixel(visitedTextSizeDp), convertDpToPixel(selectedTextSizeDp), convertDpToPixel(unvisitedTextSizeDp));
        requestLayout();
        return this;
    }

    public StateViewPager setIconDrawables(Drawable visitedDrawable, Drawable selectedDrawable, Drawable unvisitedDrawable) {
        Bitmap visitedBitmap = visitedDrawable != null ? getBitmapFromDrawable(visitedDrawable) : null;
        Bitmap unvisitedBitmap = unvisitedDrawable != null ? getBitmapFromDrawable(unvisitedDrawable) : null;
        Bitmap selectedBitmap = selectedDrawable != null ? getBitmapFromDrawable(selectedDrawable) : null;

        mPagerPresenter.setBitmaps(visitedBitmap, selectedBitmap, unvisitedBitmap);
        requestLayout();
        return this;
    }

    public StateViewPager setIntermediateIconDrawables(Drawable visitedDrawable, Drawable unvisitedDrawable) {
        Bitmap visitedBitmap = visitedDrawable != null ? getBitmapFromDrawable(visitedDrawable) : null;
        Bitmap unvisitedBitmap = unvisitedDrawable != null ? getBitmapFromDrawable(unvisitedDrawable) : null;

        mPagerPresenter.setIntermediateBitmaps(visitedBitmap, unvisitedBitmap);
        requestLayout();
        return this;
    }

    public StateViewPager setRectangularIcons(boolean visitedRectangular, boolean selectedRectangular, boolean unvisitedRectangular) {
        mPagerPresenter.setIconShapes(visitedRectangular, selectedRectangular, unvisitedRectangular);
        requestLayout();
        return this;
    }

    private int getGravity(String string, int orientation) {
        int gravity = TOP;

        if (string != null) {
            if (string.startsWith("bottom")) {
                gravity = BOTTOM;
            } else if (string.startsWith("left")) {
                gravity = LEFT;
            } else if (string.startsWith("right")) {
                gravity = RIGHT;
            } else if (string.startsWith("center")) {
                gravity = CENTER;
            }
        }

        if (orientation == -1) {
            return gravity;
        }

        if (orientation == HORIZONTAL && (gravity == LEFT || gravity == RIGHT)) {
            return TOP;
        }
        if (orientation == VERTICAL && (gravity == TOP || gravity == BOTTOM)) {
            return LEFT;
        }
        return gravity;
    }

    @NonNull
    private Bitmap getBitmapFromDrawable(@NonNull Drawable drawable) {
        checkNotNull(drawable);
        final Bitmap bmp = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bmp);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bmp;
    }

    private View getCurrentView(ViewPager viewPager) {
        try {
            final int currentItem = viewPager.getCurrentItem();
            for (int i = 0; i < viewPager.getChildCount(); i++) {
                final View child = viewPager.getChildAt(i);
                final ViewPager.LayoutParams layoutParams = (ViewPager.LayoutParams) child.getLayoutParams();

                Field f = layoutParams.getClass().getDeclaredField("position"); //NoSuchFieldException
                f.setAccessible(true);
                int position = (Integer) f.get(layoutParams); //IllegalAccessException

                if (!layoutParams.isDecor && currentItem == position) {
                    return child;
                }
            }
        } catch (NoSuchFieldException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.toString());
        } catch (IllegalAccessException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }

    private void initScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new CustomScroller(getContext())); // my liner scroller

            Field mFlingDistance = viewpager.getDeclaredField("mFlingDistance");
            mFlingDistance.setAccessible(true);
            mFlingDistance.set(this, 10);//10 dip

            Field mMinimumVelocity = viewpager.getDeclaredField("mMinimumVelocity");
            mMinimumVelocity.setAccessible(true);
            mMinimumVelocity.set(this, 0); //0 velocity

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    private void init() {
        if (mOrientation == VERTICAL) {
            initScroller();
            setPageTransformer(true, new VerticalPageTransformer());
            setOverScrollMode(OVER_SCROLL_NEVER);
        } else {
            setPageTransformer(true, null);
        }
        mPagerPresenter.init();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // call block() here if you want to draw behind children
        super.dispatchDraw(canvas);
        // call block() here if you want to draw over children

        mCanvas = canvas;
        mPagerPresenter.dispatchDraw(getWidth(), getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.mCanvas = canvas;
        mPagerPresenter.dispatchDraw(getWidth(), getHeight());
    }

    @Override
    public void setPresenter(@NonNull PagerContract.Presenter presenter) {
        mPagerPresenter = checkNotNull(presenter);
    }

    @Override
    public void drawRectangularIcon(@NonNull Icon icon) {
        checkNotNull(icon);

        if (mCanvas == null) {
            return;
        }

        if (icon.getBitmap() != null) {
            mCanvas.drawBitmap(icon.getBitmap(), icon.getLeft(), icon.getTop(), mPaintBitmap);
        } else {
            mPaintIcon.setColor(icon.getColor());

            if (icon.isGradient()) {
                Shader shader = new LinearGradient(icon.getLeft(),
                        icon.getTop() - icon.getHeight(), icon.getLeft(),
                        icon.getTop() + 5 * icon.getHeight(), icon.getColor(),
                        Color.WHITE, Shader.TileMode.CLAMP);
                mPaintIcon.setShader(shader);
            } else {
                mPaintIcon.setShader(null);
            }

            mPaintIcon.setColor(icon.getBorderColor());
            RectF rectangle = new RectF(icon.getLeft(), icon.getTop(), icon.getLeft() + icon.getWidth(), icon.getTop() + icon.getHeight());
            mCanvas.drawRoundRect(rectangle, 5, 5, mPaintIcon);

            mPaintIcon.setColor(icon.getColor());
            rectangle = new RectF(icon.getLeft() + icon.getBorderWidth(), icon.getTop() + icon.getBorderWidth(),
                    icon.getLeft() + icon.getWidth() - icon.getBorderWidth(), icon.getTop() + icon.getHeight() - icon.getBorderWidth());
            mPaintIcon.setColor(icon.getColor());
            mCanvas.drawRoundRect(rectangle, 5, 5, mPaintIcon);
        }

        if (icon.getText() != null && !(icon.isShowCheckmark() && icon.getTextGravity() == CENTER)) {
            mPaintText.setColor(icon.getTextColor());
            mPaintText.setTextSize(icon.getTextSize());
            mPaintText.setTypeface(Typeface.create(Typeface.DEFAULT, icon.getTextStyle()));
            drawText(mCanvas, icon, icon.getTextMargin());
        }

        if (icon.isShowCheckmark()) {
            drawCheckmarkSymbol(mCanvas, icon.getLeft(), icon.getTop(), icon.getWidth(), icon.getHeight(), icon.getCheckmarkColor());
        }

        if (icon.isShowNumber()) {
            drawNumber(icon);
        }
    }

    private int getTextWidth(String text) {
        int width = 0;
        Rect r = new Rect();
        for (String line: text.split("\n")) {
            mPaintText.getTextBounds(line, 0, line.length(), r);
            if (r.width() > width) {
                width = r.width();
            }
        }
        return width;
    }

    private void drawMultilineText(Canvas canvas, String text, float x, float y) {
        for (String line: text.split("\n")) {
            canvas.drawText(line, x, y, mPaintText);
            y += mPaintText.descent() - mPaintText.ascent();
        }
    }

    private void drawText(Canvas canvas, Icon icon, int margin) {
        String text = icon.getText();

        int w = getTextWidth(text);

        Rect r = new Rect();
        mPaintText.getTextBounds(text, 0, text.length(), r);
        float x = icon.getWidth() / 2f - w / 2f - r.left;
        float y = icon.getHeight() / 2f + r.height() / 2f - r.bottom;

        r.bottom = 0;

        switch (icon.getTextGravity()) {
            case CENTER:
                drawMultilineText(canvas, text, icon.getLeft() + x, icon.getTop() + y);
                break;
            case TOP:
                drawMultilineText(canvas, text, icon.getLeft() + x, icon.getTop() - r.height() / 2f - margin);
                break;
            case BOTTOM:
                drawMultilineText(canvas, text, icon.getLeft() + x, icon.getTop() + icon.getHeight() + r.height() + r.bottom + margin);
                break;
            case RIGHT:
                drawMultilineText(canvas, text, icon.getLeft() + icon.getWidth() + margin, icon.getTop() + y);
                break;
            case LEFT:
                drawMultilineText(canvas, text, icon.getLeft() - margin - r.width(), icon.getTop() + y);
                break;
        }
    }

    private void drawNumber(Icon icon) {
        if (mCanvas != null) {
            mPaintNumber.setColor(icon.getNumberColor());
            mPaintNumber.setTextSize(icon.getHeight() / 2.5f);

            String id =  Integer.toString(icon.getId());

            Rect r = new Rect();
            mPaintNumber.getTextBounds(id, 0, id.length(), r);
            float x = icon.getWidth() / 2f - r.width() / 2f - r.left;
            float y = icon.getHeight() / 2f + r.height() / 2f - r.bottom;
            r.bottom = 0;

            mCanvas.drawText(id, icon.getLeft() + x, icon.getTop() + y, mPaintNumber);
        }
    }

    private void drawCheckmarkSymbol(Canvas canvas, int left, int top, int width, int height, int color) {
        final int x = left + (int) (width * 0.42f);
        final int y = top + (int) (height * 0.65f);

        final float strokeWidth = height / 12f;
        final int length = Math.min(width, height) / 2;

        mPaintCheckmark.setColor(color);
        mPaintCheckmark.setStrokeWidth(strokeWidth);
        canvas.drawLine(x, y, x + length * 0.6f,y - length * 0.6f , mPaintCheckmark);
        canvas.drawLine(x, y, x - length * 0.29f,y - length * 0.29f , mPaintCheckmark);
        canvas.drawCircle(x, y, strokeWidth / 2, mPaintCheckmark);
    }

    @Override
    public void drawRoundedIcon(@NonNull Icon icon) {
        checkNotNull(icon);

        if (mCanvas != null) {
            if (icon.getBitmap() != null) {
                mCanvas.drawBitmap(icon.getBitmap(), icon.getLeft(), icon.getTop(), mPaintBitmap);
            } else {
                int radius = Math.min(icon.getWidth(), icon.getHeight()) / 2;
                int cx = icon.getLeft() + icon.getWidth() / 2;
                int cy = icon.getTop() + icon.getHeight() / 2;

                if (icon.isGradient()) {
                    Shader shader = new LinearGradient(icon.getLeft(),
                            icon.getTop() - icon.getHeight(), icon.getLeft(),
                            icon.getTop() + 5 * icon.getHeight(), icon.getColor(),
                            Color.WHITE, Shader.TileMode.CLAMP);
                    mPaintIcon.setShader(shader);
                } else {
                    mPaintIcon.setShader(null);
                }
                mPaintIcon.setColor(icon.getBorderColor());
                mCanvas.drawCircle(cx, cy, radius, mPaintIcon);
                mPaintIcon.setColor(icon.getColor());
                mCanvas.drawCircle(cx, cy, radius - icon.getBorderWidth(), mPaintIcon);
            }

            if (icon.getText() != null  && !(icon.isShowCheckmark() && icon.getTextGravity() == CENTER)) {
                mPaintText.setColor(icon.getTextColor());
                mPaintText.setTextSize(icon.getTextSize());
                mPaintText.setTypeface(Typeface.create(Typeface.DEFAULT, icon.getTextStyle()));
                drawText(mCanvas, icon, icon.getTextMargin());
            }

            if (icon.isShowCheckmark()) {
                drawCheckmarkSymbol(mCanvas, icon.getLeft(), icon.getTop(), icon.getWidth(), icon.getHeight(), icon.getCheckmarkColor());
            }

            if (icon.isShowNumber()) {
                drawNumber(icon);
            }
        }
    }

    @Override
    public void drawIntermediateIcon(@NonNull Icon icon) {
        checkNotNull(icon);

        if (mCanvas != null) {
            if (icon.getBitmap() != null) {
                mCanvas.drawBitmap(icon.getBitmap(), icon.getLeft(), icon.getTop(), mPaintBitmap);
            } else {
                int left = icon.getLeft();
                int top = icon.getTop();
                int width = icon.getWidth();
                int height = icon.getHeight();

                mPaintIcon.setColor(icon.getColor());
                mPaintIcon.setShader(null);

                if (icon.isDottedStyle()) {
                    int radius = Math.min(width, height) / 2;

                    if (radius == 0) {
                        return;
                    }

                    if (width > height) {
                        for (int x = left + radius; x <= left + width /*- radius*/; x += 4 * radius) {
                            mCanvas.drawCircle(x, top + height / 2f, radius, mPaintIcon);
                        }
                    } else {
                        for (int y = top + radius; y <= top + height/* - radius*/; y += 4 * radius) {
                            mCanvas.drawCircle(left + width / 2f, y, radius, mPaintIcon);
                        }
                    }
                } else {
                    mCanvas.drawRect(left, top, left + width, top + height, mPaintIcon);
                }
            }
        }
    }

    private int convertDpToPixel(float dp){
        return (int) (dp * ((float) getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    @Override
    public void setPage(int num, boolean smoothScroll) {
        if (getAdapter() != null && num >= 0 && num < getAdapter().getCount()) {
            setCurrentItem(num, smoothScroll);
        }
    }
    @Override
    public void setCurrentItem(final int item) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setCurrentItem(item, true);
            }
        }, 100);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int layoutHeight = mPagerPresenter.getLayoutHeight();
        final int layoutWidth = mPagerPresenter.getLayoutWidth();

        final Point pt = new Point( (int)event.getX(), (int)event.getY() );
        final int action = event.getAction();

        if (mOrientation == VERTICAL && mIsScrolling ) {
            if (Math.abs(mTouchedY - event.getY()) > getHeight() / 10) {

                if (event.getY() < mTouchedY) {
                    if(mCurrPage < getAdapter().getCount() - 1) {
                        setCurrentItem(mCurrPage + 1, true);
                    }
                } else {
                    if (mCurrPage > 0) {
                        setCurrentItem(mCurrPage - 1, true);
                    }
                }
                return true;
            }
        }

        if (!mIsTouched) {
            if (mOrientation == HORIZONTAL) {
                if (mGravity == TOP && event.getY() > layoutHeight || mGravity == BOTTOM && event.getY() < getHeight() - layoutHeight) {
                    return super.onTouchEvent(event);
                }
            }
            if (mOrientation == VERTICAL) {
                if (mGravity == LEFT && event.getX() > layoutWidth || mGravity == RIGHT && event.getX() < getWidth() - layoutWidth) {
                    return super.onTouchEvent(event);
                }
            }
            if (action == MotionEvent.ACTION_DOWN) {
               mStartPoint = pt;
               mIsTouched = true;

               mPagerPresenter.onDragging(mStartPoint, mStartPoint);
               mPagerPresenter.onActionDown();
           }
        }

        if (action == MotionEvent.ACTION_MOVE) {
            if (mIsTouched) {
                mCurrPoint = pt;
                mPagerPresenter.onDragging(mStartPoint, mCurrPoint);
            }
        }
        if (action == MotionEvent.ACTION_UP) {
            if (mIsTouched) {
                if (mCurrPoint != null && mStartPoint != null) {
                    if (mOrientation == HORIZONTAL) {
                        mPagerPresenter.onActionUp(mCurrPoint.x - mStartPoint.x);
                    } else {
                        mPagerPresenter.onActionUp(mCurrPoint.y - mStartPoint.y);
                    }
                }
            }

            if (mStartPoint != null) {
                if (Math.abs(mStartPoint.x - pt.x) < 10 && Math.abs(mStartPoint.y - pt.y) < 10) {
                    pt.x += getCurrentItem() * getWidth();
                    mPagerPresenter.onClick(getCurrentItem(), getWidth(), getHeight(), pt);
                }
            }

            mIsTouched = false;
        }
        invalidate();
        return true;
    }

    private boolean canChildScroll(int x, int y, View view, int direction) {
        if (view == null) {
            return false;
        }

        if (view instanceof ViewGroup) {
            if (DEBUG) Log.i(TAG, "ViewGroup: " + view.toString());

            final ViewGroup vGroup = (ViewGroup) view;
            View child;
            boolean result;
            for (int i = 0; i < vGroup.getChildCount(); i++) {
                child = vGroup.getChildAt(i);

                if (DEBUG) Log.i(TAG, " --- Child: " + child.toString());
                if (child instanceof RecyclerView) {
                    result = child.canScrollVertically(direction);
                } else if (child instanceof ViewGroup) {
                    result = canChildScroll(x, y, child, direction);
                } else if (child instanceof View) {
                    result = child.canScrollVertically(direction);
                } else {
                    result = canChildScroll(x, y, child, direction);
                }

                if (result) {
                    //return true;
                    return y > child.getTop() && y < child.getBottom() &&
                            x > child.getLeft() && x < child.getRight();
                }
            }
        }

        return view.canScrollVertically(direction) &&
                y > view.getTop() && y < view.getBottom() &&
                x > view.getLeft() && x < view.getRight();
    }

    private boolean canChildScrollVertically(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        return canChildScroll(x, y, view, 1) || canChildScroll(x, y, view, -1);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!onInterceptTouchEvent(ev)){
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if(child.dispatchTouchEvent(ev)) {
                    return super.dispatchTouchEvent(ev);
                }
            }
        }
        return super.dispatchTouchEvent(ev);
        // Always consume the event so it is not dispatched further up the chain
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int layoutHeight = mPagerPresenter.getLayoutHeight();
        int layoutWidth = mPagerPresenter.getLayoutWidth();

        if (mGravity == TOP && ev.getY() < layoutHeight || mGravity == BOTTOM && ev.getY() > getHeight() - layoutHeight) {
            return true;
        }

        if (mGravity == LEFT && ev.getX() < layoutWidth || mGravity == RIGHT && ev.getX() > getWidth() - layoutWidth) {
            mIsScrolling = false;
            return true;
        }

        if (canChildScrollVertically(getCurrentView(this), (int) ev.getX(), (int) ev.getY())) {
            return false;
        }

        if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mCurrPage = getCurrentItem();
            mTouchedX = ev.getX();
            mTouchedY = ev.getY();
            return super.onInterceptTouchEvent(ev);
        }

        float distance = mOrientation == VERTICAL ? Math.abs(mTouchedY - ev.getY()) : Math.abs(mTouchedX - ev.getX());

        if (distance > SCROLL_DISTANCE) {
            mIsScrolling = true;
            return true;
        }
        mIsScrolling = false;

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onPageScrolled(int position, float offset, int offsetPixels) {
        if (DEBUG) Log.i(TAG, "onPageScrolled " + position + "  " + offsetPixels);

        //mCurrPage = position;
        mPagerPresenter.onPageScrolled(offsetPixels + getWidth() * position, position, getWidth(), getHeight());
        super.onPageScrolled(position, offset, offsetPixels);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.page = getCurrentItem();
        savedState.width = getWidth();
        savedState.height = getHeight();
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(state);
        setCurrentItem(ss.page);
        requestLayout();
    }

    /**
     * Interface definition for a callback to be invoked when an icon is clicked.
     */
    public interface OnIconClickListener {
        /**
         * Called when an icon has been clicked.
         *
         * @param iconNum The number of icon that was clicked.
         */
        void onIconClick(int iconNum);
    }

    private class VerticalPageTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(@NonNull View view, float position) {

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            }  else if (position <= 1) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1);
                // Counteract the default slide transition
                view.setTranslationX(view.getWidth() * -position);

                //set Y position to swipe in from top
                float yPosition = position * view.getHeight();
                view.setTranslationY(yPosition);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }

    private class CustomScroller extends Scroller {
        CustomScroller(Context context) {
            super(context, new LinearInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 200);//200 duration
        }
    }

    private static class SavedState extends BaseSavedState {
        int page;
        int width;
        int height;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            page = in.readInt();
            width = in.readInt();
            height = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(page);
            out.writeInt(width);
            out.writeInt(height);
        }

        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}
